package pages;

import base.BasePage;
import base.DriverFactory;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.interactions.Actions;
import utils.WindowHelper;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.time.Duration;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RootSessionPage extends BasePage {

    public RootSessionPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By chatMessageTextbox =
            By.xpath("//*[@ClassName='TextBox']");

    private final By sendButton =
            By.xpath("//Button[@HelpText='Send']");
    public boolean isVirtualKeyboardClosed() throws Exception {
        return !isVirtualKeyboardVisible();
    }
//    public void sendChatMessage(String message) throws Exception {
//
//        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
//
//        try {
//
//            WebDriverWait wait = new WebDriverWait(rootDriver, 15);
//
//            WebElement textBox = wait.until(
//                    ExpectedConditions.elementToBeClickable(
//                            By.xpath("//*[@ClassName='TextBox']")));
//
//            textBox.click();
//            textBox.sendKeys(message);
//
//            System.out.println("[Root] Message entered: " + message);
//
//            WebElement sendButton = wait.until(
//                    ExpectedConditions.elementToBeClickable(
//                            By.xpath("//Button[@HelpText='Send']")));
//
//            sendButton.click();
//
//            System.out.println("[Root] Send button clicked");
//
//        } finally {
//            rootDriver.quit();
//        }
//    }
public void sendChatMessage(String message) throws Exception {

    System.out.println("[Chat] Step 1: Creating Root Session...");
    RemoteWebDriver rootDriver = DriverFactory.createRootSession();

    try {

        WebDriverWait wait = new WebDriverWait(rootDriver, 15);

        System.out.println("[Chat] Step 2: Waiting for chat textbox...");

        WebElement textBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@ClassName='TextBox']")));

        System.out.println("[Chat] Step 3: Chat textbox found");

        textBox.click();
        System.out.println("[Chat] Step 4: Chat textbox clicked");

        textBox.sendKeys(message);
        System.out.println("[Chat] Step 5: Message entered -> " + message);

        System.out.println("[Chat] Step 6: Waiting for Send button...");

        WebElement sendButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//Button[@HelpText='Send']")));

        System.out.println("[Chat] Step 7: Send button found");

        sendButton.click();
        System.out.println("[Chat] Step 8: Send button clicked");

    } finally {

        System.out.println("[Chat] Step 9: Closing Root Session");
        rootDriver.quit();
    }
}

    public void clickDoneOnKeypad() throws Exception {

        System.out.println("1. Creating Root Session...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();

        try {

            System.out.println("2. Root Session Created");

            WebDriverWait wait = new WebDriverWait(rootDriver, 10);

            System.out.println("3. Looking for Done button...");

            WebElement done = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.name("Done")));

            System.out.println("4. Done button found");

            done.click();

            System.out.println("5. Done button clicked");

        } finally {

            System.out.println("6. Closing Root Session");
            rootDriver.quit();
        }
    }
    // ── Leave Meeting ──────────────────────────────────────

    public void clickLeaveMeetingConfirmation() throws Exception {
        System.out.println("[Root] Clicking Leave meeting confirmation...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            WebDriverWait w = new WebDriverWait(rootDriver, 15);
            By[] locators = {
                    By.name("Leave meeting"),
                    By.xpath("//Text[@Name='Leave meeting']/parent::*"),
                    By.xpath("//*[contains(@Name,'Leave meeting')]"),
                    By.xpath("//Button[contains(@Name,'Leave')]")
            };
            for (By locator : locators) {
                try {
                    WebElement btn = w.until(
                            ExpectedConditions.elementToBeClickable(locator));
                    btn.click();
                    System.out.println("[Root] ✓ Leave meeting clicked: " + locator);
                    return;
                } catch (Exception ignored) {}
            }
            throw new RuntimeException("Leave meeting confirmation not found");
        } finally {
            rootDriver.quit();
        }
    }

    public void clickStayInMeeting() throws Exception {
        System.out.println("[Root] Clicking Stay in meeting...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            WebDriverWait w = new WebDriverWait(rootDriver, 15);
            WebElement btn = w.until(
                    ExpectedConditions.elementToBeClickable(
                            By.name("Stay in meeting")));
            btn.click();
            System.out.println("[Root] ✓ Stay in meeting clicked");
        } finally {
            rootDriver.quit();
        }
    }

    // ── Recording ──────────────────────────────────────────

    public boolean waitForRecordingPopup(int timeoutSeconds) throws Exception {
        System.out.println("[Root] Waiting for recording popup...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            return new WebDriverWait(rootDriver, timeoutSeconds).until(d ->
                    !d.findElements(
                            By.name("Recording and transcription")).isEmpty());
        } catch (Exception e) {
            System.out.println("[Root] Recording popup not found");
            return false;
        } finally {
            rootDriver.quit();
        }
    }

    public boolean clickStopRecordingInDialog(int timeoutSeconds)
            throws Exception {

        System.out.println("[Root] Clicking Stop in recording dialog...");

        RemoteWebDriver rootDriver;

        try {
            rootDriver = DriverFactory.createRootSession();
        } catch (Exception e) {
            System.out.println(
                    "[Root] BUG: Could not attach to Root Session."
            );
            return false;
        }

        try {

            long endTime =
                    System.currentTimeMillis()
                            + (timeoutSeconds * 1000L);

            By[] locators = {
                    By.xpath("//Dialog[@Name='Stop recording and transcription?']//Button[@Name='Stop']"),
                    By.name("Stop"),
                    By.xpath("//Button[@Name='Stop']")
            };

            while (System.currentTimeMillis() < endTime) {

                for (By locator : locators) {

                    List<WebElement> els =
                            rootDriver.findElements(locator);

                    if (!els.isEmpty()) {

                        els.get(0).click();

                        System.out.println(
                                "[Root] ✓ Stop clicked: "
                                        + locator
                        );

                        return true;
                    }
                }

                Thread.sleep(500);
            }

            System.out.println(
                    "[Root] Stop button not found"
            );

            return false;

        } finally {
            rootDriver.quit();
        }
    }
    public boolean waitForRecordingStoppedToast(int timeoutSeconds) throws Exception {
        System.out.println("[Root] Waiting for recording stopped toast...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            return new WebDriverWait(rootDriver, timeoutSeconds).until(d ->
                    !d.findElements(By.name(
                            "Recording and transcription have stopped.")).isEmpty());
        } catch (Exception e) {
            return false;
        } finally {
            rootDriver.quit();
        }
    }

    // ── Transcription ──────────────────────────────────────

    public boolean waitForTranscriptionPopup(int timeoutSeconds) throws Exception {
        System.out.println("[Root] Waiting for transcription popup...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            By[] locators = {
                    By.name("Recording and transcription"),
                    By.name("Transcription"),
                    By.xpath("//*[contains(@Name,'transcription')]")
            };
            WebDriverWait w = new WebDriverWait(rootDriver, timeoutSeconds);
            for (By locator : locators) {
                try {
                    boolean found = w.until(d ->
                            !d.findElements(locator).isEmpty());
                    if (found) {
                        System.out.println("[Root] ✓ Transcription popup: " + locator);
                        return true;
                    }
                } catch (Exception ignored) {}
            }
            return false;
        } finally {
            rootDriver.quit();
        }
    }

    public boolean clickStopTranscriptionInDialog(int timeoutSeconds) {
        System.out.println("[Root] Clicking Stop transcription...");
        final boolean[] result = {false};

        Thread t = new Thread(() -> {
            RemoteWebDriver rootDriver = null;
            try {
                rootDriver = DriverFactory.createRootSession();
                rootDriver.manage().timeouts()
                        .implicitlyWait(1, TimeUnit.SECONDS);
                By[] locators = {
                        By.xpath("//Dialog[contains(@Name,'Stop')]" +
                                "//Button[@Name='Stop']"),
                        By.name("Stop"),
                        By.xpath("//Button[@Name='Stop']")
                };
                long deadline = System.currentTimeMillis()
                        + (timeoutSeconds * 1000L);
                while (System.currentTimeMillis() < deadline) {
                    for (By locator : locators) {
                        try {
                            List<WebElement> els =
                                    rootDriver.findElements(locator);
                            if (!els.isEmpty()) {
                                els.get(0).click();
                                System.out.println("[Root] ✓ Stop transcription: "
                                        + locator);
                                result[0] = true;
                                return;
                            }
                        } catch (Exception ignored) {}
                    }
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                System.out.println("[Root] Stop transcription error: "
                        + e.getMessage());
            } finally {
                if (rootDriver != null) {
                    try { rootDriver.quit(); }
                    catch (Exception ignored) {}
                }
            }
        });

        t.setDaemon(true);
        t.start();
        try { t.join((timeoutSeconds + 3) * 1000L); }
        catch (InterruptedException ignored) {}
        if (t.isAlive()) t.interrupt();

        return result[0];
    }

    public boolean waitForTranscriptionStoppedToast(int timeoutSeconds)
            throws Exception {
        System.out.println("[Root] Waiting for transcription stopped toast...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            By[] locators = {
                    By.name("Recording and transcription have stopped."),
                    By.name("Transcription stopped."),
                    By.xpath("//*[contains(@Name,'transcription have stopped')]")
            };
            WebDriverWait w = new WebDriverWait(rootDriver, timeoutSeconds);
            for (By locator : locators) {
                try {
                    boolean found = w.until(d ->
                            !d.findElements(locator).isEmpty());
                    if (found) {
                        System.out.println("[Root] ✓ Stopped toast: " + locator);
                        return true;
                    }
                } catch (Exception ignored) {}
            }
            return false;
        } finally {
            rootDriver.quit();
        }
    }

    // ── Virtual Keyboard / Chat ────────────────────────────
//
//    public void clickChatMessageTextbox() throws Exception {
//        System.out.println("[Root] Clicking chat textbox...");
//        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
//        try {
//            WebDriverWait w = new WebDriverWait(rootDriver, 20);
//            By[] locators = {
//                    By.name("Type a message"),
//                    By.name("Message everyone"),
//                    By.xpath("//*[@ClassName='TextBox']"),
//                    By.xpath("//*[contains(@Name,'message')]")
//            };
//            for (By locator : locators) {
//                try {
//                    WebElement el = w.until(
//                            ExpectedConditions.elementToBeClickable(locator));
//                    el.click();
//                    System.out.println("[Root] ✓ Chat textbox clicked: " + locator);
//                    return;
//                } catch (Exception ignored) {
//                    System.out.println("[Root] Miss: " + locator);
//                }
//            }
//            throw new RuntimeException("Chat textbox not found");
//        } finally {
//            rootDriver.quit();
//        }
//    }
//    public void clickChatMessageTextbox() throws Exception {
//
//        System.out.println("[Root] Clicking chat textbox...");
//        System.out.println("[Root] Creating Root Session...");
//
//        RemoteWebDriver rootDriver =
//                DriverFactory.createRootSession();
//
//        System.out.println("[Root] Root Session Created");
//
//        try {
//
//            By[] locators = {
//                    By.name("Type a message"),
//                    By.name("Message everyone"),
//                    By.xpath("//*[contains(@Name,'message')]")
//            };
//
//            for (By locator : locators) {
//
//                System.out.println(
//                        "[Root] Trying locator: " + locator
//                );
//
//                List<WebElement> els =
//                        rootDriver.findElements(locator);
//
//                if (!els.isEmpty()) {
//
//                    WebElement el = els.get(0);
//
//                    System.out.println(
//                            "[Root] Element found"
//                    );
//
//                    try {
//                        el.click();
//                        System.out.println("[Root] ✓ Chat textbox clicked");
//                        Thread.sleep(2000);
//                        return;
//                    } catch (Exception e) {
//                        System.out.println(
//                                "[Root] Click failed: " + e.getMessage()
//                        );
//                    }
//                }
//
//                System.out.println(
//                        "[Root] Miss: " + locator
//                );
//            }
//
//            throw new RuntimeException(
//                    "Chat textbox not found"
//            );
//
//        } finally {
//
//            System.out.println(
//                    "[Root] Closing Root Session"
//            );
//
//            rootDriver.quit();
//        }
//    }
    public void clickChatMessageTextbox() throws Exception {

        RemoteWebDriver rootDriver = DriverFactory.createRootSession();

        try {
            By[] locators = {
                    By.name("Type a message"),
                    By.name("Message everyone"),
                    By.xpath("//*[@ClassName='TextBox']"),
                    By.xpath("//*[contains(@Name,'message')]")
            };

            WebElement target = null;
            By matchedLocator = null;

            for (By locator : locators) {
                List<WebElement> els = rootDriver.findElements(locator);
                if (!els.isEmpty()) {
                    target = els.get(0);
                    matchedLocator = locator;
                    break;
                }
            }

            if (target == null) {
                throw new RuntimeException("Chat textbox not found");
            }

            System.out.println("[Root] Element found via: " + matchedLocator);

            // Strategy 1: Actions-based real mouse click (sends Win32 mouse events)
            boolean focused = tryActionsClick(rootDriver, target);

            // Strategy 2: If Actions didn't produce focus, use Robot for native OS click
            if (!focused) {
                System.out.println("[Root] Actions click did not focus — falling back to Robot click");
                focused = tryRobotClick(target);
            }

            if (!focused) {
                throw new RuntimeException(
                        "Chat textbox found but could not be focused by any click strategy"
                );
            }

            System.out.println("[Root] ✓ Chat textbox clicked and focused");

        } finally {
            rootDriver.quit();
        }
    }

    private boolean tryActionsClick(RemoteWebDriver driver, WebElement element) {
        try {
            // Move to center of element and send a real left-click sequence
            new Actions(driver)
                    .moveToElement(element)
                    .pause(Duration.ofMillis(150))   // let the move settle
                    .click()
                    .pause(Duration.ofMillis(300))   // let focus propagate
                    .perform();
            return true;
        } catch (Exception e) {
            System.out.println("[Root] Actions click failed: " + e.getMessage());
            return false;
        }
    }

    private boolean tryRobotClick(WebElement element) {
        try {
            // Get the element's absolute screen position
            Point location = element.getLocation();
            Dimension size   = element.getSize();

            int centerX = location.getX() + size.getWidth()  / 2;
            int centerY = location.getY() + size.getHeight() / 2;

            Robot robot = new Robot();
            robot.mouseMove(centerX, centerY);
            robot.delay(100);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(50);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(300); // allow keyboard to appear

            System.out.println("[Root] Robot click at (" + centerX + ", " + centerY + ")");
            return true;
        } catch (Exception e) {
            System.out.println("[Root] Robot click failed: " + e.getMessage());
            return false;
        }
    }

//    public boolean isVirtualKeyboardVisible() throws Exception {
//        System.out.println("[Root] Checking virtual keyboard...");
//        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
//        try {
//            rootDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//            By[] locators = {
//                    By.name("Zoom Chat Keyboard"),
//                    By.xpath("//Pane[@Name='Zoom Chat Keyboard']"),
//                    By.xpath("//*[@ClassName='InputSiteWindowClass']")
//            };
//            for (By locator : locators) {
//                if (!rootDriver.findElements(locator).isEmpty()) {
//                    System.out.println("[Root] ✓ Keyboard found: " + locator);
//                    return true;
//                }
//            }
//            return false;
//        } finally {
//            rootDriver.quit();
//        }
//    }
public boolean isVirtualKeyboardVisible() throws Exception {

    System.out.println(
            "[Root] Checking whether Zoom virtual keyboard is visible..."
    );

    RemoteWebDriver rootDriver =
            DriverFactory.createRootSession();

    try {

        return waitForActualVirtualKeyboard(
                rootDriver,
                5
        );

    } finally {

        rootDriver.quit();
    }
}

    private void dumpAllInputSiteWindows(RemoteWebDriver rootDriver) {
        try {
            List<WebElement> all = rootDriver.findElements(
                    By.xpath("//*[@ClassName='InputSiteWindowClass']"));

            System.out.println("[Diag] Found " + all.size() + " InputSiteWindowClass elements");

            int i = 0;
            for (WebElement w : all) {
                i++;
                try {
                    System.out.println("[Diag] #" + i
                            + " displayed=" + w.isDisplayed()
                            + " loc=" + w.getLocation()
                            + " size=" + w.getSize());

                    List<WebElement> children = w.findElements(By.xpath("./*"));
                    System.out.println("[Diag] #" + i + " direct children=" + children.size());
                    for (WebElement c : children) {
                        System.out.println("[Diag]    child Name=" + c.getAttribute("Name")
                                + " ControlType=" + c.getAttribute("ControlType"));
                    }
                } catch (Exception e) {
                    System.out.println("[Diag] #" + i + " error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("[Diag] dump failed: " + e.getMessage());
        }
    }

    private boolean waitForActualVirtualKeyboard(
            RemoteWebDriver rootDriver,
            int timeoutSeconds) throws Exception {

        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        // NOTE: the "Keyboard"-named Text node is NOT guaranteed to sit right
        // under the InputSiteWindowClass pane -- diagnostics showed both live
        // InputSiteWindowClass instances have a single unnamed Pane as their
        // only direct child, so a "." (any-descendant) search is required,
        // not a filtered ancestor match. We now enumerate every
        // InputSiteWindowClass window directly and inspect it, instead of
        // pre-filtering with an xpath predicate that silently returns zero
        // results.
        By allInputSiteWindows =
                By.xpath("//*[@ClassName='InputSiteWindowClass']");

        while (System.currentTimeMillis() < endTime) {

            try {
                List<WebElement> candidates = rootDriver.findElements(allInputSiteWindows);

                System.out.println("[Keyboard] InputSiteWindowClass count = " + candidates.size());

                for (WebElement candidate : candidates) {

                    try {
                        if (!candidate.isDisplayed()) {
                            continue;
                        }

                        Point location = candidate.getLocation();
                        Dimension size = candidate.getSize();

                        System.out.println("[Keyboard] Candidate loc=" + location + " size=" + size);

                        // Reject the full-screen TSF broker/host windows.
                        // Real docked keyboard bounds observed: {l:389 t:1215 r:2347 b:1663}
                        // i.e. width ~1958, height ~448 -- clearly smaller than
                        // the ~2740x1830 full-screen broker panes seen in the logs.
                        if (location.getX() < 0 || location.getY() < 0
                                || size.getWidth() <= 100 || size.getHeight() <= 100
                                || size.getWidth() >= 2500 || size.getHeight() >= 1700) {
                            System.out.println("[Keyboard] Rejected — not a docked-keyboard-sized window");
                            continue;
                        }

                        // Bounds match is the whole test. Confirmed via two
                        // live runs that this is a reliable, unique
                        // discriminator: the real docked keyboard is always
                        // ~1958x448 at a fixed screen position, while the two
                        // TSF broker/host windows are always near-fullscreen
                        // (~2740x1830). Button count was tried as a second
                        // confirmation but WinAppDriver does not expose the
                        // individual key elements inside this control (Button
                        // count reads 0 even while the keyboard is visibly
                        // on-screen), so it cannot be used as a check here.
                        System.out.println("[Keyboard] ✓ REAL virtual keyboard detected (bounds match)");
                        return true;

                    } catch (Exception ignored) {
                    }
                }

            } catch (Exception e) {
                System.out.println("[Keyboard] Search error: " + e.getMessage());
            }

            Thread.sleep(500);
        }

        System.out.println("[Keyboard] ✗ Real virtual keyboard NOT detected");
        return false;
    }
    public boolean clickChatMessageTextboxAndCheckKeyboard() throws Exception {

        System.out.println("[Root] Looking for actual Zoom chat message textbox...");

        RemoteWebDriver rootDriver = DriverFactory.createRootSession();

        try {

            WebDriverWait wait = new WebDriverWait(rootDriver, 15);

            By composeBox = By.xpath("//*[@AutomationId='ComposeBox']");

            WebElement messageBox = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(composeBox));

            System.out.println("[Root] ✓ ComposeBox found");
            System.out.println("[DEBUG] Location = " + messageBox.getLocation());
            System.out.println("[DEBUG] Size = " + messageBox.getSize());

            boolean focused = clickComposeBoxUntilFocused(rootDriver, composeBox, 3);

            if (!focused) {
                throw new RuntimeException(
                        "ComposeBox could not be focused after multiple click attempts"
                );
            }

            System.out.println("[Root] Waiting for Zoom virtual keyboard...");
            dumpAllInputSiteWindows(rootDriver);

            return waitForActualVirtualKeyboard(rootDriver, 15);

        } finally {
            rootDriver.quit();
        }
    }

    /**
     * Clicks the ComposeBox via native Robot click, then re-queries the
     * element and checks HasKeyboardFocus. Retries with a fresh click if
     * focus didn't land -- a single Robot click was silently failing to
     * focus the box (observed: "HasKeyboardFocus after click = False"),
     * which meant the keyboard-wait below was running against a box that
     * was never actually focused.
     */
    private boolean clickComposeBoxUntilFocused(
            RemoteWebDriver rootDriver,
            By composeBox,
            int maxAttempts) throws Exception {

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            System.out.println("[Root] Focus attempt " + attempt + "/" + maxAttempts);

            WebElement box = rootDriver.findElement(composeBox);

            Point location = box.getLocation();
            Dimension size = box.getSize();

            int centerX = location.getX() + (size.getWidth() / 2);
            int centerY = location.getY() + (size.getHeight() / 2);

            System.out.println("[Root] Clicking ComposeBox at (" + centerX + ", " + centerY + ")");

            Robot robot = new Robot();
            robot.mouseMove(centerX, centerY);
            robot.delay(200);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(100);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(700);

            WebElement recheck = rootDriver.findElement(composeBox);
            String hasFocus = recheck.getAttribute("HasKeyboardFocus");

            System.out.println("[Root] HasKeyboardFocus after attempt " + attempt + " = " + hasFocus);

            if ("True".equalsIgnoreCase(hasFocus)) {
                System.out.println("[Root] ✓ ComposeBox focused");
                return true;
            }

            // Fallback strategy on retry: real Actions-based move+click,
            // which sends a different event path than Robot and can succeed
            // where Robot's absolute-coordinate click misses (e.g. DPI
            // scaling mismatch between element bounds and physical pixels).
            if (attempt < maxAttempts) {
                try {
                    new Actions(rootDriver)
                            .moveToElement(recheck)
                            .pause(Duration.ofMillis(150))
                            .click()
                            .pause(Duration.ofMillis(400))
                            .perform();

                    WebElement afterActions = rootDriver.findElement(composeBox);
                    String hasFocusAfterActions = afterActions.getAttribute("HasKeyboardFocus");

                    System.out.println("[Root] HasKeyboardFocus after Actions click = " + hasFocusAfterActions);

                    if ("True".equalsIgnoreCase(hasFocusAfterActions)) {
                        System.out.println("[Root] ✓ ComposeBox focused via Actions");
                        return true;
                    }
                } catch (Exception e) {
                    System.out.println("[Root] Actions click fallback failed: " + e.getMessage());
                }
            }
        }

        return false;
    }
}