package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PairingPage extends BasePage {

    public PairingPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By title =
            By.name("Pairing not detected");

    private final By fixButton =
            By.name("Fix");

    public boolean isDisplayed() {
        return waitForVisible(title, 15);
    }

    public void clickFix() {
        click(fixButton);
    }
}