package base;

import pages.MeetingOverlayPage;
import pages.RootSessionPage;
import utils.ProcessHelper;
import utils.WindowHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected RemoteWebDriver driver;
    private Process appProcess;

    /** Overlay window of the CURRENT test, if it joined a meeting. Used by teardown. */
    private String lastMeetingOverlayHandle;

    private static final String APP_PATH =
            "C:\\Program Files\\MersiveRoom\\MersiveRoom.exe";

    /** The in-meeting "Leave" button — its presence means we're inside a meeting. */
    private static final By LEAVE_BUTTON =
            By.xpath("//Button[.//Text[@Name='Leave']]");

    // ── Lifecycle ───────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)

    public void setup() throws Exception {

        System.out.println("\n=== SETUP START ===");
        ProcessHelper.kill("Teams.exe");
        ProcessHelper.kill("ms-teams.exe");
        ProcessHelper.kill("Zoom.exe");
        ProcessHelper.kill("MersiveRoom.exe");
        ProcessHelper.kill("msedgewebview2.exe");
        Thread.sleep(5000);

        lastMeetingOverlayHandle = null;
        appProcess = ProcessHelper.launch(APP_PATH);
        Thread.sleep(10000);
        String decimalHandle = WindowHelper.findWindowHandle("Mersive Room");
        if (decimalHandle == null)
            throw new RuntimeException("Mersive Room window not found after launch");
        driver = DriverFactory.attachByHexHandle(
                WindowHelper.toHexHandle(decimalHandle));

        // Self-heal: if the app came up already inside a meeting (a previous test's
        // cleanup didn't fully release the room), leave it so this test starts from
        // a clean home screen with a joinable meeting card.
        recoverIfInMeeting();

        System.out.println("=== SETUP COMPLETE ===\n");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        System.out.println("\n=== TEARDOWN START ===");
        leaveActiveMeeting();
        Thread.sleep(3000);
        // release the room before killing the app
        if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
            driver = null;
        }
        ProcessHelper.kill("Zoom.exe");
        ProcessHelper.kill("MersiveRoom.exe");
        ProcessHelper.kill("msedgewebview2.exe");

        ProcessHelper.destroy(appProcess);

        Thread.sleep(3000);

        ProcessHelper.kill("Zoom.exe");
        ProcessHelper.kill("MersiveRoom.exe");
        // ProcessHelper.kill("msedgewebview2.exe");

        System.out.println("=== TEARDOWN COMPLETE ===\n");
    }

    /**
     * If the freshly launched app is sitting in a meeting overlay, leave the
     * meeting and return to the home screen. Cheap no-op when not in a meeting
     * (uses a 0s implicit wait so the check returns instantly).
     */
    private void recoverIfInMeeting() {
        boolean inMeeting;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            inMeeting = !driver.findElements(LEAVE_BUTTON).isEmpty();
        } catch (Exception e) {
            return;
        } finally {
            try { driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); }
            catch (Exception ignored) {}
        }

        if (!inMeeting) return;

        try {
            System.out.println("[BaseTest] App launched inside a meeting — leaving to recover...");
            new MeetingOverlayPage(driver).clickLeaveButton();
            new RootSessionPage(driver).clickLeaveMeetingConfirmation();
            Thread.sleep(3000);
            String home = WindowHelper.findWindowHandle("Mersive Room");
            if (home != null) {
                attachByHandle(home);
                System.out.println("[BaseTest] Recovered to home screen");
            }
        } catch (Exception e) {
            System.out.println("[BaseTest] Recovery skipped: " + e.getMessage());
        }
    }

    /**
     * Best-effort graceful leave of the meeting this test joined (mirrors TC_018):
     * click Leave on the overlay, then confirm via the root session.
     */
//    private void leaveActiveMeeting() {
//        if (lastMeetingOverlayHandle == null) return;
//        try {
//            attachByHandle(lastMeetingOverlayHandle);
//            new MeetingOverlayPage(driver).clickLeaveButton();
//            new RootSessionPage(driver).clickLeaveMeetingConfirmation();
//            Thread.sleep(2500);
//            System.out.println("[BaseTest] Meeting left — room released");
//        } catch (Exception e) {
//            System.out.println("[BaseTest] Leave skipped (best-effort): " + e.getMessage());
//        } finally {
//            lastMeetingOverlayHandle = null;
//        }
//    }
    protected void switchBackToMeetingOverlay() throws Exception {

        attachByHandle(lastMeetingOverlayHandle);

    }

    private void leaveActiveMeeting() {
        if (lastMeetingOverlayHandle == null) return;

        try {
            attachByHandle(lastMeetingOverlayHandle);

            new MeetingOverlayPage(driver).clickLeaveButton();
            new RootSessionPage(driver)
                    .clickLeaveMeetingConfirmation();


            Thread.sleep(3000);
            ProcessHelper.kill("Zoom.exe");

            System.out.println(
                    "[BaseTest] Meeting left — room released"
            );

        } catch (Exception e) {
            System.out.println(
                    "[BaseTest] Leave skipped (best-effort): "
                            + e.getMessage()
            );
        } finally {
            lastMeetingOverlayHandle = null;
        }
    }
    // ── Window Helpers (used by all tests) ──────────────────

    protected void switchToDesktop() throws Exception {
        driver = DriverFactory.createRootSession();
    }

    protected void attachByHandle(String decimalHandle)
            throws Exception {

        if (decimalHandle == null) {
            throw new RuntimeException("Window handle is null.");
        }

        Exception lastException = null;

        for (int attempt = 1; attempt <= 5; attempt++) {

            try {

                driver = DriverFactory.attachByHexHandle(
                        WindowHelper.toHexHandle(decimalHandle));

                System.out.println("✓ Reattached to Mersive Room");
                return;

            } catch (Exception e) {

                lastException = e;

                System.out.println(
                        "[BaseTest] Attach attempt "
                                + attempt
                                + " failed. Retrying..."
                );

                Thread.sleep(1500);
            }
        }

        throw lastException;
    }


    protected String getWindowHandle(String titleContains)
            throws InterruptedException {
        return WindowHelper.findWindowHandle(titleContains);
    }

    protected String waitForNewWindow(String title,
                                      String originalHandle,
                                      int timeoutSeconds)
            throws InterruptedException {
        String handle = WindowHelper.waitForNewWindow(
                title,
                originalHandle,
                timeoutSeconds,
                "Controls",
                "Blocker"
        );

        if (title != null && title.contains("Mersive Room")) {
            lastMeetingOverlayHandle = handle;
        }

        return handle;
    }

    protected void attachToControlsWindow() throws Exception {
        String h = WindowHelper.findWindowHandle("Mersive Room Controls");
        if (h == null) throw new RuntimeException("Controls window not found");
        driver = DriverFactory.attachByHexHandle(WindowHelper.toHexHandle(h));
    }

    protected void attachToAVControlsWindow() throws Exception {
        String h = WindowHelper.findWindowHandle("Audio Visual Controls");
        if (h == null) throw new RuntimeException("AV Controls window not found");
        driver = DriverFactory.attachByHexHandle(WindowHelper.toHexHandle(h));
    }
    protected void setLastMeetingOverlayHandle(String handle) {
        lastMeetingOverlayHandle = handle;
    }

    protected void clickAdmitFromRoot() throws Exception {

        switchToDesktop();

        new MeetingOverlayPage(driver)
                .clickAdmitButton();

        switchBackToMeetingOverlay();

    }

    protected void clickDenyFromRoot() throws Exception {

        switchToDesktop();

        new MeetingOverlayPage(driver)
                .clickDenyButton();

        switchBackToMeetingOverlay();

    }
}