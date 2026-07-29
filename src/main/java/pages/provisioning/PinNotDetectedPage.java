package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PinNotDetectedPage extends BasePage {

    private final By title = By.name("PIN not detected");

    public PinNotDetectedPage(RemoteWebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return driver.findElements(title).size() > 0;
    }
}