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
            By.id("RecipientEmailsTextBox");

    private final By sendInviteButton =
            By.name("Send invite");

    private final By closeButton =
            By.name("Close");

    private final By shareMeetingInviteTitle =
            By.name("Share meeting invite");


    public boolean isShareInviteScreenDisplayed() {
        try {
            return wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    shareMeetingInviteTitle))
                    .isDisplayed();
        } catch (Exception e) {
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
                ExpectedConditions.elementToBeClickable(
                        closeButton));

        closeBtn.click();

        System.out.println("Close button clicked");
    }
}