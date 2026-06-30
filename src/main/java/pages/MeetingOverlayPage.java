package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;

public class MeetingOverlayPage extends BasePage {

    public MeetingOverlayPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    // Chat
    private final By chatButton =
            By.xpath("//Button[.//Text[@Name='Chat']]");
    private final By chatButtonByAutomationId =
            By.xpath("//*[@AutomationId='ChatButton']");
    private final By chatButtonBroad =
            By.xpath("//Button[contains(@AutomationId,'Chat')" +
                    " or .//Text[contains(@Name,'Chat')]]");
    private final By chatPanel =
            By.xpath("//*[contains(@Name,'Chat')]");


    // People
    private final By peopleButton =
            By.xpath("//Button[.//Text[@Name='People']]");
    private final By peoplePanel =
            By.xpath("//*[contains(@Name,'People')" +
                    " or contains(@Name,'Participants')]");

    // Raise Hand
    private final By raiseHandButton =
            By.xpath("//Button[.//Text[@Name='Hand Raise']]");
    private final By handRaisedButton =
            By.xpath("//Button[.//Text[@Name='Hand Raised']]");

    // Leave
    private final By leaveButton =
            By.xpath("//Button[.//Text[@Name='Leave']]");

    // Camera
    private final By overlayCameraButton =
            By.xpath("//*[@AutomationId='CameraButton']");
    private final By overlayCameraButtonText =
            By.xpath("//*[@AutomationId='CameraButtonText']");

    // Mic
    private final By overlayMicButton =
            By.xpath("//*[@AutomationId='MicButton']");
    private final By overlayMicButtonText =
            By.xpath("//*[@AutomationId='MicButtonText']");

    // Record / Transcribe
    private final By recordButton =
            By.xpath("//Button[.//Text[@Name='Record']]");
    private final By transcribeButton =
            By.xpath("//Button[.//Text[@Name='Transcribe']]");

    // Kiosk
    private final By kioskExitButton =
            By.xpath("//*[@AutomationId='ExitButton']");

    // Audio Visual
    private final By audioVisualButton =
            By.xpath("//*[@AutomationId='AudioVisualTabButton']");

    // Chat message box

    private final By chatMessageBox =
            By.name("Message everyone");

    // Stay in meeting
    private final By stayInMeetingButton =
            By.name("Stay in meeting");

    //share invite

    private final By shareInviteButton =
            By.name("Share invite");

    // ── Meeting Joined ─────────────────────────────────────


    public void clickShareInviteButton() {

        WebElement btn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.name("Share invite")));

        try {
            btn.click();
            System.out.println("Clicked using normal click");
            return;
        } catch (Exception ignored) {
        }

        try {
            ((RemoteWebDriver) driver)
                    .executeScript("arguments[0].click();", btn);
            System.out.println("Clicked using JavaScript");
            return;
        } catch (Exception ignored) {
        }

        try {
            new Actions(driver)
                    .moveToElement(btn)
                    .click()
                    .perform();
            System.out.println("Clicked using Actions");
            return;
        } catch (Exception ignored) {
        }

        throw new RuntimeException("Unable to click Share invite button");
    }
//    ==> when share invite button is working for 1 click then use this code... till then use below code.. it will click that button 3 times.
//public void clickShareInviteButton() {
//
//    WebElement btn = wait.until(
//            ExpectedConditions.visibilityOfElementLocated(
//                    By.name("Share invite")));
//
//    for (int i = 1; i <= 3; i++) {
//        try {
//            btn.click();
//            System.out.println("Clicked Share Invite - Attempt " + i);
//            Thread.sleep(1000);
//
//            // Stop if Share Invite screen opens
//            if (!driver.findElements(By.name("Share meeting invite")).isEmpty()) {
//                System.out.println("Share Invite screen opened");
//                return;
//            }
//
//        } catch (Exception e) {
//            System.out.println("Click attempt " + i + " failed");
//        }
//    }
//
//    throw new RuntimeException("Unable to open Share Invite screen after 3 clicks.");
//}
    public boolean waitForMeetingJoinedScreen() {

        System.out.println("[Overlay] Waiting for meeting joined screen...");

        try {

            // Option 1 - Chat button appears
            if (waitForChatButtonReady()) {
                System.out.println("[Overlay] Chat button found");
                return true;
            }

            // Option 2 - People button appears
            if (waitForPeopleButtonReady()) {
                System.out.println("[Overlay] People button found");
                return true;
            }

            // Option 3 - Meeting title visible
            if (waitForMeetingTitleVisible()) {
                System.out.println("[Overlay] Meeting title visible");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("[Overlay] Meeting screen not detected");

        return false;
    }


    public boolean isChatButtonVisible() {
        System.out.println("joined in meeting screen");
        System.out.println("chat button is visible");
        return isVisible(chatButton);

    }

    // ── Camera ─────────────────────────────────────────────

    public boolean isOverlayCameraOn() {
        try {
            List<WebElement> els = driver.findElements(overlayCameraButtonText);
            if (els.isEmpty()) return false;
            String name = els.get(0).getAttribute("Name");
            System.out.println("[Camera] State = " + name);
            return "Camera".equals(name);
        } catch (Exception e) { return false; }
    }

    public boolean isOverlayCameraOff() {
        try {
            List<WebElement> els = driver.findElements(overlayCameraButtonText);
            if (els.isEmpty()) return false;
            String name = els.get(0).getAttribute("Name");
            System.out.println("[Camera] State = " + name);
            return "Cam Off".equals(name);
        } catch (Exception e) { return false; }
    }

    public boolean waitForOverlayCameraOn() {
        try {
            new WebDriverWait(driver, 20).until(d -> isOverlayCameraOn());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean waitForOverlayCameraOff() {
        try {
            new WebDriverWait(driver, 20).until(d -> isOverlayCameraOff());
            return true;
        } catch (Exception e) { return false; }
    }

    public void clickOverlayCameraToggle() {
        try {
            WebDriverWait w = new WebDriverWait(driver, 20);
            WebElement btn = w.until(
                    ExpectedConditions.presenceOfElementLocated(overlayCameraButton));
            String before = driver.findElement(overlayCameraButtonText)
                    .getAttribute("Name");
            System.out.println("[Camera] Before = " + before);
            new Actions(driver).moveToElement(btn).click().perform();
            Thread.sleep(3000);
            String after = driver.findElement(overlayCameraButtonText)
                    .getAttribute("Name");
            System.out.println("[Camera] After = " + after);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click camera toggle", e);
        }
    }

    // ── Mic ────────────────────────────────────────────────

    public String getOverlayMicState() {
        try {
            return driver.findElement(overlayMicButtonText)
                    .getAttribute("Name");
        } catch (Exception e) { return "UNKNOWN"; }
    }

    public boolean isOverlayMicMuted() {
        try {
            return new WebDriverWait(driver, 15).until(d ->
                    getOverlayMicState().equalsIgnoreCase("Muted"));
        } catch (Exception e) { return false; }
    }

    public boolean isOverlayMicUnmuted() {
        try {
            return new WebDriverWait(driver, 15).until(d ->
                    getOverlayMicState().equalsIgnoreCase("Mic"));
        } catch (Exception e) { return false; }
    }

    public boolean waitForOverlayMicMuted() {
        try {
            new WebDriverWait(driver, 10).until(d ->
                    getOverlayMicState().equalsIgnoreCase("Muted"));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean waitForOverlayMicUnmuted() {
        try {
            new WebDriverWait(driver, 10).until(d ->
                    getOverlayMicState().equalsIgnoreCase("Mic"));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean isOverlayMicStateValid() {
        String state = getOverlayMicState();
        System.out.println("[Mic] State = " + state);
        return state.equalsIgnoreCase("Muted")
                || state.equalsIgnoreCase("Mic");
    }

    public void clickOverlayMicToggle() {
        try {

            String before = getOverlayMicState();
            System.out.println("[Mic] Before = " + before);

            WebDriverWait w = new WebDriverWait(driver, 20);

            WebElement btn = w.until(d -> {
                List<WebElement> els =
                        d.findElements(overlayMicButton);
                return els.isEmpty() ? null : els.get(0);
            });

            new Actions(driver)
                    .moveToElement(btn)
                    .click()
                    .perform();

            Thread.sleep(2000);

            String after = getOverlayMicState();

            System.out.println(
                    "[Mic] After = " + after
            );

            if (before.equals(after)) {
                throw new RuntimeException(
                        "Mic state did not change"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to click mic toggle",
                    e
            );
        }
    }

    // ── Chat ───────────────────────────────────────────────

    public boolean waitForChatButtonReady() {
        By[] locators = {
                chatButton, chatButtonByAutomationId, chatButtonBroad
        };
        for (By locator : locators) {
            try {
                new WebDriverWait(driver, 25)
                        .until(ExpectedConditions.visibilityOfElementLocated(locator));
                System.out.println("[Chat] Found via: " + locator);
                return true;
            } catch (Exception e) {
                System.out.println("[Chat] Locator miss: " + locator);
            }
        }
        return false;
    }

    public void clickChatButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        w.until(ExpectedConditions.elementToBeClickable(chatButton)).click();
        System.out.println("[Chat] Chat button clicked");
    }

    public void clickChatButtonRobust() {
        By[] locators = {
                chatButton, chatButtonByAutomationId, chatButtonBroad
        };
        WebDriverWait w = new WebDriverWait(driver, 30);
        for (By locator : locators) {
            try {
                WebElement el = w.until(
                        ExpectedConditions.elementToBeClickable(locator));
                el.click();
                System.out.println("[Chat] Clicked via: " + locator);
                return;
            } catch (Exception e) {
                System.out.println("[Chat] Failed: " + locator);
            }
        }
        throw new RuntimeException("All chat button locators failed");
    }

    //    public boolean isChatMessageBoxVisible() {
//        try {
//            return driver.findElement(chatMessageBox).isDisplayed();
//        } catch (Exception e) { return false; }
//    }
    public boolean isChatMessageBoxVisible() {

        try {

            int count = driver.findElements(chatMessageBox).size();

            System.out.println(
                    "[Chat] Message everyone count = " + count
            );

            return count > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isChatPanelVisible() {

        try {
            return new WebDriverWait(driver, 10)
                    .until(d -> !d.findElements(chatPanel).isEmpty());

        } catch (Exception e) {
            return false;
        }
    }

    // ── People ─────────────────────────────────────────────

    public boolean waitForPeopleButtonReady() {
        try {
            return new WebDriverWait(driver, 60)
                    .until(d -> !d.findElements(peopleButton).isEmpty());
        } catch (Exception e) { return false; }
    }

    public void clickPeopleButton() {
        try {
            driver.findElement(peopleButton).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click People button", e);
        }
    }

    public boolean waitForPeoplePanelOpened() {
        try {
            return new WebDriverWait(driver, 20)
                    .until(d -> !d.findElements(peoplePanel).isEmpty());
        } catch (Exception e) { return false; }
    }

    // ── Raise Hand ─────────────────────────────────────────

    public void clickRaiseHandButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        w.until(ExpectedConditions.elementToBeClickable(raiseHandButton)).click();
        System.out.println("[Hand] Raise Hand clicked");
    }

    public void clickLowerHandButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        w.until(ExpectedConditions.elementToBeClickable(handRaisedButton)).click();
        System.out.println("[Hand] Hand Raised clicked");
    }

    public void doubleClickRaiseHandButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        WebElement btn = w.until(
                ExpectedConditions.elementToBeClickable(raiseHandButton));
        new Actions(driver).doubleClick(btn).perform();
        System.out.println("[Hand] Double-clicked Raise Hand");
    }

    public void doubleClickLowerHandButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        WebElement btn = w.until(
                ExpectedConditions.elementToBeClickable(handRaisedButton));
        new Actions(driver).doubleClick(btn).perform();
        System.out.println("[Hand] Double-clicked Hand Raised");
    }

    public boolean isMyHandRaised() {
        try {
            boolean buttonChanged = new WebDriverWait(driver, 15).until(d ->
                    !d.findElements(handRaisedButton).isEmpty());
            if (buttonChanged) {
                System.out.println("[Hand] ✓ Hand raised — toolbar changed");
                return true;
            }
        } catch (Exception ignored) {}

        By[] tileLocators = {
                By.xpath("//*[contains(@Name,'Navya Kanugula')" +
                        " and contains(@Name,'Hand raised')]"),
                By.xpath("//*[contains(@Name,'Hand raised')]"),
                By.xpath("//*[contains(@Name,'Raised hand')]")
        };
        for (By locator : tileLocators) {
            try {
                if (!driver.findElements(locator).isEmpty()) {
                    System.out.println("[Hand] ✓ Hand raised via tile: " + locator);
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    public boolean isMyHandLowered() {
        try {
            boolean reverted = new WebDriverWait(driver, 15).until(d ->
                    !d.findElements(raiseHandButton).isEmpty());
            if (reverted) {
                System.out.println("[Hand] ✓ Hand lowered — toolbar reverted");
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    // ── Leave ──────────────────────────────────────────────

    public void clickLeaveButton() {
        WebDriverWait w = new WebDriverWait(driver, 15);
        w.until(ExpectedConditions.elementToBeClickable(leaveButton)).click();
        System.out.println("[Leave] Leave button clicked");
    }
    public boolean isLeaveDialogVisible() {

        try {

            int count =
                    driver.findElements(stayInMeetingButton).size();

            System.out.println(
                    "[Leave Dialog] Stay button count = " + count
            );

            return count > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public void clickStayInMeetingButton() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        stayInMeetingButton
                )
        ).click();
    }

    // ── Record ─────────────────────────────────────────────

    public void clickRecordButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        w.until(ExpectedConditions.elementToBeClickable(recordButton)).click();
        System.out.println("[Record] Record button clicked");
    }

    // ── Transcribe ─────────────────────────────────────────

    public void clickTranscribeButton() {
        WebDriverWait w = new WebDriverWait(driver, 20);
        w.until(ExpectedConditions.elementToBeClickable(transcribeButton)).click();
        System.out.println("[Transcribe] Transcribe button clicked");
    }

    // ── Meeting Name ───────────────────────────────────────

    public String getInMeetingName() {

        List<WebElement> textBlocks =
                driver.findElements(
                        By.className("TextBlock")
                );

        for (WebElement el : textBlocks) {

            String text =
                    el.getText().trim();

            int y =
                    el.getLocation().getY();

            System.out.println(
                    "Text = [" + text + "] Y = " + y
            );

            if (!text.isEmpty()
                    && y < 60
                    && !text.matches("\\d+:\\d+")
                    && !text.equalsIgnoreCase("T")) {

                return text;
            }
        }

        return null;
    }
    public boolean waitForMeetingTitleVisible() {
        try {
            return new WebDriverWait(driver, 30)
                    .until(d -> getInMeetingName() != null);
        } catch (Exception e) { return false; }
    }

    // ── Audio Visual Button ────────────────────────────────

    public void clickAudioVisualButton() {
        System.out.println("[AV] Clicking Audio & Visual button...");
        WebDriverWait w = new WebDriverWait(driver, 15);
        WebElement btn = w.until(d ->
                !d.findElements(audioVisualButton).isEmpty()
                        ? d.findElements(audioVisualButton).get(0) : null);
        new Actions(driver).moveToElement(btn).click().perform();
        System.out.println("[AV] Clicked Audio & Visual button");
    }

    // ── Kiosk ──────────────────────────────────────────────

    public void clickKioskExitButton() {
        WebDriverWait w = new WebDriverWait(driver, 15);
        w.until(ExpectedConditions.elementToBeClickable(kioskExitButton)).click();
        System.out.println("[Kiosk] Exit button clicked");
    }
}