package pages;

import base.BasePage;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.ElementOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminAccessPage extends BasePage {

    public AdminAccessPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    private final By adminAccessTitle =
            By.name("Administrator access");

    private final By instructionText =
            By.name("Enter your 6-digit PIN to continue");

    private final By clrButton =
            By.name("CLR");

    private final By invalidPinMessage =
            By.name("Incorrect PIN. Please try again.");
    private By pinDigit(String digit) {
        return By.xpath("//Button[@Name='" + digit + "']");
    }

    // ── Private Helpers ────────────────────────────────────



    /**
     * Taps an element via TouchAction using ElementOption (not PointOption).
     * WinAppDriver's touch/perform endpoint requires the "element" parameter
     * on tap gestures — sending only raw x/y coordinates (PointOption)
     * triggers "Missing Command Parameter: element". ElementOption sends
     * the element id, letting WinAppDriver resolve the tap point itself.
     */
    @SuppressWarnings("unchecked")
    private void tap(WebElement element) {
        TouchAction<?> action = new TouchAction<>((PerformsTouchActions) driver);
        action.tap(ElementOption.element(element)).perform();
    }

    // ── Validations ────────────────────────────────────────

    public boolean isAdminAccessPopupDisplayed() {
        return waitForPresent(adminAccessTitle, 10)
                && waitForPresent(instructionText, 10);
    }

    public boolean isInvalidPinDisplayed() {
        return waitForPresent(invalidPinMessage, 5);
    }

    // ── Actions ────────────────────────────────────────────





    public void enterPin(String pin) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        for (char digit : pin.toCharArray()) {

            WebElement button = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            pinDigit(String.valueOf(digit))));

            button.sendKeys(Keys.ENTER);   // Since this works
            Thread.sleep(300);
        }
    }
    public void clickCLR() {
        WebElement element = driver.findElement(clrButton);
        tap(element);
    }

}