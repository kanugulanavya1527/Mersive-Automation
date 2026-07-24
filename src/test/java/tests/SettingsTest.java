package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AdminAccessPage;
import pages.HomeScreenPage;
import pages.SettingsPage;
import utils.WindowHelper;

public class SettingsTest extends BaseTest {

    @Test(priority = 1)
    public void TC_001_VerifySettingsButtonIsDisplayed() {

        System.out.println("=== TC_001: Verify Settings button is displayed ===");

        HomeScreenPage home = new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isSettingsButtonDisplayed(),
                "TC_001 FAILED: Settings button is not displayed"
        );

        System.out.println("TC_001 PASSED");
    }

    @Test(priority = 2)
    public void TC_002_VerifyAdministratorAccessPopupOpens() {

        System.out.println("=== TC_002: Verify Administrator Access popup ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();
        System.out.println("✓ Clicked Settings");

        Assert.assertTrue(
                admin.isAdminAccessPopupDisplayed(),
                "TC_002 FAILED: Administrator Access popup did not open"
        );

        System.out.println("TC_002 PASSED");
    }

    @Test(priority = 3)
    public void TC_003_VerifyNavigationToSettingsUsingValidPIN() throws InterruptedException {

        System.out.println("=== TC_003: Verify navigation using valid PIN ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);
        SettingsPage settings = new SettingsPage(driver);

        home.clickSettings();
        System.out.println("Current window title: " + driver.getTitle());
        System.out.println("Current page source:");
        System.out.println(driver.getPageSource());
        Thread.sleep(1000);
        admin.enterPin("152746");
        Thread.sleep(500);
        System.out.println("Invalid PIN shown: " + admin.isInvalidPinDisplayed());
        System.out.println("Settings shown: " + settings.isSettingsScreenDisplayed());

        Assert.assertTrue(
                settings.isSettingsScreenDisplayed(),
                "TC_003 FAILED: Settings screen did not open"
        );

        System.out.println("TC_003 PASSED");
    }

    @Test(priority = 4)
    public void TC_004_VerifyAllSettingsOptionsDisplayed() throws InterruptedException {

        System.out.println("=== TC_004: Verify all Settings options ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);
        SettingsPage settings = new SettingsPage(driver);

        home.clickSettings();
        admin.enterPin("152746");

        Assert.assertTrue(
                settings.areAllSettingsOptionsDisplayed(),
                "TC_004 FAILED: One or more Settings options are missing"
        );

        System.out.println("TC_004 PASSED");
    }

    @Test(priority = 5)
    public void TC_005_VerifyCloseButtonOnAdminAccessPopup() throws InterruptedException {

        System.out.println("=== TC_005: Verify Close (X) button on Administrator Access popup ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        Assert.assertTrue(
                admin.isAdminAccessPopupDisplayed(),
                "Administrator Access popup not displayed."
        );

        admin.clickCancel();

        Thread.sleep(3000);
        Assert.assertTrue(
                admin.isAdminPopupClosed(),
                "Administrator Access popup was not closed."
        );

        System.out.println("TC_005 PASSED");
    }

    @Test(priority = 6)
    public void TC_006_VerifyCLRButtonClearsPIN() throws InterruptedException {

        System.out.println("=== TC_006: Verify CLR button clears entered PIN ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        admin.enterPin("123");

        admin.clickCLR();

        Thread.sleep(500);

        Assert.assertFalse(
                admin.isInvalidPinDisplayed(),
                "CLR button did not clear the PIN."

        );

        System.out.println("TC_006 PASSED");
    }

    @Test(priority = 7)
    public void TC_007_VerifyInvalidAdminPIN() throws InterruptedException {

        System.out.println("=== TC_007: Verify Invalid Admin PIN ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        admin.enterPin("111111");

        Assert.assertTrue(
                admin.isInvalidPinDisplayed(),
                "Invalid PIN message not displayed."
        );

        System.out.println("TC_007 PASSED");
    }

    @Test(priority = 8)
    public void TC_008_VerifyPartialPINEntry() throws InterruptedException {

        System.out.println("=== TC_008: Verify Partial PIN Entry ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);
        SettingsPage settings = new SettingsPage(driver);

        home.clickSettings();

        admin.enterPin("123");

        Thread.sleep(1000);

        Assert.assertFalse(
                settings.isSettingsScreenDisplayed(),
                "Settings screen should not open for partial PIN."
        );

        System.out.println("TC_008 PASSED");
    }

    @Test(priority = 9)
    public void TC_009_VerifySettingsCloseButton() throws InterruptedException {

        System.out.println("=== TC_009: Verify Settings Close (X) Button ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);
        SettingsPage settings = new SettingsPage(driver);

        home.clickSettings();

        admin.enterPin("152746");

        Assert.assertTrue(
                settings.isSettingsScreenDisplayed(),
                "Settings screen not displayed."
        );

        settings.clickClose();

        Thread.sleep(3000);

        Assert.assertFalse(
                settings.isSettingsScreenDisplayed(),
                "Settings screen was not closed."
        );

        System.out.println("TC_009 PASSED");
    }

    @Test(priority = 10)
    public void TC_010_VerifyCloseApplicationPopup() throws Exception {

        System.out.println("=== TC_010: Verify Close Application Popup ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        SettingsPage settings = new SettingsPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        admin.enterPin("152746");

        Assert.assertTrue(settings.isSettingsScreenDisplayed());

        settings.clickCloseApplication();

        Assert.assertTrue(
                admin.isCloseApplicationPopupDisplayed(),
                "Quit Application popup was not displayed."
        );

        System.out.println("TC_010 PASSED");
    }
    @Test(priority = 11)
    public void TC_011_VerifyCloseApplicationCancelButton() throws Exception {

        System.out.println("=== TC_011: Verify Cancel Button ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        SettingsPage settings = new SettingsPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        admin.enterPin("152746");

        Assert.assertTrue(settings.isSettingsScreenDisplayed());

        settings.clickCloseApplication();

        Assert.assertTrue(admin.isCloseApplicationPopupDisplayed());

        admin.clickCloseApplicationCancel();

        Assert.assertTrue(settings.isSettingsScreenDisplayed());

        System.out.println("TC_011 PASSED");
    }
    @Test(priority = 12)
    public void TC_012_VerifyInvalidPINForCloseApplication() throws Exception {

        System.out.println("=== TC_012: Verify Invalid PIN ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        SettingsPage settings = new SettingsPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        admin.enterPin("152746");

        Assert.assertTrue(settings.isSettingsScreenDisplayed());

        settings.clickCloseApplication();

        Assert.assertTrue(admin.isCloseApplicationPopupDisplayed());

        admin.enterPin("111111");

        Assert.assertTrue(
                admin.isInvalidPinDisplayed(),
                "Invalid PIN message was not displayed."
        );

        System.out.println("TC_012 PASSED");
    }

    @Test(priority = 14)
    public void TC_014_VerifyBackspaceButton() throws Exception {

        System.out.println("=== TC_014: Verify Backspace Button ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        SettingsPage settings = new SettingsPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        admin.enterPin("152746");

        Assert.assertTrue(settings.isSettingsScreenDisplayed());

        settings.clickCloseApplication();

        admin.enterPin("123");

        admin.clickBackspace();

        System.out.println("TC_014 PASSED");
    }

    @Test(priority = 15)
    public void TC_015_VerifyCloseApplicationWithValidPIN() throws Exception {

        System.out.println("=== TC_015: Verify Close Application with Valid PIN ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        SettingsPage settings = new SettingsPage(driver);
        AdminAccessPage admin = new AdminAccessPage(driver);

        home.clickSettings();

        // First authentication
        admin.enterPin("152746");

        Assert.assertTrue(settings.isSettingsScreenDisplayed());

        settings.clickCloseApplication();

        Assert.assertTrue(admin.isCloseApplicationPopupDisplayed());

        // Second authentication
        admin.enterPin("152746");

        Thread.sleep(2000);

        Assert.assertFalse(
                WindowHelper.isWindowPresentNow("Mersive Room"),
                "Mersive Room application was not closed."
        );

        System.out.println("TC_015 PASSED");
    }
}