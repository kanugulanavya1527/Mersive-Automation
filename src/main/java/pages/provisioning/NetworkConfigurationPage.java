package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class NetworkConfigurationPage extends BasePage {

    public NetworkConfigurationPage(RemoteWebDriver driver) {
        super(driver);
    }

    // Locators
    private final By title =
            By.name("Network Configuration");

    private final By saveAndContinueButton =
            By.name("Save & Continue");

    // Methods
    public boolean isDisplayed() {
        return waitForVisible(title, 15);
    }

    public void clickSaveAndContinue() {
        click(saveAndContinueButton);
    }
}