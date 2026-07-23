package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class HomeScreenPage extends BasePage {

    public HomeScreenPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    private final By roomName        = By.name("InfoComm 2026");
    private final By startMeetingBtn = By.xpath("//Button[.//Text[@Name='Start a meeting']]");
    private final By joinWithIdBtn   = By.xpath("//Button[.//Text[@Name='Join with ID']]");
    private final By settingsBtn     = By.xpath("//Button[.//Text[@Name='Settings']]");

    private final By startMeetingText = By.name("Start a meeting");
    private final By joinWithIdText   = By.name("Join with ID");
    private final By settingsText     = By.name("Settings");

    private final By airPlayText    = By.name("AirPlay");
    private final By googleCastText = By.name("Google Cast");
    private final By miracastText   = By.name("Miracast");
    private final By hdmiInText     = By.name("HDMI In");



    // ── Validations ────────────────────────────────────────

    public boolean isHomeScreenLoaded() {
        return isVisible(roomName);
    }

    public boolean areBottomActionButtonsVisible() {
        return isVisible(startMeetingText)
                && isVisible(joinWithIdText)
                && isVisible(settingsText);
    }

    public boolean areCastingButtonsVisible() {
        return isVisible(airPlayText)
                && isVisible(googleCastText)
                && isVisible(miracastText)
                && isVisible(hdmiInText);
    }

    public String getDisplayedTime() {
        return findTextByPattern("\\d{1,2}:\\d{2}\\s?(AM|PM)");
    }

    public String getDisplayedDate() {
        return findTextByPattern("[A-Za-z]+,\\s[A-Za-z]+\\s\\d{1,2}");
    }

    // ── Actions ────────────────────────────────────────────

    public void clickStartMeeting() {
        click(startMeetingBtn);
    }

    public void clickJoinWithId() {
        click(joinWithIdBtn);
    }


    public boolean isSettingsButtonDisplayed() {
        return waitForPresent(settingsBtn, 10);
    }

    public void clickSettings() {
        click(settingsBtn);
    }


}


