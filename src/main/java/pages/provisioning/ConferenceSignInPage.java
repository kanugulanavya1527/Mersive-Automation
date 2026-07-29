package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ConferenceSignInPage extends BasePage {

    public ConferenceSignInPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By title =
            By.name("Sign into conferencing platforms");

    private final By skipButton =
            By.name("Skip for now");

    public boolean isDisplayed() {
        return waitForVisible(title,15);
    }

    public void clickSkip() {
        click(skipButton);
    }
}