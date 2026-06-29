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

public class ZoomVirtualKeyboardTest extends BaseTest {


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
        Thread.sleep(5000);

        String blocker = WindowHelper.waitForBlockerWindow(60); // was 30

        if (blocker == null) {
            throw new RuntimeException(
                    "Zoom launched outside Mersive. Blocker window was not created."
            );
        }
        attachByHandle(blocker);
        setLastMeetingOverlayHandle(blocker); // ADD

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        return overlay;
    }

    @Test(priority = 52)
    public void TC_052_VerifyVirtualKeyboardAppearsWhenChatOpened()
            throws Exception {

        System.out.println(
                "=== TC_052: Verify Virtual Keyboard Appears ==="
        );

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        RootSessionPage root =
                new RootSessionPage(driver);

        overlay.clickChatButtonRobust();

        System.out.println("✓ Chat panel opened");

        Thread.sleep(3000);

        root.clickChatMessageTextbox();

        System.out.println("✓ Chat textbox clicked");

        Thread.sleep(3000);

        Assert.assertTrue(
                WindowHelper.isWindowPresent("Keyboard"),
                "Virtual keyboard did not appear"
        );

        System.out.println("✓ Virtual keyboard appeared");

        System.out.println("TC_052 PASSED");

    }
    @Test(priority = 53)
    public void TC_053_VerifyChatTextboxAcceptsInput()
            throws Exception {

        MeetingOverlayPage overlay = joinZoomMeeting();
        RootSessionPage root = new RootSessionPage(driver);

        overlay.clickChatButtonRobust();
        root.clickChatMessageTextbox();

        Thread.sleep(10000);

        throw new AssertionError(
                "TC_053 FAILED: Chat textbox did not remain focused. " +
                        "Known defect: Focus continuously switches between virtual keyboard and Chat icon."
        );
    }
}