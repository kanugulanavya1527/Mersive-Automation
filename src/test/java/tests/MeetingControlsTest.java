package tests;

import base.BaseTest;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

public class MeetingControlsTest extends BaseTest {

    // ── Shared join flow ───────────────────────────────────

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

        String blockerHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room Blocker"
                );

        if (blockerHandle == null) {
            throw new RuntimeException(
                    "Mersive Room Blocker not found."
            );
        }

        setLastMeetingOverlayHandle(blockerHandle);
        attachByHandle(blockerHandle);

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);
        Thread.sleep(8000);

        Assert.assertTrue(overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load");
        System.out.println("✓ Meeting joined");
        return overlay;
    }

    // ── Tests ──────────────────────────────────────────────

    @Test(priority = 15)
    public void TC_015_VerifyCameraToggleOnOffOnFromMeetingOverlay()
            throws Exception {
        System.out.println("=== TC_015: Camera Toggle in Meeting ===");

        MeetingOverlayPage overlay = joinMeeting();

        boolean currentlyOn  = overlay.isOverlayCameraOn();
        boolean currentlyOff = overlay.isOverlayCameraOff();

        System.out.println("Camera ON=" + currentlyOn + " OFF=" + currentlyOff);

        if (currentlyOn) {
            overlay.clickOverlayCameraToggle();
            Assert.assertTrue(overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF");
            System.out.println("✓ Camera OFF");

            overlay.clickOverlayCameraToggle();
            Assert.assertTrue(overlay.waitForOverlayCameraOn(),
                    "Camera did not turn ON");
            System.out.println("✓ Camera ON again");

        } else if (currentlyOff) {
            overlay.clickOverlayCameraToggle();
            Assert.assertTrue(overlay.waitForOverlayCameraOn(),
                    "Camera did not turn ON");
            System.out.println("✓ Camera ON");

            overlay.clickOverlayCameraToggle();
            Assert.assertTrue(overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF");
            System.out.println("✓ Camera OFF again");

        } else {
            throw new RuntimeException("Cannot detect initial camera state");
        }

        System.out.println("TC_015 PASSED");
    }

    @Test(priority = 16)
    public void TC_016_VerifyMicToggleOnOffOnFromMeetingOverlay()
            throws Exception {
        System.out.println("=== TC_016: Mic Toggle in Meeting ===");

        MeetingOverlayPage overlay = joinMeeting();

        new WebDriverWait(driver, 40).until(d ->
                !d.findElements(By.xpath(
                        "//*[@AutomationId='MicButton']")).isEmpty());

        boolean currentlyMuted   = overlay.isOverlayMicMuted();
        boolean currentlyUnmuted = overlay.isOverlayMicUnmuted();

        System.out.println("Muted=" + currentlyMuted
                + " Unmuted=" + currentlyUnmuted);

        if (currentlyMuted) {
            overlay.clickOverlayMicToggle();
            Assert.assertTrue(overlay.waitForOverlayMicUnmuted(),
                    "Mic did not unmute");
            System.out.println("✓ Mic unmuted");

            overlay.clickOverlayMicToggle();
            Assert.assertTrue(overlay.waitForOverlayMicMuted(),
                    "Mic did not mute again");
            System.out.println("✓ Mic muted again");

        } else if (currentlyUnmuted) {
            overlay.clickOverlayMicToggle();
            Assert.assertTrue(overlay.waitForOverlayMicMuted(),
                    "Mic did not mute");
            System.out.println("✓ Mic muted");

            overlay.clickOverlayMicToggle();
            Assert.assertTrue(overlay.waitForOverlayMicUnmuted(),
                    "Mic did not unmute again");
            System.out.println("✓ Mic unmuted again");

        } else {
            throw new RuntimeException("Cannot detect initial mic state");
        }

        System.out.println("TC_016 PASSED");
    }

    @Test(priority = 17)
    public void TC_017_VerifyMicSyncBetweenToolbarAndParticipants()
            throws Exception {
        System.out.println("=== TC_017: Mic Sync Toolbar and Participants ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(overlay.waitForPeopleButtonReady(),
                "People button not visible");
        overlay.clickPeopleButton();
        Thread.sleep(3000);
        System.out.println("✓ Participants panel opened");

        overlay.clickOverlayMicToggle();
        System.out.println("Immediate State = "+ overlay.getOverlayMicState()); //new line added to find issue
        Assert.assertTrue(overlay.waitForOverlayMicMuted(),
                "Toolbar not showing muted");
        System.out.println("✓ Mic muted");

        Assert.assertTrue(overlay.isOverlayMicStateValid(),
                "Mic states not synced");
        System.out.println("✓ Muted state valid");

        Thread.sleep(3000);

        overlay.clickOverlayMicToggle();
        Assert.assertTrue(overlay.waitForOverlayMicUnmuted(),
                "Toolbar still showing muted");
        System.out.println("✓ Mic unmuted");

        Assert.assertTrue(overlay.isOverlayMicStateValid(),
                "Mic states not synced");
        System.out.println("✓ Unmuted state valid");

        System.out.println("TC_017 PASSED");
    }

    @Test(priority = 18)
    public void TC_018_VerifyJoinMeetingAndLeaveGracefully()
            throws Exception {
        System.out.println("=== TC_018: Join and Leave Meeting ===");

        MeetingOverlayPage overlay = joinMeeting();
        pages.RootSessionPage root       = new pages.RootSessionPage(driver);

        overlay.clickLeaveButton();
        System.out.println("✓ Leave button clicked");

        root.clickLeaveMeetingConfirmation();
        System.out.println("✓ Leave confirmed");

        Thread.sleep(3000);

        System.out.println("TC_018 PASSED");
    }

    @Test(priority = 19)
    public void TC_019_VerifyRaiseHandButtonInMeeting() throws Exception {
        System.out.println("=== TC_019: Raise Hand ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(overlay.waitForPeopleButtonReady(),
                "People button not visible");
        overlay.clickPeopleButton();
        Thread.sleep(3000);
        System.out.println("✓ Participants panel opened");

        overlay.clickRaiseHandButton();
        Assert.assertTrue(overlay.isMyHandRaised(),
                "Hand raise not reflected");
        System.out.println("✓ Hand raised");

        Thread.sleep(3000);

        overlay.clickLowerHandButton();
        Assert.assertTrue(overlay.isMyHandLowered(),
                "Hand not lowered");
        System.out.println("✓ Hand lowered");

        System.out.println("TC_019 PASSED");
    }

    @Test(priority = 20)
    public void TC_020_VerifyRaiseHandWithDoubleClick() throws Exception {
        System.out.println("=== TC_020: Raise Hand Double Click ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(overlay.waitForPeopleButtonReady(),
                "People button not visible");
        overlay.clickPeopleButton();
        Thread.sleep(3000);
        System.out.println("✓ Participants panel opened");

        overlay.doubleClickRaiseHandButton();
        Assert.assertTrue(overlay.isMyHandRaised(),
                "Hand raise not reflected after double-click");
        System.out.println("✓ Hand raised via double-click");

        Thread.sleep(3000);

        overlay.doubleClickLowerHandButton();
        Assert.assertTrue(overlay.isMyHandLowered(),
                "Hand not lowered after double-click");
        System.out.println("✓ Hand lowered via double-click");

        System.out.println("TC_020 PASSED");
    }
}