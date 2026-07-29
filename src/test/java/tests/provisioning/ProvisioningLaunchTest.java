package tests.provisioning;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.provisioning.NetworkConfigurationPage;
import pages.provisioning.NoInternetPage;

public class ProvisioningLaunchTest extends BaseTest {

    @Test(priority = 1)
    public void TC_001_LaunchMersiveApplication() {

        System.out.println("========== TC_001 ==========");

        NoInternetPage noInternetPage =
                new NoInternetPage(driver);

        Assert.assertTrue(
                noInternetPage.isNoInternetScreenDisplayed(),
                "Application did not launch successfully."
        );

        System.out.println("Application launched successfully.");

    }

    @Test(priority = 2)
    public void TC_002_VerifyFixNavigatesToNetworkConfiguration() {

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);

        Assert.assertTrue(noInternet.isNoInternetScreenDisplayed());

        noInternet.clickFix();

        Assert.assertTrue(network.isDisplayed());
    }
}