package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PairSuccessPage extends BasePage {

    public PairSuccessPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By title =
            By.name("Paired successfully");

    private final By continueToPinSetupButton =
            By.name("Continue to PIN setup");

    private final By fixButton =
            By.name("Fix");

    public boolean isDisplayed() {
        return waitForVisible(title, 15);
    }

    public void clickContinueToPinSetup() {
        click(continueToPinSetupButton);
    }

    public void clickFix() {
        click(fixButton);
    }
}