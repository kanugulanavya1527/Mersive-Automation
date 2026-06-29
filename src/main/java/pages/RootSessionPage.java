package pages;

import base.BasePage;
import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Point;
import org.openqa.selenium.Dimension;
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

    public boolean isVirtualKeyboardVisible() throws Exception {
        System.out.println("[Root] Checking virtual keyboard...");
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            rootDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            By[] locators = {
                    By.name("Zoom Chat Keyboard"),
                    By.xpath("//Pane[@Name='Zoom Chat Keyboard']"),
                    By.xpath("//*[@ClassName='InputSiteWindowClass']")
            };
            for (By locator : locators) {
                if (!rootDriver.findElements(locator).isEmpty()) {
                    System.out.println("[Root] ✓ Keyboard found: " + locator);
                    return true;
                }
            }
            return false;
        } finally {
            rootDriver.quit();
        }
    }
    public boolean clickChatMessageTextboxAndCheckKeyboard() throws Exception {
        RemoteWebDriver rootDriver = DriverFactory.createRootSession();
        try {
            // ── Step 1: Find the textbox ──────────────────────────
            By[] textboxLocators = {
                    By.name("Type a message"),
                    By.name("Message everyone"),
                    By.xpath("//*[@ClassName='TextBox']"),
                    By.xpath("//*[contains(@Name,'message')]")
            };

            rootDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            WebElement target = null;
            By matchedLocator = null;

            for (By locator : textboxLocators) {
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

            // Print coordinates for diagnosis
            Point loc = target.getLocation();
            Dimension sz = target.getSize();
            System.out.println("[DEBUG] Element location: x=" + loc.getX() + " y=" + loc.getY());
            System.out.println("[DEBUG] Element size: w=" + sz.getWidth() + " h=" + sz.getHeight());

            // ── Step 2: First click — bring window into focus ─────
            System.out.println("[Root] First click...");
            tryRobotClick(target);
            System.out.println("[Root] First click done — waiting 3 seconds...");
            Thread.sleep(3000);

            // ── Step 3: Re-find element (avoid stale reference) ───
            target = null;
            for (By locator : textboxLocators) {
                List<WebElement> els = rootDriver.findElements(locator);
                if (!els.isEmpty()) {
                    target = els.get(0);
                    System.out.println("[Root] Re-found element via: " + locator);
                    break;
                }
            }

            if (target == null) {
                throw new RuntimeException("Chat textbox not found on second lookup");
            }

            // ── Step 4: Second click — focus the textbox itself ───
            System.out.println("[Root] Second click...");
            tryRobotClick(target);
            System.out.println("[Root] Second click done");
            Thread.sleep(1000);

            // ── Step 5: Poll for virtual keyboard — 15s window ───
            By[] keyboardLocators = {
                    By.name("Zoom Chat Keyboard"),
                    By.xpath("//Pane[@Name='Zoom Chat Keyboard']"),
                    By.xpath("//*[@ClassName='InputSiteWindowClass']")
            };

            long deadline = System.currentTimeMillis() + 15_000L;
            System.out.println("[Root] Polling for virtual keyboard...");

            while (System.currentTimeMillis() < deadline) {
                for (By locator : keyboardLocators) {
                    List<WebElement> els = rootDriver.findElements(locator);
                    if (!els.isEmpty()) {
                        System.out.println("[Root] ✓ Keyboard found: " + locator);
                        return true;
                    }
                }
                Thread.sleep(500);
            }

            System.out.println("[Root] ✗ Virtual keyboard not found within 15s");
            return false;

        } finally {
            rootDriver.quit();
        }
    }
}