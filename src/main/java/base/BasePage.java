package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class BasePage {

    protected static RemoteWebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(RemoteWebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, 10);
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void clickWithActions(By locator) {
        WebElement el = wait.until(
                ExpectedConditions.presenceOfElementLocated(locator));
        new Actions(driver).moveToElement(el).click().perform();
    }

    protected boolean isVisible(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected boolean waitForVisible(By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, timeoutSeconds)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) { return false; }
    }

    protected boolean waitForPresent(By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, timeoutSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) { return false; }
    }

    protected String getAttributeOf(By locator, String attribute) {
        List<WebElement> els = driver.findElements(locator);
        return els.isEmpty() ? null : els.get(0).getAttribute(attribute);
    }

    protected boolean containsText(String partialText) {
        return driver.findElements(By.className("TextBlock"))
                .stream()
                .anyMatch(el -> el.getText().trim().contains(partialText));
    }

    protected String findTextByPattern(String regex) {
        return driver.findElements(By.className("TextBlock"))
                .stream()
                .map(el -> el.getText().trim())
                .filter(t -> t.matches(regex))
                .findFirst()
                .orElse(null);
    }
}