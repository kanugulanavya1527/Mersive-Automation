package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PlatformSelectPage extends BasePage {

    public PlatformSelectPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    private final By microsoftTeamsOption = By.name("Microsoft Teams");
    private final By zoomOption           = By.name("Zoom");

    // ── Validations ────────────────────────────────────────

    public boolean isPlatformScreenLoaded() {
        return isVisible(microsoftTeamsOption)
                && isVisible(zoomOption);
    }

    // ── Actions ────────────────────────────────────────────

    public void clickMicrosoftTeams() {
        click(microsoftTeamsOption);
    }

    public void clickZoom() {

        By zoomButton =
                By.xpath("//Button[.//Text[@Name='Zoom']]");

        System.out.println(
                "Zoom buttons = "
                        + driver.findElements(zoomButton).size()
        );

        click(zoomButton);
    }
}