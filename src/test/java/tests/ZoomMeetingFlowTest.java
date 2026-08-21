package tests;
import base.BaseTest;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import pages.RootSessionPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

import static base.BasePage.driver;

public class ZoomMeetingFlowTest extends  BaseTest {
    public MeetingOverlayPage joinZoomMeeting() throws Exception {

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

        String blocker = WindowHelper.waitForBlockerWindow(60);

        if (blocker == null) {
            throw new RuntimeException(
                    "BUG: Zoom launched outside Mersive. Mersive Room Blocker window was not created."
            );
        }

        attachByHandle(blocker);
        setLastMeetingOverlayHandle(blocker);

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);

        Thread.sleep(15000);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load"
        );

        System.out.println("✓ Zoom meeting joined");

        return overlay;
    }
    @Test(priority = 58)
    public void TC_058_VerifyStayInMeetingCancelsLeave() throws Exception {

        System.out.println("=== TC_058: Stay in Meeting ===");

        MeetingOverlayPage overlay = joinZoomMeeting();
        RootSessionPage root = new RootSessionPage(driver);

        overlay.clickLeaveButton();
        System.out.println("✓ Leave button clicked");

        root.clickStayInMeeting();
        System.out.println("✓ Stay in Meeting clicked");

        Assert.assertTrue(
                overlay.isChatButtonVisible(),
                "FAILED: No longer in meeting after Stay");

        System.out.println("✓ Still in meeting");
        System.out.println("TC_058 PASSED");
    }
    @Test(priority = 59)
    public void TC_059_VerifyMeetingNameOnTopRibbonAfterJoining()
            throws Exception {

        System.out.println("=== TC_059: Meeting Name ===");

        MeetingCardPage cards = new MeetingCardPage(driver);

        // Read meeting title from Zoom meeting card
        cards.clickJoinForFirstZoomMeeting();
        String expectedName = cards.getMeetingCardTitle();

        Assert.assertNotNull(expectedName, "Meeting card title not found");
        Assert.assertFalse(expectedName.trim().isEmpty(),
                "Meeting card title is empty");

        System.out.println("Expected Card Title : " + expectedName);

        // Join Zoom meeting
        MeetingOverlayPage overlay = joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForMeetingTitleVisible(),
                "Meeting title not visible on top ribbon");

        String actualName = overlay.getInMeetingName();

        Assert.assertNotNull(actualName, "Top ribbon meeting title is null");

        String expected = expectedName.replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();

        String actual = actualName.replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();

        System.out.println("Expected : " + expected);
        System.out.println("Actual   : " + actual);

        Assert.assertEquals(
                actual,
                expected,
                "Meeting name mismatch between Zoom card and top ribbon");

        System.out.println("✓ Meeting name verified");
        System.out.println("TC_059 PASSED");
    }
    @Test(priority = 60)
    public void TC_060_VerifyVirtualKeyboardAppearsWhenChatOpened()
            throws Exception {

        System.out.println("=== TC_060: Virtual Keyboard ===");

        MeetingOverlayPage overlay = joinZoomMeeting();
        RootSessionPage root = new RootSessionPage(driver);

        Thread.sleep(3000);

        overlay.clickChatButton();

        System.out.println("✓ Chat opened");

        Thread.sleep(2000);

        Assert.assertTrue(
                root.clickChatMessageTextboxAndCheckKeyboard(),
                "FAILED: Virtual keyboard did not appear"
        );

        System.out.println("✓ Virtual keyboard appeared");
        System.out.println("TC_060 PASSED");
    }
}
