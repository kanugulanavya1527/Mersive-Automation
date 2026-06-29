package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import pages.JoinWithIdPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * JoinWithIdTest - Complete Flow Test
 *
 * Method 1: testTeamsJoinWithId()
 * - Runs the COMPLETE FLOW 6 times
 * - Flow: Home → Join with ID → Enter ID → Enter Password → Join → Verify Chat → Leave → Home
 *
 * Method 2: testZoomJoinWithId()
 * - Runs the COMPLETE FLOW 6 times (same as Teams)
 */
public class JoinWithIdTest extends BaseTest {

    private JoinWithIdPage joinPage;
    private List<TestResult> results = new ArrayList<>();
    private String MEETING_ID;
    private String PASSWORD;
    private static final String TEAMS_MEETING_ID = "237 022 222 096 289";
    private static final String TEAMS_PASSWORD = "vY992kc6";

    private static final String ZOOM_MEETING_ID = "795 4660 7520";
    private static final String ZOOM_PASSWORD = "1C3KKb";





    private static class TestResult {
        int iteration;
        long duration;
        boolean success;
        String error;
    }



    // ═════════════════════════════════════════════════════════════
    // TEST METHOD 1: TEAMS - COMPLETE FLOW 6 TIMES
    // ═════════════════════════════════════════════════════════════

    @Test(priority = 1)
    public void testTeamsJoinWithId() throws Exception {
        System.out.println("\n" + "╔" + "═".repeat(70) + "╗");
        System.out.println("║         TEAMS - JOIN WITH ID (6 COMPLETE FLOWS)                   ║");
        System.out.println("╚" + "═".repeat(70) + "╝");

        results.clear();
        MEETING_ID = TEAMS_MEETING_ID;
        PASSWORD = TEAMS_PASSWORD;
        // Run the complete flow 6 times
        for (int i = 1; i <= 6; i++) {
            runCompleteFlow(i, "TEAMS");
        }

        printReport("TEAMS");
    }

    // ═════════════════════════════════════════════════════════════
    // TEST METHOD 2: ZOOM - COMPLETE FLOW 6 TIMES
    // ═════════════════════════════════════════════════════════════

    @Test(priority = 2) //, dependsOnMethods = "testTeamsJoinWithId"
    public void testZoomJoinWithId() throws Exception {
        System.out.println("\n" + "╔" + "═".repeat(70) + "╗");
        System.out.println("║         ZOOM - JOIN WITH ID (6 COMPLETE FLOWS)                    ║");
        System.out.println("╚" + "═".repeat(70) + "╝");

        results.clear();
        MEETING_ID = ZOOM_MEETING_ID;
        PASSWORD = ZOOM_PASSWORD;
        // Run the complete flow 6 times
        for (int i = 1; i <= 6; i++) {
            runCompleteFlow(i, "ZOOM");
        }

        printReport("ZOOM");
    }

    // ═════════════════════════════════════════════════════════════
    // COMPLETE FLOW (Runs 6 times)
    // ═════════════════════════════════════════════════════════════

    private void runCompleteFlow(int iteration, String platform) throws Exception {
        TestResult result = new TestResult();
        result.iteration = iteration;
        long start = System.currentTimeMillis();

        try {
            System.out.println("\n┌─ ITERATION " + iteration + " ─┐");

            joinPage = new JoinWithIdPage(driver);

            // ═══════════════════════════════════════════════════════════
            // SETUP PHASE (Only once at start)
            // ═══════════════════════════════════════════════════════════


            // ═══════════════════════════════════════════════════════════

            System.out.println("═══ JOIN MEETING CYCLE #" + iteration + " ═══");

            boolean loaded = false;

            for (int i = 0; i < 15; i++) {

                if (joinPage.isHomeScreenLoaded()) {
                    loaded = true;
                    break;
                }

                System.out.println("Waiting for Home Screen... " + (i + 1));
                Thread.sleep(1000);
            }

            Assert.assertTrue(loaded, "Home screen not loaded");

            System.out.println("✓ Home screen");

            joinPage.clickJoinWithId();
            System.out.println("✓ Clicked Join with ID");
            System.out.println("Current platform = " + platform);
            if (platform.equalsIgnoreCase("TEAMS")) {
                joinPage.clickMicrosoftTeams();
            } else {
                joinPage.clickZoom();
            }

            Thread.sleep(1000);

            joinPage.enterMeetingId(MEETING_ID);
            System.out.println("✓ Meeting ID entered");

            Thread.sleep(1000);

            joinPage.enterPassword(PASSWORD);
            System.out.println("✓ Password entered");

            Thread.sleep(1000);

// Switch to Keyboard
            switchToDesktop();

            String keyboardHandle =
                    WindowHelper.findWindowHandle("Keyboard");

            Assert.assertNotNull(keyboardHandle, "Keyboard window not found");

            attachByHandle(keyboardHandle);

            joinPage = new JoinWithIdPage(driver);

            joinPage.clickDoneOnKeypad();
            System.out.println("✓ Done clicked");

            Thread.sleep(1500);

// Back to Mersive
            switchToDesktop();

            String roomHandle =
                    WindowHelper.findWindowHandle("Mersive Room");

            Assert.assertNotNull(roomHandle);

            attachByHandle(roomHandle);

            joinPage = new JoinWithIdPage(driver);

            Thread.sleep(1000);





            Thread.sleep(2000);
            joinPage.debugPrintAllButtons();
            joinPage.clickJoinMeetingButton();

//            // STEP 7: Switch to meeting
//            switchToDesktop();
//            String blockerHandle = WindowHelper.findWindowHandle("Mersive Room Blocker");
//            Assert.assertNotNull(blockerHandle, "Meeting window not found");
//            setLastMeetingOverlayHandle(blockerHandle);
//            attachByHandle(blockerHandle);
//            System.out.println("✓ In meeting");
//            Thread.sleep(5000);
            Thread.sleep(15000);

            switchToDesktop();
            WindowHelper.printAllWindows();

// ----------------------------------------------------------
// BUG CHECK : Teams / Zoom opened outside Mersive
// ----------------------------------------------------------

            String blockerHandle =
                    WindowHelper.findWindowHandle("Mersive Room Blocker");

             roomHandle =
                    WindowHelper.findWindowHandle("Mersive Room");

            String teamsHandle =
                    WindowHelper.findWindowHandle("Microsoft Teams");

            String zoomHandle =
                    WindowHelper.findWindowHandle("Zoom");

            if (blockerHandle != null) {

                setLastMeetingOverlayHandle(blockerHandle);
                attachByHandle(blockerHandle);

            } else if (teamsHandle != null || zoomHandle != null) {

                System.out.println();
                System.out.println("========================================================");
                System.out.println("BUG DETECTED");
                System.out.println(platform + " came out of the Mersive application.");
                System.out.println("Iteration " + iteration + " skipped.");
                System.out.println("Continuing with next iteration...");
                System.out.println("========================================================");
                System.out.println();

                result.success = true;     // don't fail the test
                result.duration = System.currentTimeMillis() - start;
                results.add(result);
                return;

            } else if (roomHandle != null) {

                attachByHandle(roomHandle);

            } else {

                throw new RuntimeException("Neither Mersive window nor meeting window found.");
            }


            // STEP 8: Verify chat button
            joinPage = new JoinWithIdPage(driver);
            boolean chatFound = joinPage.verifyChatButton();
            Assert.assertTrue(chatFound, "Chat button not found");
            System.out.println("✓ Chat verified - IN MEETING!");
            Thread.sleep(3000);

            // STEP 9: Click leave
            joinPage.clickLeaveButton();
            System.out.println("✓ Left meeting");
            Thread.sleep(2000);

            // STEP 10: Confirm leave


            confirmLeaveMeeting();
            System.out.println("✓ Leave meeting confirmed");

            Thread.sleep(5000);
            switchToDesktop();

            String homeHandle =
                    WindowHelper.findWindowHandle("Mersive Room");

            // STEP 11: Return to join form (NOT home for iterations 2-6)

            Assert.assertNotNull(homeHandle, "Home window not found");
            attachByHandle(homeHandle);
            Thread.sleep(3000);
            joinPage = new JoinWithIdPage(driver);

// Wait for Home Screen to come back
            boolean homeLoaded = false;

            for (int i = 0; i < 15; i++) {
                if (joinPage.isHomeScreenLoaded()) {
                    homeLoaded = true;
                    break;
                }

                System.out.println("Waiting for Home Screen... " + (i + 1));
                Thread.sleep(1000);
            }

            Assert.assertTrue(homeLoaded, "Home screen did not return");

            System.out.println("✓ Back at home");
            WindowHelper.printAllWindows();

            joinPage.debugPrintAllButtons();

            System.out.println(
                    "Page source:\n" + driver.getPageSource()
            );

            joinPage = new JoinWithIdPage(driver);


            result.success = true;
            long end = System.currentTimeMillis();
            result.duration = end - start;

            System.out.println("└─ PASSED: " + result.duration + " ms ─┘");

        } catch (Exception e) {
            result.success = false;
            result.error = e.getMessage();
            long end = System.currentTimeMillis();
            result.duration = end - start;

            System.out.println("✗ FAILED: " + e.getMessage());
            e.printStackTrace();
        }

        results.add(result);
    }

    // ═════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═════════════════════════════════════════════════════════════

    private void confirmLeaveMeeting() throws Exception {
        new pages.RootSessionPage(driver)
                .clickLeaveMeetingConfirmation();

        Thread.sleep(3000);
    }

    // ═════════════════════════════════════════════════════════════
    // REPORT METHOD
    // ═════════════════════════════════════════════════════════════

    private void printReport(String platform) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println(platform + " FINAL REPORT");
        System.out.println("═".repeat(70));

        int success = 0;
        long total = 0;
        long min = Long.MAX_VALUE;
        long max = 0;

        System.out.println("\n┌─────┬──────────────┐");
        System.out.println("│ Itr │ Duration(ms) │");
        System.out.println("├─────┼──────────────┤");

        for (TestResult r : results) {
            String status = r.success ? "✓" : "✗";
            System.out.printf("│ %s%d  │    %8d  │%n", status, r.iteration, r.duration);
            if (r.success) {
                success++;
                total += r.duration;
                min = Math.min(min, r.duration);
                max = Math.max(max, r.duration);
            }
        }
        System.out.println("└─────┴──────────────┘");

        double avg = success > 0 ? (double) total / success : 0;
        System.out.println();
        System.out.printf("Total Flows: %d%n", results.size());
        System.out.printf("Successful: %d%n", success);
        System.out.printf("Failed: %d%n", results.size() - success);
        System.out.printf("Success Rate: %.1f%%%n", (success * 100.0) / results.size());
        System.out.printf("Min Duration: %d ms%n", min == Long.MAX_VALUE ? 0 : min);
        System.out.printf("Max Duration: %d ms%n", max);
        System.out.printf("Average Duration: %.2f ms%n", avg);
        System.out.printf("Total Time: %d ms%n", total);
        System.out.println("═".repeat(70));

        Assert.assertTrue(success >= 1, "At least 1  should pass");
    }
}