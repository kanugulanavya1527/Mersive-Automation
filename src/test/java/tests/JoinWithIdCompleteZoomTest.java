// ═════════════════════════════════════════════════════════════════
// FILE 2: JoinWithIdCompleteZoomTest.java
// ═════════════════════════════════════════════════════════════════

package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import pages.PlatformSelectPage;
import pages.PreJoinPage;
import pages.MeetingOverlayPage;
import pages.RootSessionPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

import java.util.ArrayList;
import java.util.List;

public class JoinWithIdCompleteZoomTest extends BaseTest {

    private static final String MEETING_ID = "249 434 512 849 474";
    private static final String MEETING_PASSWORD = "nk7vi9ap";

    private List<TestResult> results = new ArrayList<>();

    private static class TestResult {
        int iteration;
        long startTime;
        long endTime;
        long duration;
        boolean success;
        String error;
    }

    @Test(priority = 1)
    public void TC_Zoom_001() throws Exception {
        header(1);
        run(1);
    }

    @Test(priority = 2, dependsOnMethods = "TC_Zoom_001")
    public void TC_Zoom_002() throws Exception {
        header(2);
        run(2);
    }

    @Test(priority = 3, dependsOnMethods = "TC_Zoom_002")
    public void TC_Zoom_003() throws Exception {
        header(3);
        run(3);
    }

    @Test(priority = 4, dependsOnMethods = "TC_Zoom_003")
    public void TC_Zoom_004() throws Exception {
        header(4);
        run(4);
    }

    @Test(priority = 5, dependsOnMethods = "TC_Zoom_004")
    public void TC_Zoom_005() throws Exception {
        header(5);
        run(5);
    }

    @Test(priority = 6, dependsOnMethods = "TC_Zoom_005")
    public void TC_Zoom_006() throws Exception {
        header(6);
        run(6);
    }

    @Test(priority = 7, dependsOnMethods = "TC_Zoom_006")
    public void TC_Zoom_007_Report() throws Exception {
        report();
    }

    private void run(int i) throws Exception {
        TestResult r = new TestResult();
        r.iteration = i;
        r.startTime = System.currentTimeMillis();

        try {
            System.out.println("\n[1] HOME SCREEN");
            HomeScreenPage h = new HomeScreenPage(driver);
            Assert.assertTrue(h.isHomeScreenLoaded());
            System.out.println("✓ Home loaded");
            Thread.sleep(1000);

            System.out.println("\n[2] CLICK JOIN WITH ID");
            h.clickJoinWithId();
            System.out.println("✓ Clicked");
            Thread.sleep(2000);

            System.out.println("\n[3] SELECT ZOOM");
            PlatformSelectPage p = new PlatformSelectPage(driver);
            Assert.assertTrue(p.isPlatformScreenLoaded());
            p.clickZoom();
            System.out.println("✓ Zoom selected");
            Thread.sleep(2000);

            System.out.println("\n[4] PRE-JOIN SCREEN");
            PreJoinPage pre = new PreJoinPage(driver);
            Assert.assertTrue(pre.isPreJoinScreenLoaded());
            System.out.println("✓ Pre-join loaded");
            Thread.sleep(1000);

            System.out.println("\n[5] ENTER MEETING ID");
            enterID(MEETING_ID);
            System.out.println("✓ ID entered: " + MEETING_ID);
            Thread.sleep(500);

            System.out.println("\n[6] ENTER PASSWORD");
            enterPass(MEETING_PASSWORD);
            System.out.println("✓ Password entered: " + MEETING_PASSWORD);
            Thread.sleep(500);

            System.out.println("\n[7] CLICK JOIN MEETING");
            Assert.assertTrue(pre.isJoinNowVisible());
            pre.clickJoinNow();
            System.out.println("✓ Join clicked");
            Thread.sleep(3000);

            System.out.println("\n[8] SWITCH TO MEETING");
            switchToDesktop();
            String handle = WindowHelper.findWindowHandle("Mersive Room Blocker");
            Assert.assertNotNull(handle);
            setLastMeetingOverlayHandle(handle);
            attachByHandle(handle);
            System.out.println("✓ In meeting overlay");
            Thread.sleep(2000);

            System.out.println("\n[9] VERIFY CHAT BUTTON");
            MeetingOverlayPage m = new MeetingOverlayPage(driver);
            Thread.sleep(5000);
            Assert.assertTrue(m.waitForMeetingJoinedScreen());
            System.out.println("✓ Meeting loaded");
            Thread.sleep(2000);
            Assert.assertTrue(m.isChatButtonVisible());
            System.out.println("✓ Chat visible - IN MEETING!");
            Thread.sleep(3000);

            System.out.println("\n[10] CLICK LEAVE");
            m.clickLeaveButton();
            System.out.println("✓ Leave clicked");
            Thread.sleep(2000);

            System.out.println("\n[11] CONFIRM LEAVE");
            RootSessionPage root = new RootSessionPage(driver);
            root.clickLeaveMeetingConfirmation();
            System.out.println("✓ Confirmed leave");
            Thread.sleep(3000);

            System.out.println("\n[12] BACK TO HOME");
            String hh = WindowHelper.findWindowHandle("Mersive Room");
            Assert.assertNotNull(hh);
            attachByHandle(hh);
            Thread.sleep(2000);
            HomeScreenPage hc = new HomeScreenPage(driver);
            Assert.assertTrue(hc.isHomeScreenLoaded());
            System.out.println("✓ Back at home");

            r.success = true;
            r.endTime = System.currentTimeMillis();
            r.duration = r.endTime - r.startTime;

            System.out.println("\n✅ ITERATION " + i + " PASSED: " + r.duration + " ms");

        } catch (Exception e) {
            r.success = false;
            r.error = e.getMessage();
            r.endTime = System.currentTimeMillis();
            r.duration = r.endTime - r.startTime;
            System.out.println("\n❌ ITERATION " + i + " FAILED: " + e.getMessage());
        } finally {
            results.add(r);
        }
    }

    private void enterID(String id) throws Exception {
        By b = By.xpath("//input[@placeholder*='ID']");
        WebElement e = driver.findElement(b);
        e.clear();
        e.sendKeys(id);
    }

    private void enterPass(String pass) throws Exception {
        By b = By.xpath("//input[@type='password']");
        WebElement e = driver.findElement(b);
        e.clear();
        e.sendKeys(pass);
    }

    private void header(int i) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("ZOOM ITERATION " + i + " OF 6");
        System.out.println("═".repeat(70));
    }

    private void report() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("FINAL REPORT - ZOOM");
        System.out.println("═".repeat(70));

        long total = 0;
        int success = 0;
        long min = Long.MAX_VALUE;
        long max = 0;

        System.out.println("\n┌─────┬──────────────┐");
        System.out.println("│ Itr │ Duration(ms) │");
        System.out.println("├─────┼──────────────┤");

        for (TestResult r : results) {
            System.out.printf("│ %2d  │    %8d  │%n", r.iteration, r.duration);
            if (r.success) {
                total += r.duration;
                success++;
                min = Math.min(min, r.duration);
                max = Math.max(max, r.duration);
            }
        }
        System.out.println("└─────┴──────────────┘");

        double avg = success > 0 ? (double) total / success : 0;

        System.out.println("\nSTATISTICS:");
        System.out.printf("Total: %d, Success: %d, Failed: %d%n", results.size(), success, results.size() - success);
        System.out.printf("Success Rate: %.1f%%%n", (success * 100.0) / results.size());
        System.out.printf("Min: %d ms, Max: %d ms, Avg: %.2f ms%n", min, max, avg);
        System.out.printf("Total Time: %d ms%n", total);

        Assert.assertTrue(success >= 5);
    }
}