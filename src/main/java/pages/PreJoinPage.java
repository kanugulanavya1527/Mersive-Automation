package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static java.lang.Thread.sleep;

public class PreJoinPage extends BasePage {

    public PreJoinPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    private final By cameraButton      = By.xpath("//Button[.//Text[contains(@Name,'Camera')]]");
    private final By cameraOnText      = By.name("Camera  On");
    private final By cameraOffText     = By.name("Camera  Off");

    private final By microphoneButton  = By.xpath("//Button[.//Text[contains(@Name,'Microphone')]]");
    private final By microphoneOnText  = By.name("Microphone  On");
    private final By microphoneOffText = By.name("Microphone  Off");

    private final By joinTeamsMeetingBtn =
            By.xpath("//Text[@Name='Join Microsoft Teams Meeting']");
    private final By joinZoomMeetingBtn =
            By.xpath("//Text[@Name='Join Zoom Meeting']");
    private final By joinNowButton = By.name("Join now");



    // ── Validations ────────────────────────────────────────

//    public boolean isPreJoinScreenLoaded() {
//        boolean cameraVisible     = containsText("Camera");
//        boolean microphoneVisible = containsText("Microphone");
//        boolean titleVisible      = false;
//
//        List<WebElement> textBlocks =
//                driver.findElements(By.className("TextBlock"));
//
//        for (WebElement el : textBlocks) {
//            String text = el.getText().trim();
//            if (!text.isEmpty()
//                    && !text.contains("Camera")
//                    && !text.contains("Microphone")
//                    && !text.contains("Conference Room")
//                    && !text.matches(".*\\d{1,2}:\\d{2}.*")) {
//                titleVisible = true;
//                System.out.println("[PreJoinPage] Meeting title: " + text);
//                break;
//            }
//        }
//
//        return titleVisible && cameraVisible && microphoneVisible;
//    }




    private final By startTeamsMeetingBtn =
            By.name("Start a Teams meeting");

    private final By startZoomMeetingBtn =
            By.name("Start a Zoom meeting");

    public void clickStartTeamsMeeting() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 30);

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(startTeamsMeetingBtn));

        btn.click();

        Thread.sleep(6000);
    }

    public void clickStartZoomMeeting() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 30);

        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(startZoomMeetingBtn));

        btn.click();

        Thread.sleep(6000);
    }
public boolean isJoinNowVisible() {
    return isVisible(joinNowButton);
}

    public void clickJoinNow() {
        click(joinNowButton);
    }
public boolean isPreJoinScreenLoaded() throws InterruptedException {

    long end = System.currentTimeMillis() + 30000;

    while (System.currentTimeMillis() < end) {

        try {

            if (isVisible(By.name("Join Zoom Meeting"))
                    || isVisible(By.name("Start a Zoom meeting"))
                    || isVisible(By.name("Join with ID"))
                    || isVisible(By.name("Settings"))) {

                return true;
            }

        } catch (Exception ignored) {
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    return false;
}

    // ── Camera State ───────────────────────────────────────

    public boolean isCameraOn() {
        return isVisible(cameraOnText);
    }

    public boolean isCameraOff() {
        return isVisible(cameraOffText);
    }

    public boolean waitForCameraOn() {
        return waitForPresent(cameraOnText, 10);
    }

    public boolean waitForCameraOff() {
        return waitForPresent(cameraOffText, 10);
    }

    // ── Microphone State ───────────────────────────────────

    public boolean isMicrophoneOn() {
        return isVisible(microphoneOnText);
    }

    public boolean isMicrophoneOff() {
        return isVisible(microphoneOffText);
    }

    public boolean waitForMicrophoneOn() {
        return waitForPresent(microphoneOnText, 10);
    }

    public boolean waitForMicrophoneOff() {
        return waitForPresent(microphoneOffText, 10);
    }

    // ── Actions ────────────────────────────────────────────

    public void clickCameraToggle() {
        click(cameraButton);
    }

    public void clickMicrophoneToggle() {
        click(microphoneButton);
    }

    public void clickJoinMicrosoftTeamsMeeting() throws InterruptedException {
        WebDriverWait longWait = new WebDriverWait(driver, 30);
        WebElement btn = longWait.until(
                ExpectedConditions.elementToBeClickable(joinTeamsMeetingBtn));
        btn.click();
        sleep(6000);
    }

    public void clickJoinZoomMeeting() throws InterruptedException {

        WebDriverWait longWait =
                new WebDriverWait(driver, 100);

        WebElement btn =
                longWait.until(
                        ExpectedConditions.elementToBeClickable(
                                joinZoomMeetingBtn));

        System.out.println("[PreJoin] Clicking Join Zoom Meeting");

        btn.click();

        System.out.println("[PreJoin] Click completed");

        sleep(1000);

        System.out.println(
                "[PreJoin] Current page still contains Join Zoom = "
                        + !driver.findElements(joinZoomMeetingBtn).isEmpty()
        );

        sleep(9000);
    }

    // ── Pre-join name helper ───────────────────────────────

    public String getPreJoinMeetingName() {
        List<WebElement> textBlocks =
                driver.findElements(By.className("TextBlock"));
        for (WebElement el : textBlocks) {
            String text = el.getText().trim();
            if (!text.isEmpty()
                    && !text.contains("Camera")
                    && !text.contains("Microphone")
                    && !text.contains("Conference Room")
                    && !text.contains("Join")
                    && !text.matches(".*\\d{1,2}:\\d{2}.*")) {
                return text;
            }
        }
        return null;
    }
}