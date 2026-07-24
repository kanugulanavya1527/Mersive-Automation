//package pages;
//
//import base.BasePage;
//import io.appium.java_client.PerformsTouchActions;
//import io.appium.java_client.TouchAction;
//import io.appium.java_client.touch.offset.ElementOption;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//public class AdminAccessPage extends BasePage {
//
//    public AdminAccessPage(RemoteWebDriver driver) {
//        super(driver);
//    }
//
//    // ── Locators ───────────────────────────────────────────
//
//    private final By adminAccessTitle =
//            By.name("Administrator access");
//
//    private final By instructionText =
//            By.name("Enter your 6-digit PIN to continue");
//
//    private final By clrButton =
//            By.name("CLR");
//
//    private final By invalidPinMessage =
//            By.name("Incorrect PIN. Please try again.");
//    private By pinDigit(String digit) {
//        return By.xpath("//Button[@Name='" + digit + "']");
//    }
//
//    private final By clrBtn =
//            By.name("CLR");
//
//    private final By invalidPinMsg =
//            By.name("Incorrect PIN. Please try again.");
//
//    private final By cancelBtn =
//            By.xpath("(//Button[.//Text[@Name='']])[1]");
//
//    private final By backspaceBtn =
//            By.xpath("(//Button[.//Text[@Name='']])[1]");
//
//    private final By adminTitle =
//            By.name("Enter your 6-digit PIN to continue");
//
//    // ── Private Helpers ────────────────────────────────────
//
//
//
//    /**
//     * Taps an element via TouchAction using ElementOption (not PointOption).
//     * WinAppDriver's touch/perform endpoint requires the "element" parameter
//     * on tap gestures — sending only raw x/y coordinates (PointOption)
//     * triggers "Missing Command Parameter: element". ElementOption sends
//     * the element id, letting WinAppDriver resolve the tap point itself.
//     */
//    @SuppressWarnings("unchecked")
//    private void tap(WebElement element) {
//        TouchAction<?> action = new TouchAction<>((PerformsTouchActions) driver);
//        action.tap(ElementOption.element(element)).perform();
//    }
//
//    // ── Validations ────────────────────────────────────────
//
//    public boolean isAdminAccessPopupDisplayed() {
//        return waitForPresent(adminAccessTitle, 10)
//                && waitForPresent(instructionText, 10);
//    }
//
//
//
//    // ── Actions ────────────────────────────────────────────
//
//
//
//
//
//    public void enterPin(String pin) throws InterruptedException {
//
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//
//        for (char digit : pin.toCharArray()) {
//
//            WebElement button = wait.until(
//                    ExpectedConditions.elementToBeClickable(
//                            pinDigit(String.valueOf(digit))));
//
//            button.sendKeys(Keys.ENTER);   // Since this works
//            Thread.sleep(300);
//        }
//    }
//
//    public void clickCLR() {
//
//        WebElement clr = new WebDriverWait(driver, 10)
//                .until(ExpectedConditions.visibilityOfElementLocated(clrBtn));
//
//        clr.sendKeys(Keys.ENTER);
//    }
//    public void clickCancel() {
//
//        WebElement cancel = new WebDriverWait(driver, 10)
//                .until(ExpectedConditions.visibilityOfElementLocated(cancelBtn));
//
//        cancel.sendKeys(Keys.ENTER);
//    }
//
//    public void clickBackspace() {
//
//        WebElement backspace = new WebDriverWait(driver, 10)
//                .until(ExpectedConditions.visibilityOfElementLocated(backspaceBtn));
//
//        backspace.sendKeys(Keys.ENTER);
//    }
//    public boolean isInvalidPinDisplayed() {
//
//        return !driver.findElements(invalidPinMsg).isEmpty();
//    }
//
//
//}

package pages;

import base.BasePage;
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

    // ==================== Locators ====================

    private final By adminAccessTitle =
            By.name("Administrator access");

    private final By instructionText =
            By.name("Enter your 6-digit PIN to continue");

    private final By clrBtn =
            By.name("CLR");

    private final By invalidPinMsg =
            By.name("Incorrect PIN. Please try again.");

    private final By cancelBtn =
            By.xpath("//Text[@Name='Enter your 6-digit PIN to continue']/following-sibling::Button[1]");

    private final By backspaceBtn =
            By.xpath("//Button[@Name='0']/following-sibling::Button[1]");

    private By pinDigit(String digit) {
        return By.xpath("//Button[@Name='" + digit + "']");
    }

    // ==================== Validations ====================

    public boolean isAdminAccessPopupDisplayed() {
        return waitForPresent(adminAccessTitle, 10)
                && waitForPresent(instructionText, 10);
    }

    public boolean isInvalidPinDisplayed() {
        return !driver.findElements(invalidPinMsg).isEmpty();
    }

    public boolean isAdminPopupClosed() {
        return driver.findElements(instructionText).isEmpty();
    }

    // ==================== Actions ====================

    public void enterPin(String pin) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        for (char digit : pin.toCharArray()) {

            WebElement button = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            pinDigit(String.valueOf(digit))));

            button.sendKeys(Keys.ENTER);

            Thread.sleep(300);
        }
    }

    public void clickCLR() {

        WebElement clr = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(clrBtn));

        clr.sendKeys(Keys.ENTER);
    }

    public void clickCancel() throws InterruptedException {

        WebElement cancel = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(cancelBtn));

        System.out.println("Cancel button found");

        Thread.sleep(3000);   // <-- Watch the popup for 3 seconds

        cancel.click();

        System.out.println("Cancel clicked");

        Thread.sleep(2000);   // <-- Watch it return to Home
    }

    public void clickBackspace() {

        WebElement backspace = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(backspaceBtn));

        backspace.sendKeys(Keys.ENTER);
    }

}