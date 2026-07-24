//package pages;
//
//import base.BasePage;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//public class SettingsPage extends BasePage {
//
//    public SettingsPage(RemoteWebDriver driver) {
//        super(driver);
//    }
//
//    // ── Locators ───────────────────────────────────────────
//
//    private final By settingsTitle =
//            By.name("Settings");
//
//    private final By configureNetwork =
//            By.name("Configure Network");
//
//    private final By testAVEquipment =
//            By.name("Test AV equipment");
//
//    private final By conferencing =
//            By.name("Conferencing");
//
//    private final By resetAdminPin =
//            By.name("Reset Admin PIN");
//
//    private final By closeApplication =
//            By.name("Close Application");
//
//    private final By closeBtn =
//            By.xpath("(//Button[.//Text[@Name='']])[1]");
//    // ── Validations ────────────────────────────────────────
//
//    public boolean isSettingsScreenDisplayed() {
//        return waitForPresent(closeApplication, 10);
//    }
//
//    public boolean areAllSettingsOptionsDisplayed() {
//        return waitForPresent(configureNetwork, 5)
//                && waitForPresent(testAVEquipment, 5)
//                && waitForPresent(conferencing, 5)
//                && waitForPresent(resetAdminPin, 5)
//                && waitForPresent(closeApplication, 5);
//    }
//
//    public void clickClose() {
//
//        WebElement close = new WebDriverWait(driver, 10)
//                .until(ExpectedConditions.visibilityOfElementLocated(closeBtn));
//
//        close.sendKeys(Keys.ENTER);
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

public class SettingsPage extends BasePage {

    public SettingsPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ==================== Locators ====================

    private final By settingsTitle =
            By.name("Settings");

    private final By configureNetwork =
            By.name("Configure Network");

    private final By testAVEquipment =
            By.name("Test AV equipment");

    private final By conferencing =
            By.name("Conferencing");

    private final By resetAdminPin =
            By.name("Reset Admin PIN");

    private final By closeApplication =
            By.name("Close Application");

    private final By closeBtn =
            By.xpath("//Text[@Name='Settings']/following-sibling::Button[1]");



    public void clickCloseApplication() {
        wait.until(ExpectedConditions.elementToBeClickable(closeApplication)).click();
    }
    // ==================== Validations ====================

    public boolean isSettingsScreenDisplayed() {

        return waitForPresent(settingsTitle, 10)
                && waitForPresent(closeApplication, 10);
    }

    public boolean areAllSettingsOptionsDisplayed() {

        return waitForPresent(configureNetwork, 5)
                && waitForPresent(testAVEquipment, 5)
                && waitForPresent(conferencing, 5)
                && waitForPresent(resetAdminPin, 5)
                && waitForPresent(closeApplication, 5);
    }

    // ==================== Actions ====================

    public void clickClose() {

        WebElement close = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(closeBtn));

        close.sendKeys(Keys.ENTER);
    }

}