package tests;

import base.BaseTest;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import utils.WindowHelper;

public class ZoomMeetingJoinTest extends BaseTest {

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
//        System.out.println("Step 3: Clicking Join Zoom Meeting");
//
//        preJoin.clickJoinZoomMeeting();
//
//        System.out.println("Step 4: Switching to desktop session");
//
//        switchToDesktop();
//
//        System.out.println(
//                "Step 5: Waiting for Mersive Room Blocker window"
//        );
//
//        String blockerHandle =
//                WindowHelper.waitForBlockerWindow(60);
//
//        Assert.assertNotNull(
//                blockerHandle,
//                "Mersive Room Blocker window not found"
//        );
//
//        System.out.println(
//                "Step 6: Attaching to Blocker window"
//        );
//
//        attachByHandle(blockerHandle);
//
//        return new MeetingOverlayPage(driver);
//
//    }
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

    System.out.println("Step 5: Waiting for Mersive Room Blocker window");
    String blockerHandle = WindowHelper.waitForBlockerWindow(60);

    Assert.assertNotNull(
            blockerHandle,
            "Mersive Room Blocker window not found"
    );

    System.out.println("Step 6: Attaching to Blocker window");
    attachByHandle(blockerHandle);
    setLastMeetingOverlayHandle(blockerHandle); // ADD

    return new MeetingOverlayPage(driver);
}

    @Test(priority = 35)
    public void TC_035_VerifyJoinZoomMeeting() throws Exception {

        System.out.println("TC_035 Started");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        Assert.assertTrue(
                overlay.isChatButtonVisible(),
                "Chat button not visible"
        );

        System.out.println("TC_035 PASSED");
    }


}