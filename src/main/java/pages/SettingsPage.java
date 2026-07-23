package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SettingsPage extends BasePage {

    public SettingsPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

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

    // ── Validations ────────────────────────────────────────

    public boolean isSettingsScreenDisplayed() {
        return waitForPresent(closeApplication, 10);
    }

    public boolean areAllSettingsOptionsDisplayed() {
        return waitForPresent(configureNetwork, 5)
                && waitForPresent(testAVEquipment, 5)
                && waitForPresent(conferencing, 5)
                && waitForPresent(resetAdminPin, 5)
                && waitForPresent(closeApplication, 5);
    }
}