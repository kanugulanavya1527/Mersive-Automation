package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ActivationPage extends BasePage {

    public ActivationPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By title =
            By.name("Two ways to activate");

    private final By activatedButton =
            By.name("I've activated my tablet →");

    public boolean isDisplayed() {
        return waitForVisible(title, 15);
    }

    public void clickActivatedButton() {
        click(activatedButton);
    }
}