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

public class MeetingFlowTest extends BaseTest {

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

//        String originalHandle = getWindowHandle("Mersive Room");
//        preJoin.clickJoinMicrosoftTeamsMeeting();
//        switchToDesktop();
//
//        String newHandle = waitForNewWindow("Mersive Room", originalHandle, 100);
//        attachByHandle(newHandle);



//        preJoin.clickJoinMicrosoftTeamsMeeting();
//        switchToDesktop();
        preJoin.clickJoinMicrosoftTeamsMeeting();

        Thread.sleep(5000);

        if (preJoin.isJoinNowVisible()) {
            System.out.println("Join now screen appeared");
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

    @Test(priority = 27)
    public void TC_027_VerifyStayInMeetingCancelsLeave() throws Exception {
        System.out.println("=== TC_027: Stay in Meeting ===");

        MeetingOverlayPage overlay = joinMeeting();
        RootSessionPage root       = new RootSessionPage(driver);

        overlay.clickLeaveButton();
        System.out.println("✓ Leave button clicked");

        root.clickStayInMeeting();
        System.out.println("✓ Stay in meeting clicked");

        Assert.assertTrue(overlay.isChatButtonVisible(),
                "FAILED: No longer in meeting after Stay");
        System.out.println("✓ Still in meeting — chat visible");

        System.out.println("TC_027 PASSED");
    }

    @Test(priority = 28)
    public void TC_028_VerifyMeetingNameOnTopRibbonAfterJoining()
            throws Exception {
        System.out.println("=== TC_028: Meeting Name on Top Ribbon ===");

        MeetingCardPage cards = new MeetingCardPage(driver);
        String expectedName   = cards.getMeetingCardTitle();

        Assert.assertNotNull(expectedName, "Meeting card title not found");
        Assert.assertFalse(expectedName.trim().isEmpty(),
                "Meeting card title is empty");
        System.out.println("✓ Card title: " + expectedName);

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(overlay.waitForMeetingTitleVisible(),
                "Meeting title not visible on ribbon");

        String actualName = overlay.getInMeetingName();
        Assert.assertNotNull(actualName, "Ribbon name is null");

        String expected = expectedName.replaceAll("\\s+", " ").trim().toLowerCase();
        String actual   = actualName.replaceAll("\\s+", " ").trim().toLowerCase();

        System.out.println("Expected : " + expected);
        System.out.println("Actual   : " + actual);

        Assert.assertEquals(actual, expected,
                "Meeting name mismatch between card and ribbon");
        System.out.println("✓ Names matched");

        System.out.println("TC_028 PASSED");
    }

    @Test(priority = 29)
    public void TC_029_VerifyVirtualKeyboardAppearsWhenChatOpened()
            throws Exception {
        System.out.println("=== TC_029: Virtual Keyboard Appears ===");

        MeetingOverlayPage overlay = joinMeeting();
        RootSessionPage root       = new RootSessionPage(driver);

        Thread.sleep(3000);

        overlay.clickChatButton();
        System.out.println("✓ Chat panel opened");

        Thread.sleep(2000); // wait for chat panel to fully render

// Single method: clicks textbox AND checks keyboard in ONE Root Session
        Assert.assertTrue(
                root.clickChatMessageTextboxAndCheckKeyboard(),
                "FAILED: Virtual keyboard did not appear"
        );
        System.out.println("✓ Virtual keyboard visible");

        System.out.println("TC_029 PASSED");

        System.out.println("TC_029 PASSED");
    }
}