package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
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
            By.xpath("//*[@AutomationId='HandRaiseButton']");
    private final By handRaisedButton =
            By.xpath("//*[@AutomationId='HandRaiseButton']//Text[@Name='Lower Hand']");
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

    private final By zoomInviteButton =
            By.xpath("//Button[contains(@Name,'Invite')]");

    // Lobby
    private final By admitButton = By.name("Admit");
    private final By denyButton = By.name("Deny");




    // ── Meeting Joined ─────────────────────────────────────
    public void clickZoomInviteButton() {

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        zoomInviteButton));

        btn.click();

        System.out.println("Zoom Invite button clicked");
    }

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
            return "Camera Off".equalsIgnoreCase(name);
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

            List<WebElement> texts =
                    driver.findElements(By.xpath("//*[@AutomationId='MicButton']//Text"));

            if (texts.isEmpty())
                return "UNKNOWN";

            String state =
                    texts.get(texts.size() - 1).getAttribute("Name").trim();

            System.out.println("[Mic] State = " + state);

            return state;

        } catch (Exception e) {

            return "UNKNOWN";

        }
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

        List<WebElement> buttons = driver.findElements(By.xpath("//Button"));

        System.out.println("========= BUTTONS =========");

        for (WebElement b : buttons) {

            System.out.println(
                    "AutomationId = " + b.getAttribute("AutomationId")
                            + " | Name = " + b.getAttribute("Name")
            );
        }

        System.out.println("===========================");

        WebElement button = driver.findElement(
                By.xpath("//*[@AutomationId='HandRaiseButton']")
        );

        button.click();

        System.out.println("✓ Raise Hand clicked");
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

            List<WebElement> texts =
                    driver.findElements(
                            By.xpath("//*[@AutomationId='HandRaiseButton']//Text"));

            if (texts.isEmpty())
                return false;

            String state =
                    texts.get(0).getAttribute("Name");

            System.out.println("[Hand] State = " + state);

            return state.equalsIgnoreCase("Lower Hand");

        } catch (Exception e) {

            return false;

        }
    }
    public boolean isMyHandLowered() {

        try {

            List<WebElement> texts =
                    driver.findElements(
                            By.xpath("//*[@AutomationId='HandRaiseButton']//Text"));

            if (texts.isEmpty())
                return false;

            String state =
                    texts.get(0).getAttribute("Name");

            System.out.println("[Hand] State = " + state);

            return state.equalsIgnoreCase("Raise Hand");

        } catch (Exception e) {

            return false;

        }
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


////////////////////////////////////////////////////////








    public void clickAdmitButton() {

        driver.findElement(By.name("Admit")).click();

    }

    public void clickDenyButton() {

        driver.findElement(By.name("Deny")).click();

    }

    public boolean isJoinRequestDisplayed() {

        try {

            return new WebDriverWait(driver, 30)
                    .until(d ->
                            !d.findElements(admitButton).isEmpty()
                                    &&
                                    !d.findElements(denyButton).isEmpty());

        } catch (Exception e) {

            return false;

        }
    }

    public boolean waitForJoinRequestToDisappear() {

        try {

            return new WebDriverWait(driver, 20)
                    .until(d ->
                            d.findElements(admitButton).isEmpty()
                                    &&
                                    d.findElements(denyButton).isEmpty());

        } catch (Exception e) {

            return false;

        }
    }




    public boolean isOverlayMicMuted() {
        return getOverlayMicState().equalsIgnoreCase("Unmute");
    }

    public boolean isOverlayMicUnmuted() {
        return getOverlayMicState().equalsIgnoreCase("Mute");
    }

    public boolean waitForOverlayMicMuted() {
        try {
            new WebDriverWait(driver,10)
                    .until(d -> getOverlayMicState().equalsIgnoreCase("Unmute"));
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public boolean waitForOverlayMicUnmuted() {
        try {
            new WebDriverWait(driver,10)
                    .until(d -> getOverlayMicState().equalsIgnoreCase("Mute"));
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public boolean isOverlayMicStateValid() {

        String state = getOverlayMicState();

        return state.equalsIgnoreCase("Mute")
                || state.equalsIgnoreCase("Unmute");
    }


    private boolean waitForText(By locator, int timeoutSeconds, String label) {
        try {WebElement el = new WebDriverWait(driver,timeoutSeconds)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));

            System.out.println("[Chat] " + label + " message found: \"" + el.getAttribute("Name") + "\"");
            return true;

        } catch (Exception e) {
            System.out.println("[Chat] " + label + " message NOT found within " + timeoutSeconds + "s");
            return false;
        }
    }

    private final By recordingStartedText =
            By.xpath("//*[contains(@Name,'started recording')]");

    private final By recordingStoppedText =
            By.xpath("//*[contains(@Name,'stopped recording')]");

    private final By recordingSavedText =
            By.xpath("//*[contains(@Name,'Recording has been saved')]");

    public boolean waitForRecordingStartedMessage(int timeoutSeconds) {
        return waitForChatText(recordingStartedText, timeoutSeconds, "recording-started");
    }

    public boolean waitForRecordingStoppedMessage(int timeoutSeconds) {
        return waitForChatText(recordingStoppedText, timeoutSeconds, "recording-stopped");
    }

    public boolean waitForRecordingSavedMessage(int timeoutSeconds) {
        return waitForChatText(recordingSavedText, timeoutSeconds, "recording-saved");
    }

    private boolean waitForChatText(By locator, int timeoutSeconds, String label) {
        try {
            WebElement el = new WebDriverWait(driver, timeoutSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            System.out.println("[Chat] " + label + " message found: \"" + el.getAttribute("Name") + "\"");
            return true;
        } catch (Exception e) {
            System.out.println("[Chat] " + label + " message NOT found within " + timeoutSeconds + "s");
            return false;
        }
    }
}

