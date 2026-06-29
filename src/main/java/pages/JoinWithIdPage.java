package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.awt.*;
import java.util.List;


public class JoinWithIdPage extends BasePage {

    public JoinWithIdPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ═════════════════════════════════════════════════════════════
    // LOCATORS - XAML CONTROLS (WinAppDriver)
    // ═════════════════════════════════════════════════════════════

    // private final By homeScreen = By.name("Mersive Room");
    private final By joinWithIdBtn = By.xpath("//Button[.//Text[@Name='Join with ID']]");
    private final By meetingIdInput = By.name("e.g. 111 111 111 11 or 111 111 111");
    private final By passwordInput = By.name("Enter meeting password if required");
    private final By joinMeetingBtn = By.xpath("//Button[.//Text[contains(., 'Join meeting')]]");
    private final By chatButton = By.xpath("//Button[.//Text[@Name='Chat']]");
    private final By leaveButton = By.xpath("//Button[.//Text[@Name='Leave']]");
    private final By teamsBtn =
            By.name("Microsoft Teams");
    private final By zoomBtn = By.name("Zoom");


    // ═════════════════════════════════════════════════════════════
    // PAGE METHODS
    // ═════════════════════════════════════════════════════════════

    /**
     * Verify home screen is loaded
     */
    public void clickZoom() throws Exception {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement zoom = wait.until(
                ExpectedConditions.visibilityOfElementLocated(zoomBtn)
        );

        zoom.click();

        Thread.sleep(1000);

        System.out.println("Zoom Selected = "
                + zoom.getAttribute("SelectionItem.IsSelected"));

        System.out.println("Zoom Name = "
                + zoom.getAttribute("Name"));

        System.out.println("Zoom Enabled = "
                + zoom.isEnabled());

        if (!"True".equalsIgnoreCase(
                zoom.getAttribute("SelectionItem.IsSelected"))) {

            System.out.println("[PAGE] Zoom click failed. Clicking again...");

            zoom.click();

            Thread.sleep(1000);

            System.out.println("Zoom Selected = "
                    + zoom.getAttribute("SelectionItem.IsSelected"));
        }

        System.out.println("[PAGE] Zoom selected");
    }
    public void clickMicrosoftTeams() throws Exception {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement teams = wait.until(
                ExpectedConditions.visibilityOfElementLocated(teamsBtn)
        );

        teams.click();

        Thread.sleep(1000);

        System.out.println(
                "Teams Selected = "
                        + teams.getAttribute("SelectionItem.IsSelected")
        );

        if (!"True".equalsIgnoreCase(
                teams.getAttribute("SelectionItem.IsSelected"))) {

            System.out.println("Teams click failed. Clicking again...");

            teams.click();

            Thread.sleep(1000);

            System.out.println(
                    "Teams Selected = "
                            + teams.getAttribute("SelectionItem.IsSelected")
            );
        }
    }
    public void verifyHomeScreen() {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        joinWithIdBtn
                )
        );

        System.out.println("[PAGE] Home screen verified");
    }
    /**
     * Click "Join with ID" button
     * CORRECT XPATH: //Button[.//Text[@Name='Join with ID']]
     */
    public void clickJoinWithId() throws Exception {
        WebElement joinBtn = driver.findElement(joinWithIdBtn);
        joinBtn.click();
        System.out.println("[PAGE] Clicked Join with ID");
        Thread.sleep(2000);
    }

    /**
     * Enter Meeting ID
     */
    public void enterMeetingId(String meetingId) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        System.out.println("[PAGE] Finding Meeting ID field...");
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(meetingIdInput)
        );

        System.out.println("[PAGE] Meeting ID field found, waiting for visibility...");
        wait.until(ExpectedConditions.visibilityOf(input));

        System.out.println("[PAGE] Clicking on Meeting ID field...");
        input.click();
        Thread.sleep(300);

        System.out.println("[PAGE] Clearing Meeting ID field...");
        input.clear();
        Thread.sleep(300);

        System.out.println("[PAGE] Entering Meeting ID: " + meetingId);
        input.sendKeys(meetingId);
        Thread.sleep(500);

        System.out.println("[PAGE] Meeting ID entered successfully");
    }

    /**
     * Enter Password
     */
    public void enterPassword(String password) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 15);

        System.out.println("[PAGE] Finding Password field...");
        WebElement input = wait.until(
                ExpectedConditions.presenceOfElementLocated(passwordInput)
        );

        System.out.println("[PAGE] Password field found, waiting for visibility...");
        wait.until(ExpectedConditions.visibilityOf(input));

        System.out.println("[PAGE] Clicking on Password field...");
        input.click();
        Thread.sleep(300);

        System.out.println("[PAGE] Clearing Password field...");
        input.clear();
        Thread.sleep(300);

        System.out.println("[PAGE] Entering Password...");
        input.sendKeys(password);
        Thread.sleep(500);

        System.out.println("[PAGE] Password entered successfully");
    }

    /**
     * Click the "Done" button.
     *
     * IMPORTANT: The on-screen keyboard is a SEPARATE top-level window
     * (it shows up as "Keyboard" in the window list). The driver must already
     * be attached to that Keyboard window before this method is called, or the
     * Done button will not be found. The window switching is handled in the test.
     *
     * Done button: Name="Done", inside the Keyboard window.
     */
    public void clickDoneOnKeypad() throws Exception {
        System.out.println("[PAGE] Looking for Done button in current window...");
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Primary: by name
        try {
            WebElement done = wait.until(
                    ExpectedConditions.elementToBeClickable(By.name("Done"))
            );
            done.click();
            Thread.sleep(1000);
            System.out.println("[PAGE] ✓ Done button clicked (By.name)");
            return;
        } catch (Exception e) {
            System.out.println("[PAGE] By.name('Done') failed: " + e.getMessage());
        }

        // Fallback: by xpath @Name
        WebElement done = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//Button[@Name='Done']"))
        );
        done.click();
        Thread.sleep(1000);
        System.out.println("[PAGE] ✓ Done button clicked (XPath @Name)");
    }

    /**
     * Click "Join Meeting" button
     *
     * IMPORTANT: Button may be disabled initially, must wait for it to become enabled
     * Button structure: //Button[.//Text[contains(., 'Join meeting')]]
     * ClickablePoint: {x:959 y:915}
     */
    public void clickJoinMeetingButton() throws Exception {

        System.out.println("[PAGE] Finding Join Meeting button...");

        WebDriverWait wait = new WebDriverWait(driver, 20);

        WebElement button = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//Button[.//Text[@Name='Join meeting']]")
                )
        );

        System.out.println("Enabled = " + button.isEnabled());
        System.out.println("Displayed = " + button.isDisplayed());
        System.out.println("Location = " + button.getLocation());

        new Actions(driver)
                .moveToElement(button)
                .click()
                .perform();

        System.out.println("[PAGE] Join Meeting clicked using Actions");

        Thread.sleep(5000);
    }
    /**
     * DEBUG: List all buttons on the page
     */
    public void debugPrintAllButtons() {
        try {
            List<WebElement> allButtons = driver.findElements(By.xpath("//Button"));
            System.out.println("\n[DEBUG] ========== ALL BUTTONS ON PAGE ==========");
            System.out.println("[DEBUG] Total buttons found: " + allButtons.size());

            for (int i = 0; i < allButtons.size(); i++) {
                try {
                    String name = allButtons.get(i).getAttribute("Name");
                    String text = allButtons.get(i).getText();
                    String className = allButtons.get(i).getAttribute("ClassName");
                    boolean visible = allButtons.get(i).isDisplayed();
                    System.out.println(
                            "[DEBUG] Button " + i +
                                    ": Name='" + name +
                                    "', Location=" + allButtons.get(i).getLocation()
                    );
                    System.out.println(String.format(
                            "[DEBUG] Button %d: Name='%s', Text='%s', Class='%s', Visible=%s",
                            i, name, text, className, visible
                    ));
                } catch (Exception e) {
                    System.out.println("[DEBUG] Button " + i + ": Error reading attributes");
                }
            }
            System.out.println("[DEBUG] ==========================================\n");
        } catch (Exception e) {
            System.out.println("[DEBUG] Error listing buttons: " + e.getMessage());
        }
    }

    /**
     * Verify chat button is visible (confirms we're in the meeting)
     */
    public boolean verifyChatButton() throws Exception {
        System.out.println("[PAGE] Verifying Chat button...");
        Thread.sleep(2000);

        try {
            WebElement chat = driver.findElement(chatButton);
            boolean visible = chat.isDisplayed();
            System.out.println("[PAGE] Chat button visible: " + visible);
            return visible;
        } catch (Exception e) {
            System.out.println("[PAGE] Chat button not found: " + e.getMessage());
            return false;
        }
    }

    /**
     * Click Leave button
     */
    public void clickLeaveButton() throws Exception {
        System.out.println("[PAGE] Finding Leave button...");
        WebElement leave = driver.findElement(leaveButton);

        System.out.println("[PAGE] Leave button found, clicking...");
        leave.click();
        System.out.println("[PAGE] Leave button clicked");
    }

    /**
     * Check if home screen is loaded
     */
    public boolean isHomeScreenLoaded() {
        return !driver.findElements(
                By.xpath("//Button[.//Text[@Name='Join with ID']]")
        ).isEmpty();
    }
}