package pages.provisioning;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class TestAVPage extends BasePage {

    public TestAVPage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By title = By.name("Test the room's AV equipment");

    private final By skipButtons = By.name("Skip for now");

    private final By continueButton = By.name("Continue");

    private final By popupTitle =
            By.name("No AV equipment detected");

    private final By popupContinueButton =
            By.name("Continue");

    public boolean isDisplayed() {
        return waitForVisible(title, 15);
    }

    public void skipCamera() {

        List<WebElement> buttons = driver.findElements(skipButtons);

        System.out.println("Skip buttons found : " + buttons.size());

        buttons.get(0).click();

        System.out.println("Camera skipped");
    }

    public void skipMicrophone() {

        List<WebElement> buttons = driver.findElements(skipButtons);

        System.out.println("Skip buttons found : " + buttons.size());

        buttons.get(1).click();

        System.out.println("Microphone skipped");
    }

    public void skipSpeaker() {

        List<WebElement> buttons = driver.findElements(skipButtons);

        System.out.println("Skip buttons found : " + buttons.size());

        buttons.get(2).click();

        System.out.println("Speaker skipped");
    }

    public void clickContinue() {

        System.out.println("Clicking Continue...");

        click(continueButton);
    }



    public boolean isPopupDisplayed() {
        return waitForVisible(popupTitle, 10);
    }

    public void clickPopupContinue() {

        waitForVisible(popupTitle,10);

        click(popupContinueButton);
    }
    public boolean isDisplayedAfterResume() {
        List<WebElement> elements =
                driver.findElements(By.name("Test the room's AV equipment"));

        System.out.println("Found elements = " + elements.size());

        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

}