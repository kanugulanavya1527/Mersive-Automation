package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DonePage extends BasePage {

    public DonePage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By title =
            By.name("Mersive Tablet is ready for use");

    private final By doneButton =
            By.name("Done");

    public boolean isDisplayed() {
        return waitForVisible(title,15);
    }

    public void clickDone() {
        click(doneButton);
    }
}