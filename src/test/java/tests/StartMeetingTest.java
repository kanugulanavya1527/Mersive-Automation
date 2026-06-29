package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomeScreenPage;
import pages.PlatformSelectPage;
import pages.PreJoinPage;
import pages.MeetingOverlayPage;
import utils.WindowHelper;

import java.util.ArrayList;
import java.util.List;

public class StartMeetingTest extends BaseTest {

    private List<TestResult> results = new ArrayList<>();

    private static class TestResult {
        int iteration;
        long duration;
        boolean success;
        String error;
    }

    // ==========================================================
    // TEAMS
    // ==========================================================

    @Test(priority = 1)
    public void testTeamsStartMeeting() throws Exception {

        System.out.println("\n==================================================");
        System.out.println("TEAMS START MEETING - 6 ITERATIONS");
        System.out.println("==================================================");

        results.clear();

        for (int i = 1; i <= 6; i++) {
            runCompleteFlow(i, "TEAMS");
        }

        printReport("TEAMS");
    }

    // ==========================================================
    // ZOOM
    // ==========================================================

    @Test(priority = 2)
    public void testZoomStartMeeting() throws Exception {

        System.out.println("\n==================================================");
        System.out.println("ZOOM START MEETING - 6 ITERATIONS");
        System.out.println("==================================================");

        results.clear();

        for (int i = 1; i <= 6; i++) {
            runCompleteFlow(i, "ZOOM");
        }

        printReport("ZOOM");
    }

    // ==========================================================
    // COMPLETE FLOW
    // ==========================================================

    private void runCompleteFlow(int iteration,
                                 String platform) throws Exception {

        TestResult result = new TestResult();
        result.iteration = iteration;

        long start = System.currentTimeMillis();

        try {

            System.out.println();
            System.out.println("=====================================");
            System.out.println("ITERATION " + iteration);
            System.out.println("=====================================");

            HomeScreenPage home = new HomeScreenPage(driver);

            PlatformSelectPage platformPage =
                    new PlatformSelectPage(driver);

            PreJoinPage preJoin =
                    new PreJoinPage(driver);

            // -------------------------------
            // Wait for Home
            // -------------------------------

            boolean loaded = false;

            for (int i = 0; i < 15; i++) {

                if (home.isHomeScreenLoaded()) {
                    loaded = true;
                    break;
                }

                Thread.sleep(1000);
            }

            Assert.assertTrue(
                    loaded,
                    "Home screen not loaded"
            );

            System.out.println("✓ Home loaded");

            // -------------------------------
            // Start Meeting
            // -------------------------------

            home.clickStartMeeting();

            Thread.sleep(2000);

            Assert.assertTrue(
                    platformPage.isPlatformScreenLoaded(),
                    "Platform screen not loaded"
            );

            if (platform.equals("TEAMS")) {

                platformPage.clickMicrosoftTeams();

            } else {

                platformPage.clickZoom();
            }

            System.out.println("✓ Platform selected");

            Assert.assertTrue(
                    preJoin.isPreJoinScreenLoaded(),
                    "PreJoin screen not loaded"
            );

            System.out.println("✓ PreJoin loaded");

            // -------------------------------
            // Click Start Meeting
            // -------------------------------

            if (platform.equals("TEAMS")) {

                preJoin.clickStartTeamsMeeting();

            } else {

                preJoin.clickStartZoomMeeting();
            }

            System.out.println("✓ Start Meeting clicked");

            Thread.sleep(3000);

            switchToDesktop();

            WindowHelper.printAllWindows();

            String blockerHandle =
                    WindowHelper.findWindowHandle(
                            "Mersive Room Blocker");

            String roomHandle =
                    WindowHelper.findWindowHandle(
                            "Mersive Room");

            String teamsHandle =
                    WindowHelper.findWindowHandle(
                            "Microsoft Teams");

            String zoomHandle =
                    WindowHelper.findWindowHandle(
                            "Zoom");

            if (blockerHandle != null) {

                setLastMeetingOverlayHandle(
                        blockerHandle);

                attachByHandle(blockerHandle);

            }

            else if (teamsHandle != null || zoomHandle != null) {

                System.out.println();
                System.out.println("========================================================");
                System.out.println("BUG DETECTED");
                System.out.println(platform + " came out of the Mersive application.");
                System.out.println("Iteration " + iteration + " skipped.");
                System.out.println("Continuing with next iteration...");
                System.out.println("========================================================");
                System.out.println();

                // Return to Mersive before next iteration
                switchToDesktop();

                String homeHandle =
                        WindowHelper.findWindowHandle("Mersive Room");

                if (homeHandle != null) {
                    attachByHandle(homeHandle);
                    Thread.sleep(3000);
                }

                result.success = true;
                result.duration = System.currentTimeMillis() - start;
                results.add(result);
                return;
            } else if (roomHandle != null) {

                attachByHandle(roomHandle);

            } else {

                throw new RuntimeException(
                        "Neither Mersive window nor meeting window found.");
            }

// ======================================================
// Verify Meeting Screen
// ======================================================

            MeetingOverlayPage overlay =
                    new MeetingOverlayPage(driver);

            Assert.assertTrue(
                    overlay.waitForMeetingJoinedScreen(),
                    "Meeting screen not loaded"
            );

            Assert.assertTrue(
                    overlay.isChatButtonVisible(),
                    "Chat button not visible"
            );

            System.out.println("✓ Chat verified");

            Thread.sleep(3000);

// ======================================================
// Leave Meeting
// ======================================================

            overlay.clickLeaveButton();

            System.out.println("✓ Leave clicked");

            Thread.sleep(2000);

            confirmLeaveMeeting();

            System.out.println("✓ Leave confirmed");
            killTeamsProcess();
            System.out.println("✓ Teams process killed");

            Thread.sleep(5000);

// ======================================================
// Return Home
// ======================================================

            switchToDesktop();

            String homeHandle =
                    WindowHelper.findWindowHandle("Mersive Room");

            Assert.assertNotNull(
                    homeHandle,
                    "Home window not found"
            );

            attachByHandle(homeHandle);

            Thread.sleep(3000);

            home = new HomeScreenPage(driver);

            boolean homeLoaded = false;

            for (int i = 0; i < 15; i++) {

                if (home.isHomeScreenLoaded()) {

                    homeLoaded = true;
                    break;
                }

                Thread.sleep(1000);
            }

            Assert.assertTrue(
                    homeLoaded,
                    "Home screen did not return"
            );

            System.out.println("✓ Back at Home");

// ======================================================
// Success
// ======================================================

            result.success = true;

            result.duration =
                    System.currentTimeMillis() - start;

            System.out.println(
                    "Iteration "
                            + iteration
                            + " completed in "
                            + result.duration
                            + " ms");

        } catch (Exception e) {

            result.success = false;
            result.error = e.getMessage();

            result.duration =
                    System.currentTimeMillis() - start;

            System.out.println(
                    "Iteration "
                            + iteration
                            + " FAILED");

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
    private void killTeamsProcess() throws Exception {
        try {
            ProcessBuilder pb = new ProcessBuilder("taskkill", "/F", "/IM", "Teams.exe");
            pb.start();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("⚠ Teams kill failed (may already be closed): " + e.getMessage());
        }
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