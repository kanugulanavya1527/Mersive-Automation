package base;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private static final String WINAPPDRIVER_URL = "http://127.0.0.1:4723/";
    private static final int IMPLICIT_WAIT = 15;

    public static RemoteWebDriver attachByHexHandle(String hexHandle) throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Windows");
        caps.setCapability("deviceName", "WindowsPC");
        caps.setCapability("appTopLevelWindow", hexHandle);
        return build(caps);
    }

    public static RemoteWebDriver createRootSession() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Windows");
        caps.setCapability("deviceName", "WindowsPC");
        caps.setCapability("app", "Root");
        return build(caps);
    }

    private static RemoteWebDriver build(DesiredCapabilities caps) throws Exception {
        RemoteWebDriver driver = new RemoteWebDriver(
                new URL(WINAPPDRIVER_URL), caps);
        driver.manage().timeouts()
                .implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        return driver;
    }
}