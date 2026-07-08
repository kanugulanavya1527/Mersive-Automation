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

public class ZoomAVSyncTest extends BaseTest {

    private String overlayHandle;

//
private MeetingOverlayPage joinZoomMeeting() throws Exception {

    MeetingCardPage cards = new MeetingCardPage(driver);
    PreJoinPage preJoin = new PreJoinPage(driver);

    cards.clickJoinForFirstZoomMeeting();

    new WebDriverWait(driver, 30)
            .until(d -> {
                try {
                    return preJoin.isPreJoinScreenLoaded();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

    preJoin.clickJoinZoomMeeting();

    switchToDesktop();
//
//    String blocker = WindowHelper.waitForBlockerWindow(60); // was 30
//
//    attachByHandle(blocker);
//    overlayHandle = blocker;
//    setLastMeetingOverlayHandle(blocker); // ADD
    String blocker = WindowHelper.waitForBlockerWindow(60);

    if (blocker == null) {
        throw new RuntimeException(
                "BUG: Zoom launched outside Mersive. "
                        + "Mersive Room Blocker window was not created."
        );
    }

    attachByHandle(blocker);
    overlayHandle = blocker;
    setLastMeetingOverlayHandle(blocker);

    MeetingOverlayPage overlay = new MeetingOverlayPage(driver);

    Thread.sleep(8000);

    Assert.assertTrue(
            overlay.waitForMeetingJoinedScreen(),
            "Meeting screen did not load"
    );

    System.out.println("✓ Zoom meeting joined");

    return overlay;
}

    @Test(priority = 56)
    public void TC_056_VerifyCameraStateSyncBetweenAVPanelAndTopRibbon()
            throws Exception {

        System.out.println(
                "=== TC_056: Camera Sync AV Panel ↔ Top Ribbon ==="
        );

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Thread.sleep(5000);

        boolean ribbonOn =
                overlay.isOverlayCameraOn();

        boolean ribbonOff =
                overlay.isOverlayCameraOff();

        if (!ribbonOn && !ribbonOff) {
            throw new RuntimeException(
                    "Cannot detect initial camera state"
            );
        }

        attachToControlsWindow();

        MeetingOverlayPage controlsOverlay =
                new MeetingOverlayPage(driver);

        controlsOverlay.clickAudioVisualButton();

        Thread.sleep(3000);

        attachToAVControlsWindow();

        AVControlsPage avPanel =
                new AVControlsPage(driver);

        if (ribbonOn) {

            Assert.assertTrue(
                    avPanel.isCameraOn(),
                    "Panel shows OFF but ribbon shows ON"
            );

        } else {

            Assert.assertTrue(
                    avPanel.isCameraOff(),
                    "Panel shows ON but ribbon shows OFF"
            );
        }

        avPanel.clickCameraToggle();

        Thread.sleep(2000);

        if (ribbonOn) {

            Assert.assertTrue(
                    avPanel.waitForCameraOff(),
                    "Panel did not turn OFF"
            );

        } else {

            Assert.assertTrue(
                    avPanel.waitForCameraOn(),
                    "Panel did not turn ON"
            );
        }

        attachByHandle(overlayHandle);


        overlay =
                new MeetingOverlayPage(driver);

        if (ribbonOn) {

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOff(),
                    "Ribbon did not reflect Camera OFF"
            );

        } else {

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOn(),
                    "Ribbon did not reflect Camera ON"
            );
        }

        attachToAVControlsWindow();

        avPanel =
                new AVControlsPage(driver);

        avPanel.clickCameraToggle();

        Thread.sleep(2000);

        avPanel.clickSwipeToClose();

        System.out.println("TC_056 PASSED");
    }

    @Test(priority = 57)
    public void TC_057_VerifyMicStateSyncBetweenAVPanelAndTopRibbon()
            throws Exception {

        System.out.println(
                "=== TC_057: Mic Sync AV Panel ↔ Top Ribbon ==="
        );

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Thread.sleep(5000);

        boolean ribbonMuted =
                overlay.isOverlayMicMuted();

        boolean ribbonUnmuted =
                overlay.isOverlayMicUnmuted();

        if (!ribbonMuted && !ribbonUnmuted) {

            throw new RuntimeException(
                    "Cannot detect initial mic state"
            );
        }

        attachToControlsWindow();

        MeetingOverlayPage controlsOverlay =
                new MeetingOverlayPage(driver);

        controlsOverlay.clickAudioVisualButton();

        Thread.sleep(3000);

        attachToAVControlsWindow();

        AVControlsPage avPanel =
                new AVControlsPage(driver);

        if (ribbonMuted) {

            Assert.assertTrue(
                    avPanel.isMicMuted(),
                    "Panel shows ON but ribbon shows MUTED"
            );

        } else {

            Assert.assertTrue(
                    avPanel.isMicOn(),
                    "Panel shows MUTED but ribbon shows ON"
            );
        }

        avPanel.clickMicToggle();

        Thread.sleep(2000);

        if (ribbonMuted) {

            Assert.assertTrue(
                    avPanel.isMicOn(),
                    "AV panel mic did not turn ON"
            );

        } else {

            Assert.assertTrue(
                    avPanel.isMicMuted(),
                    "AV panel mic did not mute"
            );
        }

        attachByHandle(overlayHandle);

        overlay =
                new MeetingOverlayPage(driver);

        if (ribbonMuted) {

            Assert.assertTrue(
                    overlay.waitForOverlayMicUnmuted(),
                    "Ribbon did not reflect Mic ON"
            );

        } else {

            Assert.assertTrue(
                    overlay.waitForOverlayMicMuted(),
                    "Ribbon did not reflect Mic MUTED"
            );
        }

        attachToAVControlsWindow();

        avPanel =
                new AVControlsPage(driver);

        avPanel.clickMicToggle();

        Thread.sleep(2000);

        avPanel.clickSwipeToClose();

        System.out.println("TC_057 PASSED");
    }
}