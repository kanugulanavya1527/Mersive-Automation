package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class NoInternetPage extends BasePage {

    public NoInternetPage(RemoteWebDriver driver) {
        super(driver);
    }

    // Locators
    private final By title = By.name("No network detected");

    private final By fixButton = By.name("Fix");

    private  final By exitButton =
            By.xpath("//Button[@HelpText='Quit application']");

    private  final By yesbutton = By.name("Yes, quit");

    // Methods

    public boolean isNoInternetScreenDisplayed() {
        return waitForVisible(title, 15);
    }

    public void clickFix() {
        click(fixButton);
    }

    public  void clickExit() {
        click(exitButton);
    }


    public void clickYesQuit() {
        click(yesbutton);
    }
}