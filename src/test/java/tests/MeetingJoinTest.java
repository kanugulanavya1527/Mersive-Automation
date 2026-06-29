package tests;

import base.BaseTest;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

public class MeetingJoinTest extends BaseTest {

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
        System.out.println("✓ Pre-join screen loaded");

        preJoin.clickJoinMicrosoftTeamsMeeting();
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room Blocker");

        if (blockerHandle == null) {
            throw new RuntimeException(
                    "Mersive Room Blocker not found.");
        }

        setLastMeetingOverlayHandle(blockerHandle);
        attachByHandle(blockerHandle);

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);

        Thread.sleep(10000);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load"
        );

        System.out.println("✓ Meeting joined");
        return overlay;
    }

    // ── Tests ──────────────────────────────────────────────

    @Test(priority = 12)
    public void TC_012_VerifyJoinMeetingAndValidateInMeetingScreen()
            throws Exception {
        System.out.println("=== TC_012: Join Meeting and Validate Screen ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.isChatButtonVisible(),
                "TC_012 FAILED: Chat button not visible after joining"
        );

        System.out.println("TC_012 PASSED: Meeting overlay loaded, chat visible");
    }

    @Test(priority = 13)
    public void TC_013_ClickChatButtonFromMeetingOverlay() throws Exception {
        System.out.println("=== TC_013: Click Chat Button ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "TC_013 FAILED: Chat button not ready"
        );
        System.out.println("✓ Chat button ready");

        overlay.clickChatButtonRobust();
        System.out.println("✓ Chat button clicked");

        Thread.sleep(3000);

        System.out.println("TC_013 PASSED");
    }

    @Test(priority = 14)
    public void TC_014_ClickPeopleButtonFromMeetingOverlay() throws Exception {
        System.out.println("=== TC_014: Click People Button ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "TC_014 FAILED: People button not visible"
        );
        System.out.println("✓ People button ready");

        overlay.clickPeopleButton();
        System.out.println("✓ People button clicked");

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "TC_014 FAILED: People panel did not open"
        );

        System.out.println("TC_014 PASSED: People panel opened");
    }
}