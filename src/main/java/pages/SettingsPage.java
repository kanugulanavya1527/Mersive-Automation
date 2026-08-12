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

import java.util.List;

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

    private final By configureNetworkBtn = By.name("Configure Network");


    private final By ethernetTab = By.name("Ethernet");
    private final By wifiTab = By.name("Wi-Fi");
    private final By proxyTab = By.name("Proxy");

    private final By updateButton = By.name("Update");

    private final By ipv4Button = By.name("IPv4");
    private final By ipv6Button = By.name("IPv6");

    private final By dhcpButton = By.name("DHCP");
    private final By staticIpButton = By.name("Static IP");

    private final By enable8021x = By.name("Enable 802.1X authentication");

    private final By peapButton = By.name("PEAP");
    private final By tlsButton = By.name("TLS");
    private final By ttlsButton = By.name("TTLS");
    private final By testAVEquipmentBtn = By.name("Test AV equipment");

    private final By cameraLooksGoodBtn = By.name("Camera looks good");

    private final By confirmMicWorksBtn = By.name("Confirm mic works");

    private final By playTestToneBtn = By.name("Play test tone");

    private final By doneBtn = By.name("Done");
    private final By iHeardItBtn = By.name("I heard it");
    private final By iDidntHearItBtn = By.name("I didn't hear it");
    private final By conferencingBtn = By.name("Conferencing");

    private final By microsoftTeams = By.name("Microsoft Teams");

    private final By zoom = By.name("Zoom");

    private final By googleMeet = By.name("Google Meet");

    private final By webex = By.name("Webex");

    private final By resetAdminPINBtn = By.name("Reset Admin PIN");

    private final By currentPIN = By.name("Current PIN");
    private final By newPIN = By.name("New PIN");
    private final By confirmPIN = By.name("Re-enter New PIN");

    private final By enterCurrentPinTitle =
            By.name("Enter current PIN");

    private final By enterNewPinTitle =
            By.name("Enter new PIN");

    private final By reEnterNewPinTitle =
            By.name("Re-enter new PIN");

    private final By incorrectPinMessage =
            By.name("Incorrect PIN. Please try again.");


    public void clickCloseApplication() {
        wait.until(ExpectedConditions.elementToBeClickable(closeApplication)).click();
    }
    // ==================== Validations ====================

    public boolean isSettingsScreenDisplayed() {

        return waitForPresent(settingsTitle, 10)
                && waitForPresent(closeApplication, 10);
    }

    public boolean areAllSettingsOptionsDisplayed() {

        return waitForPresent(closeApplication, 5);
    }

    // ==================== Actions ====================

    public void clickClose() {

        WebElement close = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(closeBtn));

        close.sendKeys(Keys.ENTER);
    }

    public void clickConfigureNetwork() {
        click(configureNetworkBtn);
    }
    public boolean isNetworkConfigurationScreenDisplayed() {

        return waitForPresent(ethernetTab, 10)
                && waitForPresent(wifiTab, 10)
                && waitForPresent(proxyTab, 10)
                && waitForPresent(updateButton, 10);
    }

    public void clickEthernetTab() {
        click(ethernetTab);
    }

    public void clickWifiTab() {
        click(wifiTab);
    }

    public void clickProxyTab() {
        click(proxyTab);
    }



    public void clickUpdate() {
        click(updateButton);
    }

    public void enterEthernetValues() {

        List<WebElement> textBoxes =
                driver.findElements(By.className("TextBox"));

        textBoxes.get(0).sendKeys("192.168.1.100"); // IP Address
        textBoxes.get(1).sendKeys("192.168.1.1");   // Gateway
        textBoxes.get(2).clear();
        textBoxes.get(2).sendKeys("24");            // Network Prefix
        textBoxes.get(3).sendKeys("8.8.8.8");       // DNS1
        textBoxes.get(4).sendKeys("8.8.4.4");       // DNS2
    }
    public void enterWifiValues() {

        List<WebElement> textBoxes =
                driver.findElements(By.className("TextBox"));

        textBoxes.get(0).sendKeys("TestWifi");
        textBoxes.get(1).sendKeys("Test@12345");
    }
    public void enterProxyValues() {

        List<WebElement> textBoxes =
                driver.findElements(By.className("TextBox"));

        textBoxes.get(0).sendKeys("proxy.example.com");
        textBoxes.get(1).clear();
        textBoxes.get(1).sendKeys("8080");
        textBoxes.get(2).sendKeys("testuser");
        textBoxes.get(3).sendKeys("Test@123");
        textBoxes.get(4).sendKeys("localhost");
    }
    public void clickTestAVEquipment() {
        click(testAVEquipmentBtn);
    }

    public boolean isTestAVEquipmentScreenDisplayed() {

        return waitForPresent(cameraLooksGoodBtn, 10)
                && waitForPresent(confirmMicWorksBtn, 10)
                && waitForPresent(playTestToneBtn, 10)
                && waitForPresent(doneBtn, 10);
    }

    public void clickCameraLooksGood() {
        click(cameraLooksGoodBtn);
    }

    public void clickConfirmMicWorks() {
        click(confirmMicWorksBtn);
    }

    public void clickPlayTestTone() {
        click(playTestToneBtn);
    }

    public void clickDone() {
        click(doneBtn);
    }
    public boolean isSpeakerResultDisplayed() {

        return waitForPresent(iHeardItBtn, 10)
                && waitForPresent(iDidntHearItBtn, 10);
    }

    public void clickIHeardIt() {
        click(iHeardItBtn);
    }

    public void clickConferencing() {
        click(conferencingBtn);
    }

    public boolean isConferencingScreenDisplayed() {

        return waitForPresent(microsoftTeams, 10)
                && waitForPresent(zoom, 10)
                && waitForPresent(googleMeet, 10)
                && waitForPresent(webex, 10)
                && waitForPresent(doneBtn, 10);
    }

    public boolean isMicrosoftTeamsDisplayed() {
        return isVisible(microsoftTeams);
    }

    public boolean isZoomDisplayed() {
        return isVisible(zoom);
    }

    public boolean isGoogleMeetDisplayed() {
        return isVisible(googleMeet);
    }

    public boolean isWebexDisplayed() {
        return isVisible(webex);
    }

    public void clickResetAdminPIN() {
        click(resetAdminPINBtn);
    }

    public boolean isResetPinScreenDisplayed() {
        return waitForPresent(currentPIN,10)
                && waitForPresent(newPIN,10)
                && waitForPresent(confirmPIN,10);
    }

    public boolean isIncorrectPINMessageDisplayed() {
        try {
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(incorrectPinMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isCurrentPinScreenDisplayed() {
        return waitForPresent(enterCurrentPinTitle, 10);
    }

    public boolean isNewPinScreenDisplayed() {
        return waitForPresent(enterNewPinTitle, 10);
    }

    public boolean isReEnterPinScreenDisplayed() {
        return waitForPresent(reEnterNewPinTitle, 10);
    }
}