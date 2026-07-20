package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.InputEvent;
import java.time.Duration;
import java.util.List;

public class AVControlsPage extends BasePage {

    public AVControlsPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    private final By cameraToggleButton =
            By.xpath("//*[@AutomationId='CameraToggleButton']");
    private final By micToggleButton =
            By.xpath("//*[@AutomationId='MicToggleButton']");
    private final By swipeToCloseButton =
            By.xpath("//Button[.//Text[@Name='swipe to close']]");

    private final By speakerPercentage =
            By.xpath("//Text[contains(@Name,'%')]");
//    private final By speakerSlider =
//
//            By.xpath("//Pane[@Name='Audio Visual Controls']//Slider");

    // ── Camera ─────────────────────────────────────────────

    public String getCameraState() {

        WebElement button = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(cameraToggleButton));

        String state = button.getAttribute("Name");

        System.out.println("[AVPanel] Camera = " + state);

        return state == null ? "UNKNOWN" : state.trim().toUpperCase();
    }

    public boolean isCameraOn() {
        return "ON".equals(getCameraState());
    }

    public boolean isCameraOff() {
        return "OFF".equals(getCameraState());
    }

    public void clickCameraToggle() {

        WebElement button = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(cameraToggleButton));

        new Actions(driver)
                .moveToElement(button)
                .click()
                .perform();

        System.out.println("[AVPanel] Camera toggled");
    }

    public boolean waitForCameraOn() {
        try {
            return new WebDriverWait(driver, 15).until(d -> isCameraOn());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForCameraOff() {
        try {
            return new WebDriverWait(driver, 15).until(d -> isCameraOff());
        } catch (Exception e) {
            return false;
        }
    }

    // ── Mic ────────────────────────────────────────────────

    public String getMicState() {

        WebElement button = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(micToggleButton));

        String state = button.getAttribute("Name");

        System.out.println("[AVPanel] Mic = " + state);

        return state == null ? "UNKNOWN" : state.trim().toUpperCase();
    }

    public boolean isMicOn() {
        return "ON".equals(getMicState());
    }

    public boolean isMicMuted() {
        return "MUTED".equals(getMicState());
    }

    public void clickMicToggle() {

        WebElement button = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(micToggleButton));

        new Actions(driver)
                .moveToElement(button)
                .click()
                .perform();

        System.out.println("[AVPanel] Mic toggled");
    }

    // ── Close ──────────────────────────────────────────────

    public void clickSwipeToClose() {

        WebElement button = new WebDriverWait(driver, 15)
                .until(ExpectedConditions.visibilityOfElementLocated(swipeToCloseButton));

        new Actions(driver)
                .moveToElement(button)
                .click()
                .perform();

        System.out.println("[AVPanel] Panel closed");
    }




    public int getSpeakerVolume() {
        WebElement slider = getSliderElement();
        String value = slider.getAttribute("RangeValue.Value");
        return (int) Double.parseDouble(value);
    }

    private void focusSlider() {
        WebElement micButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(micToggleButton));
        micButton.sendKeys(Keys.TAB);
    }

    public void increaseSpeakerVolume() {
        focusSlider();
        WebElement slider = getSliderElement();
        for (int i = 0; i < 3; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }
    }

    public void decreaseSpeakerVolume() {
        focusSlider();
        WebElement slider = getSliderElement();
        for (int i = 0; i < 3; i++) {
            slider.sendKeys(Keys.ARROW_LEFT);
        }
    }

    private final By speakerSlider =
            By.xpath("//Pane[@Name='Audio Visual Controls']//Slider");

    private final By speakerSliderThumb =
            By.xpath("//Pane[@Name='Audio Visual Controls']//Slider//Thumb");

    private WebElement getSliderElement() {
        return new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(speakerSlider));
    }


    private void focusSlider(WebElement slider) {
        try {
            WebElement thumb = new WebDriverWait(driver, 5)
                    .until(ExpectedConditions.visibilityOfElementLocated(speakerSliderThumb));
            new Actions(driver)
                    .moveToElement(thumb)
                    .click()
                    .perform();
        } catch (TimeoutException e) {
            new Actions(driver).moveToElement(slider).click().perform();
        }
    }
    }
