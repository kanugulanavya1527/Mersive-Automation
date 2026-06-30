package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ShareInvitePage extends BasePage {

    public ShareInvitePage(RemoteWebDriver driver) {
        super(driver);
    }

    private final By recipientEmailTextBox =
            By.xpath("//Edit");

    private final By sendInviteButton =
            By.name("Send invite");

    private final By closeButton =
            By.xpath("(//Button)[1]");

    private final By shareMeetingInviteTitle =
            By.name("Share meeting invite");


    public boolean isShareInviteScreenDisplayed() {

        try {

            WebElement title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            shareMeetingInviteTitle));

            System.out.println("Found Title = " + title.getText());

            return title.isDisplayed();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    public void enterRecipientEmail(String email) {

        WebElement emailBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        recipientEmailTextBox));

        emailBox.clear();
        emailBox.sendKeys(email);

        System.out.println("Recipient Email : " + email);
    }
    public void clickSendInvite() {

        WebElement sendBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        sendInviteButton));

        sendBtn.click();

        System.out.println("Send Invite button clicked");
    }

    public void clickCloseButton() {

        WebElement closeBtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(closeButton));

        closeBtn.click();

        System.out.println("Close button clicked");
    }
}