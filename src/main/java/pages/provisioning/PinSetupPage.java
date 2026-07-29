package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PinSetupPage extends BasePage {

    public PinSetupPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By setupPinTitle =
            By.name("Set up a PIN code");

    private final By confirmCodeTitle =
            By.name("Confirm code");

    public boolean isDisplayed() {
        return waitForVisible(setupPinTitle, 15);
    }

    public boolean isConfirmCodeDisplayed() {
        return waitForVisible(confirmCodeTitle, 15);
    }

    public void enterPin(String pin) {

        for (char digit : pin.toCharArray()) {
            driver.findElement(By.name(String.valueOf(digit))).click();
        }
    }

    public void clickDigit(String digit) {
        driver.findElement(By.name(digit)).click();
    }

    public void clearPin() {
        driver.findElement(By.name("CLR")).click();
    }

    public void backspace() {
        driver.findElement(By.name("←")).click();
    }
    public void enterAdminPasscode() {
        enterPin("123456");
    }
}