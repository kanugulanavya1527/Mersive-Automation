package tests;

import base.BaseTest;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import pages.RootSessionPage;
import utils.WindowHelper;

public class ZoomControlsTest extends BaseTest {
    private String lastZoomOverlayHandle;
//
//    private MeetingOverlayPage joinZoomMeeting() throws Exception {
//
//        MeetingCardPage cards = new MeetingCardPage(driver);
//        PreJoinPage preJoin = new PreJoinPage(driver);
//
//        System.out.println("Step 1: Opening meeting card");
//        cards.clickJoinForFirstZoomMeeting();
//
//        System.out.println("Step 2: Waiting for pre-join screen");
//
//        new WebDriverWait(driver, 30)
//                .until(d -> preJoin.isPreJoinScreenLoaded());
//
//        Assert.assertTrue(
//                preJoin.isPreJoinScreenLoaded(),
//                "Pre-join screen not loaded"
//        );
//
//        String originalHandle =
//                getWindowHandle("Mersive Room");
//
//        System.out.println("Step 3: Clicking Join Zoom Meeting");
//
//        preJoin.clickJoinZoomMeeting();
//
//        System.out.println("Step 4: Switching to desktop session");
//
//        switchToDesktop();
//
//        String newHandle =
//                waitForNewWindow(
//                        "Mersive Room",
//                        originalHandle,
//                        100
//                );
//
//        Assert.assertNotNull(
//                newHandle,
//                "Zoom meeting window not found"
//        );
//
//        System.out.println("Step 5: Attaching to Zoom meeting window");
//
//        attachByHandle(newHandle);
//        return new MeetingOverlayPage(driver);
//
//
//
//    }
//private MeetingOverlayPage joinZoomMeeting() throws Exception {
//
//    MeetingCardPage cards = new MeetingCardPage(driver);
//    PreJoinPage preJoin = new PreJoinPage(driver);
//
//    System.out.println("Step 1: Opening meeting card");
//    cards.clickJoinForFirstZoomMeeting();
//
//    System.out.println("Step 2: Waiting for pre-join screen");
//    new WebDriverWait(driver, 30)
//            .until(d -> preJoin.isPreJoinScreenLoaded());
//
//    Assert.assertTrue(
//            preJoin.isPreJoinScreenLoaded(),
//            "Pre-join screen not loaded"
//    );
//
//    System.out.println("Step 3: Clicking Join Zoom Meeting");
//    preJoin.clickJoinZoomMeeting();
//
//    System.out.println("Step 4: Switching to desktop session");
//    switchToDesktop();
//
//    System.out.println("Step 5: Waiting for Mersive Room Blocker window");
//    String blockerHandle = WindowHelper.waitForBlockerWindow(60);
//
//    Assert.assertNotNull(
//            blockerHandle,
//            "Mersive Room Blocker window not found"
//    );
//
//    System.out.println("Step 6: Attaching to Blocker window");
//    attachByHandle(blockerHandle);
//
//    // remember it for teardown
//    lastZoomOverlayHandle = blockerHandle;
//
//    return new MeetingOverlayPage(driver);
//}

    private MeetingOverlayPage joinZoomMeeting() throws Exception {

        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        System.out.println("Step 1: Opening meeting card");
        cards.clickJoinForFirstZoomMeeting();

        System.out.println("Step 2: Waiting for pre-join screen");
        new WebDriverWait(driver, 30)
                .until(d -> {
                    try {
                        return preJoin.isPreJoinScreenLoaded();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Pre-join screen not loaded"
        );

        System.out.println("Step 3: Clicking Join Zoom Meeting");
        preJoin.clickJoinZoomMeeting();

        System.out.println("Step 4: Switching to desktop session");
        switchToDesktop();

// ADD
// THIS
      //zoom  WindowHelper.printAllWindows();

        System.out.println("Step 5: Waiting for Mersive Room Blocker window");
        //String blockerHandle = WindowHelper.waitForBlockerWindow(60);
        String blockerHandle =
                WindowHelper.waitForBlockerWindow(30);

        if (blockerHandle == null) {
            throw new RuntimeException(
                    "Zoom detached from Mersive. Blocker window was not created."
            );
        }

        System.out.println("Step 6: Attaching to Blocker window");
        attachByHandle(blockerHandle);
        lastZoomOverlayHandle = blockerHandle;
        setLastMeetingOverlayHandle(blockerHandle); // ADD

        return new MeetingOverlayPage(driver);
    }
    @Test(priority = 47)
    public void TC_047_OpenParticipantsPanel() throws Exception {

        System.out.println("TC_047 Started");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not available"
        );

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open"
        );

        System.out.println("TC_047 PASSED");
    }
    @Test(priority = 48)
    public void TC_048_VerifyChatPanelOpens() throws Exception {

        System.out.println("TC_048 Started");

        MeetingOverlayPage overlay = joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "Chat button not available"
        );

        overlay.clickChatButtonRobust();

        Assert.assertTrue(
                overlay.isChatPanelVisible(),
                "Chat panel did not open"
        );

        System.out.println("TC_048 PASSED");
    }
    @Test(priority = 49)
    public void TC_049_VerifyRaiseHandInZoomMeeting() throws Exception {

        System.out.println("=== TC_0349: Raise Hand In Zoom ===");

        MeetingOverlayPage overlay = joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible"
        );

        overlay.clickPeopleButton();
        Thread.sleep(3000);

        System.out.println("✓ Participants panel opened");

        overlay.clickRaiseHandButton();

        Assert.assertTrue(
                overlay.isMyHandRaised(),
                "Hand raise not reflected"
        );

        System.out.println("✓ Hand raised");

        Thread.sleep(3000);

        overlay.clickLowerHandButton();

        Assert.assertTrue(
                overlay.isMyHandLowered(),
                "Hand not lowered"
        );

        System.out.println("✓ Hand lowered");

        System.out.println("TC_049 PASSED");
    }

    @Test(priority = 50)
    public void TC_050_VerifyMuteUnmuteMicInZoomMeeting()
            throws Exception {

        System.out.println("=== TC_050: Mic Sync In Zoom Meeting ===");

        MeetingOverlayPage overlay = joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible"
        );

        overlay.clickPeopleButton();
        Thread.sleep(3000);

        System.out.println("✓ Participants panel opened");

        // After overlay.clickPeopleButton(); Thread.sleep(3000);
// ADD THIS before first clickOverlayMicToggle():
// Normalize to ON
        String initialMic = overlay.getOverlayMicState();
        if (initialMic.equalsIgnoreCase("Muted")) {
            overlay.clickOverlayMicToggle();
            Thread.sleep(1500);
        }

// Step 1: Mute
        overlay.clickOverlayMicToggle();

        Assert.assertTrue(
                overlay.waitForOverlayMicMuted(),
                "Toolbar not showing muted"
        );
        System.out.println("✓ Mic muted");

        Assert.assertTrue(overlay.isOverlayMicStateValid(), "Mic state invalid");
        System.out.println("✓ Muted state valid");

        Thread.sleep(3000);

// Step 2: Unmute
        overlay.clickOverlayMicToggle();

        Assert.assertTrue(
                overlay.waitForOverlayMicUnmuted(),
                "Toolbar still showing muted"
        );
        System.out.println("✓ Mic unmuted");

        Assert.assertTrue(overlay.isOverlayMicStateValid(), "Mic state invalid");
        System.out.println("✓ Unmuted state valid");

        System.out.println("TC_050 PASSED");
    }
    @Test(priority = 51)
    public void TC_051_VerifyCameraToggleOnOffOnInZoomMeeting()
            throws Exception {

        System.out.println("=== TC_051: Camera Toggle In Zoom Meeting ===");

        MeetingOverlayPage overlay = joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        boolean currentlyOn = overlay.isOverlayCameraOn();
        boolean currentlyOff = overlay.isOverlayCameraOff();

        System.out.println(
                "Camera ON=" + currentlyOn +
                        " OFF=" + currentlyOff
        );

        if (currentlyOn) {

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF"
            );

            System.out.println("✓ Camera OFF");

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOn(),
                    "Camera did not turn ON again"
            );

            System.out.println("✓ Camera ON again");

        } else if (currentlyOff) {

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOn(),
                    "Camera did not turn ON"
            );

            System.out.println("✓ Camera ON");

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF again"
            );

            System.out.println("✓ Camera OFF again");

        } else {

            throw new RuntimeException(
                    "Cannot detect initial camera state"
            );
        }

        System.out.println("TC_051 PASSED");
    }
    @Test(priority = 52)
    public void TC_052_VerifyJoinAndLeaveZoomMeetingGracefully()
            throws Exception {

        System.out.println("=== TC_052: Join and Leave Zoom Meeting ===");

        MeetingOverlayPage overlay = joinZoomMeeting();
        pages.RootSessionPage root = new pages.RootSessionPage(driver);

        overlay.clickLeaveButton();

        System.out.println("✓ Leave button clicked");

        root.clickLeaveMeetingConfirmation();

        System.out.println("✓ Leave confirmed");

        Thread.sleep(15000);

        System.out.println("TC_052 PASSED");
    }
    @Test(priority = 53)
    public void TC_053_VerifyStayInMeeting() throws Exception {

        System.out.println("=== TC_053: Stay In Meeting ===");

        MeetingOverlayPage overlay = joinZoomMeeting();

        RootSessionPage root =
                new RootSessionPage(driver);

        overlay.clickLeaveButton();

        Thread.sleep(3000);

        root.clickStayInMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Meeting was exited unexpectedly"
        );

        System.out.println("TC_053 PASSED");
    }
//    @Test(priority = 43)
//    public void TC_043_VerifyMeetingTitleAfterJoiningZoom()
//            throws Exception {
//
//        System.out.println(
//                "=== TC_043: Meeting Title Validation ==="
//        );
//
//        MeetingCardPage cards =
//                new MeetingCardPage(driver);
//
//        String expectedTitle =
//                cards.getMeetingCardTitle();
//
//        Assert.assertNotNull(
//                expectedTitle,
//                "Meeting title was not found on the meeting card"
//        );
//
//        System.out.println(
//                "Expected Title: " + expectedTitle
//        );
//
//        MeetingOverlayPage overlay =
//                joinZoomMeeting();
//
//        String actualTitle =
//                overlay.getInMeetingName();
//
//        System.out.println(
//                "Actual Title: " + actualTitle
//        );
//
//        Assert.assertEquals(
//                actualTitle,
//                expectedTitle,
//                "Meeting title mismatch"
//        );
//
//        System.out.println("TC_043 PASSED");
//    }
}
