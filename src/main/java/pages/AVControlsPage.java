package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    // ── Camera ─────────────────────────────────────────────

    public String getCameraState() {
        List<WebElement> els = driver.findElements(cameraToggleButton);
        if (els.isEmpty()) return "UNKNOWN";
        String name = els.get(0).getAttribute("Name");
        System.out.println("[AVPanel] Camera = " + name);
        return name != null ? name.trim().toUpperCase() : "UNKNOWN";
    }

    public boolean isCameraOn()  { return "ON".equals(getCameraState()); }
    public boolean isCameraOff() { return "OFF".equals(getCameraState()); }

    public void clickCameraToggle() {
        WebDriverWait w = new WebDriverWait(driver, 15);
        w.until(ExpectedConditions.elementToBeClickable(cameraToggleButton)).click();
        System.out.println("[AVPanel] Camera toggled");
    }

    public boolean waitForCameraOn() {
        try {
            return new WebDriverWait(driver, 15).until(d -> isCameraOn());
        } catch (Exception e) { return false; }
    }

    public boolean waitForCameraOff() {
        try {
            return new WebDriverWait(driver, 15).until(d -> isCameraOff());
        } catch (Exception e) { return false; }
    }

    // ── Mic ────────────────────────────────────────────────

    public String getMicState() {
        List<WebElement> els = driver.findElements(micToggleButton);
        if (els.isEmpty()) return "UNKNOWN";
        String name = els.get(0).getAttribute("Name");
        System.out.println("[AVPanel] Mic = " + name);
        return name != null ? name.trim().toUpperCase() : "UNKNOWN";
    }

    public boolean isMicOn()    { return "ON".equals(getMicState()); }
    public boolean isMicMuted() { return "MUTED".equals(getMicState()); }

    public void clickMicToggle() {
        WebDriverWait w = new WebDriverWait(driver, 15);
        w.until(ExpectedConditions.elementToBeClickable(micToggleButton)).click();
        System.out.println("[AVPanel] Mic toggled");
    }

    // ── Close ──────────────────────────────────────────────

    public void clickSwipeToClose() {
        WebDriverWait w = new WebDriverWait(driver, 10);
        w.until(ExpectedConditions.elementToBeClickable(swipeToCloseButton)).click();
        System.out.println("[AVPanel] Panel closed");
    }
}