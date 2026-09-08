package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import pages.MeetingOverlayPage;
import pages.RootSessionPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ManualCheckpoint;
import utils.WindowHelper;

/**3
 * ScreenKeySyncTest - TC_1a: Screen key sync
 *
 * REWRITTEN based on confirming the real setup with Sachin (2026-09-07):
 * the Screen Key shown inside the tablet's own Kiosk app is a static/dummy
 * value -- it is NOT the real key and never changes. The real Screen Key
 * that rotates lives on the physical Pod display (e.g. "Conference Room-3"
 * monitor), which is a separate physical screen outside anything
 * WinAppDriver/Selenium can read (it isn't part of the app window this
 * framework attaches to).
 *
 * Because the expected result is only visible on a physical display, this
 * cannot be a fully automated test -- it follows the same "automate the
 * action, pause for a human to judge the real-world result" pattern as
 * DisconnectFromCallTest (TC_3m) and the 3a-3h scenarios. No ShareControlsPage
 * locators are used/needed here anymore -- that page's Share/Disconnect
 * button guesses were placeholders for a mechanism that turned out not to be
 * how this scenario actually works.
 *
 * Flow (confirmed with Sachin): join a meeting -> a remote participant
 * shares their screen then stops sharing -> the call ends -> tablet returns
 * to its home/idle screen -> the Pod's physical Screen Key should now be
 * different from what it was before the test started.
 */
public class ScreenKeySyncTest extends BaseTest {

    @Test(priority = 1)
    public void TC_1a_VerifyScreenKeySyncsOnShareAndDisconnect() throws Exception {

        System.out.println("=== TC_1a: Screen Key Sync ===");

        // ── Manual step: capture the "before" state ──────────────────────
        // Automation cannot read the Pod's physical Screen Key, so a human
        // has to note it before anything else happens.
        ManualCheckpoint.pause(
                "TC_1a",
                "Look at the physical Pod display (e.g. 'Conference Room-3'). " +
                        "Note down the Screen Key currently shown there -- you'll be " +
                        "asked to compare it again at the end of this test."
        );

        // ── Automated action: join the meeting ────────────────────────────
        MeetingOverlayPage overlay = joinMeeting();
        System.out.println("✓ Meeting joined");

        // ── Manual step: remote participant shares then stops sharing ─────
        // This needs a second, real Teams client on another device -- not
        // something this framework can drive from the tablet alone.
        ManualCheckpoint.pause(
                "TC_1a",
                "On the REMOTE participant's device (a second machine already " +
                        "in this same call), start sharing their screen, then stop " +
                        "sharing. Do this now, then press ENTER to continue."
        );

        // ── Automated action: leave/end the call ──────────────────────────
        overlay.clickLeaveButton();
        System.out.println("✓ Leave button clicked");

        new RootSessionPage(driver).clickLeaveMeetingConfirmation();
        System.out.println("✓ Leave confirmed");

        Thread.sleep(5000);

        // ── Automated checkpoint: tablet returns to its normal home screen ─
        switchToDesktop();
        String homeHandle = WindowHelper.findWindowHandle("Mersive Room");
        Assert.assertNotNull(homeHandle,
                "TC_1a FAILED: Home/canvas window not found after call ended");
        attachByHandle(homeHandle);

        HomeScreenPage home = new HomeScreenPage(driver);
        boolean homeLoaded = false;
        for (int i = 0; i < 15; i++) {
            if (home.isHomeScreenLoaded()) {
                homeLoaded = true;
                break;
            }
            System.out.println("Waiting for Home Screen... " + (i + 1));
            Thread.sleep(1000);
        }
        Assert.assertTrue(homeLoaded,
                "TC_1a FAILED: Did not return to the home screen after the call ended");
        System.out.println("✓ Tablet back at home screen after call ended");

        // ── Manual checkpoint: only a human can compare the Pod's real key ─
        boolean manualResult = ManualCheckpoint.confirm(
                "TC_1a",
                "Look at the physical Pod display's Screen Key again. Has it " +
                        "changed compared to the value you noted at the start of " +
                        "this test?"
        );

        Assert.assertTrue(manualResult,
                "TC_1a FAILED: manual check -- Pod Screen Key did not change after share/disconnect");

        System.out.println("TC_1a PASSED");
    }
}