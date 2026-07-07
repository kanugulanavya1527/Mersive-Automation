package tests;

import base.BaseTest;
import pages.AVControlsPage;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

public class AVSyncTest extends BaseTest {

    private String overlayHandle;

    private MeetingOverlayPage joinMeeting() throws Exception {
        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin   = new PreJoinPage(driver);

        cards.clickJoinForFirstTeamsMeeting();

        new WebDriverWait(driver, 20).until(
                d -> {
                    try {
                        return new PreJoinPage(driver).isPreJoinScreenLoaded();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

//        preJoin.clickJoinMicrosoftTeamsMeeting();
//        switchToDesktop();
        preJoin.clickJoinMicrosoftTeamsMeeting();

        Thread.sleep(5000);

        if (preJoin.isJoinNowVisible()) {
            preJoin.clickJoinNow();
            Thread.sleep(8000);
        }

        switchToDesktop();

        overlayHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room Blocker"
                );

        if (overlayHandle == null) {
            throw new RuntimeException(
                    "Mersive Room Blocker not found."
            );
        }
        setLastMeetingOverlayHandle(overlayHandle);
        attachByHandle(overlayHandle);

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);
        Thread.sleep(8000);

        Assert.assertTrue(overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load");
        System.out.println("✓ Meeting joined");
        return overlay;
    }

    @Test(priority = 30)
    public void TC_030_VerifyCameraStateSyncBetweenAVPanelAndTopRibbon()
            throws Exception {
        System.out.println("=== TC_030: Camera Sync AV Panel ↔ Top Ribbon ===");

        MeetingOverlayPage overlay = joinMeeting();
        Thread.sleep(5000);

        boolean ribbonOn  = overlay.isOverlayCameraOn();
        boolean ribbonOff = overlay.isOverlayCameraOff();

        System.out.println("Ribbon ON=" + ribbonOn + " OFF=" + ribbonOff);

        if (!ribbonOn && !ribbonOff)
            throw new RuntimeException("Cannot detect initial camera state");

        // Open AV panel
        attachToControlsWindow();
        MeetingOverlayPage controlsOverlay = new MeetingOverlayPage(driver);
        controlsOverlay.clickAudioVisualButton();
        System.out.println("✓ Audio & Visual clicked");
        Thread.sleep(3000);

        // Read AV panel state
        attachToAVControlsWindow();
        AVControlsPage avPanel = new AVControlsPage(driver);
        System.out.println("AV Panel Camera: " + avPanel.getCameraState());

        if (ribbonOn) {
            Assert.assertTrue(avPanel.isCameraOn(),
                    "Panel shows OFF but ribbon shows ON");
            System.out.println("✓ Both ON — in sync");
        } else {
            Assert.assertTrue(avPanel.isCameraOff(),
                    "Panel shows ON but ribbon shows OFF");
            System.out.println("✓ Both OFF — in sync");
        }

        // Toggle in AV panel
        avPanel.clickCameraToggle();
        Thread.sleep(2000);

        if (ribbonOn) {
            Assert.assertTrue(avPanel.waitForCameraOff(),
                    "Panel did not turn OFF");
            System.out.println("✓ AV Panel: Camera OFF");
        } else {
            Assert.assertTrue(avPanel.waitForCameraOn(),
                    "Panel did not turn ON");
            System.out.println("✓ AV Panel: Camera ON");
        }

        // Verify ribbon updated
        attachByHandle(overlayHandle);
        overlay = new MeetingOverlayPage(driver);

        if (ribbonOn) {
            Assert.assertTrue(overlay.waitForOverlayCameraOff(),
                    "Ribbon did not reflect Camera OFF");
            System.out.println("✓ Ribbon: Camera OFF — synced");
        } else {
            Assert.assertTrue(overlay.waitForOverlayCameraOn(),
                    "Ribbon did not reflect Camera ON");
            System.out.println("✓ Ribbon: Camera ON — synced");
        }

        // Restore original state
        attachToAVControlsWindow();
        avPanel = new AVControlsPage(driver);
        avPanel.clickCameraToggle();
        Thread.sleep(2000);
        avPanel.clickSwipeToClose();
        Thread.sleep(2000);
        System.out.println("✓ Restored and panel closed");

        System.out.println("TC_030 PASSED");
    }

    @Test(priority = 31)
    public void TC_031_VerifyMicStateSyncBetweenAVPanelAndTopRibbon()
            throws Exception {
        System.out.println("=== TC_031: Mic Sync AV Panel ↔ Top Ribbon ===");

        MeetingOverlayPage overlay = joinMeeting();
        Thread.sleep(5000);

        boolean ribbonMuted   = overlay.isOverlayMicMuted();
        boolean ribbonUnmuted = overlay.isOverlayMicUnmuted();

        System.out.println("Muted=" + ribbonMuted
                + " Unmuted=" + ribbonUnmuted);

        if (!ribbonMuted && !ribbonUnmuted)
            throw new RuntimeException("Cannot detect initial mic state");

        // Open AV panel
        attachToControlsWindow();
        MeetingOverlayPage controlsOverlay = new MeetingOverlayPage(driver);
        controlsOverlay.clickAudioVisualButton();
        System.out.println("✓ Audio & Visual clicked");
        Thread.sleep(3000);

        // Read AV panel state
        attachToAVControlsWindow();
        AVControlsPage avPanel = new AVControlsPage(driver);
        System.out.println("AV Panel Mic: " + avPanel.getMicState());

        if (ribbonMuted) {
            Assert.assertTrue(avPanel.isMicMuted(),
                    "Panel shows ON but ribbon shows MUTED");
            System.out.println("✓ Both MUTED — in sync");
        } else {
            Assert.assertTrue(avPanel.isMicOn(),
                    "Panel shows MUTED but ribbon shows ON");
            System.out.println("✓ Both ON — in sync");
        }

        // Toggle in AV panel
        avPanel.clickMicToggle();
        Thread.sleep(2000);

        if (ribbonMuted) {
            Assert.assertTrue(avPanel.isMicOn(),
                    "AV panel mic did not turn ON");
            System.out.println("✓ AV Panel: Mic ON");
        } else {
            Assert.assertTrue(avPanel.isMicMuted(),
                    "AV panel mic did not mute");
            System.out.println("✓ AV Panel: Mic MUTED");
        }

        // Verify ribbon updated
        attachByHandle(overlayHandle);
        overlay = new MeetingOverlayPage(driver);

        if (ribbonMuted) {
            Assert.assertTrue(overlay.waitForOverlayMicUnmuted(),
                    "Ribbon did not reflect Mic ON");
            System.out.println("✓ Ribbon: Mic ON — synced");
        } else {
            Assert.assertTrue(overlay.waitForOverlayMicMuted(),
                    "Ribbon did not reflect Mic MUTED");
            System.out.println("✓ Ribbon: Mic MUTED — synced");
        }

        // Restore
        attachToAVControlsWindow();
        avPanel = new AVControlsPage(driver);
        avPanel.clickMicToggle();
        Thread.sleep(2000);
        avPanel.clickSwipeToClose();
        Thread.sleep(2000);
        System.out.println("✓ Restored and panel closed");

        System.out.println("TC_031 PASSED");
    }

    @Test(priority = 32)
    public void TC_032_VerifySpeakerVolumeUpDown() throws Exception {

        System.out.println("=== TC_032: Verify Speaker Volume Up/Down ===");

        MeetingOverlayPage overlay = joinMeeting();
        Thread.sleep(5000);

        // Open AV panel
        attachToControlsWindow();

        MeetingOverlayPage controlsOverlay = new MeetingOverlayPage(driver);
        controlsOverlay.clickAudioVisualButton();
        System.out.println("✓ Audio & Visual clicked");

        Thread.sleep(3000);

        // Switch to AV Controls
        attachToAVControlsWindow();

        AVControlsPage avPanel = new AVControlsPage(driver);

        int initialVolume = avPanel.getSpeakerVolume();
        System.out.println("Initial Volume : " + initialVolume + "%");

        // Increase
        avPanel.increaseSpeakerVolume();

        Thread.sleep(2000);

        int increasedVolume = avPanel.getSpeakerVolume();

        System.out.println("After Increase : " + increasedVolume + "%");

        Assert.assertTrue(
                increasedVolume > initialVolume,
                "Speaker volume did not increase."
        );

        System.out.println("✓ Speaker volume increased");

        // Decrease
        avPanel.decreaseSpeakerVolume();

        Thread.sleep(2000);

        int decreasedVolume = avPanel.getSpeakerVolume();

        System.out.println("After Decrease : " + decreasedVolume + "%");

        Assert.assertTrue(
                decreasedVolume < increasedVolume,
                "Speaker volume did not decrease."
        );

        System.out.println("✓ Speaker volume decreased");

        avPanel.clickSwipeToClose();

        System.out.println("TC_032 PASSED");
    }
}