package tests.provisioning;

import base.BaseTest;
import base.DriverFactory;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.provisioning.*;
import pages.HomeScreenPage;
import utils.WindowHelper;

public class ProvisioningFlowTest extends BaseTest {

    @Test(priority = 1)
    public void verifySaveAndContinueNavigatesToPairing() {

        System.out.println("===== Test Started: verifySaveAndContinueNavigatesToPairing =====");

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);

        System.out.println("Clicking Fix on No Internet screen...");
        noInternet.clickFix();

        System.out.println("Clicking Save & Continue...");
        network.clickSaveAndContinue();

        System.out.println("Verifying Pairing screen...");
        Assert.assertTrue(pairing.isDisplayed(),
                "Pairing screen is not displayed.");

        System.out.println("✅ Pairing screen displayed successfully.");
    }

    @Test(priority = 2)
    public void verifyPairingFixNavigatesToActivation() {

        System.out.println("===== Test Started: verifyPairingFixNavigatesToActivation =====");

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);
        ActivationPage activation = new ActivationPage(driver);

        System.out.println("Navigating to Pairing screen...");
        noInternet.clickFix();
        network.clickSaveAndContinue();

        System.out.println("Clicking Fix on Pairing screen...");
        pairing.clickFix();

        System.out.println("Verifying Activation screen...");
        Assert.assertTrue(activation.isDisplayed(),
                "Activation screen is not displayed.");

        System.out.println("✅ Activation screen displayed successfully.");
    }

    @Test(priority = 3)
    public void verifyActivationNavigatesToPairSuccess() {

        System.out.println("===== Test Started: verifyActivationNavigatesToPairSuccess =====");

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);
        ActivationPage activation = new ActivationPage(driver);
        PairSuccessPage pairSuccess = new PairSuccessPage(driver);

        System.out.println("Navigating to Activation screen...");
        noInternet.clickFix();
        network.clickSaveAndContinue();
        pairing.clickFix();

        System.out.println("Clicking I've activated my tablet...");
        activation.clickActivatedButton();

        System.out.println("Verifying Pair Success screen...");
        Assert.assertTrue(pairSuccess.isDisplayed(),
                "Pair Success screen is not displayed.");

        System.out.println("✅ Pair Success screen displayed successfully.");
    }

    @Test(priority = 4)
    public void verifyPairSuccessNavigatesToPinSetup() {

        System.out.println("===== Test Started: verifyPairSuccessNavigatesToPinSetup =====");

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);
        ActivationPage activation = new ActivationPage(driver);
        PairSuccessPage pairSuccess = new PairSuccessPage(driver);
        PinSetupPage pinSetup = new PinSetupPage(driver);

        System.out.println("Navigating to Pair Success screen...");
        noInternet.clickFix();
        network.clickSaveAndContinue();
        pairing.clickFix();
        activation.clickActivatedButton();

        System.out.println("Clicking Continue to PIN Setup...");
        pairSuccess.clickContinueToPinSetup();

        System.out.println("Verifying PIN Setup screen...");
        Assert.assertTrue(pinSetup.isDisplayed(),
                "PIN Setup screen is not displayed.");

        System.out.println("✅ PIN Setup screen displayed successfully.");
    }
    @Test(priority = 5)
    public void verifyPinSetupFlow() {

        System.out.println("===== Test Started: verifyPinSetupFlow =====");

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);
        ActivationPage activation = new ActivationPage(driver);
        PairSuccessPage pairSuccess = new PairSuccessPage(driver);
        PinSetupPage pinSetup = new PinSetupPage(driver);
        TestAVPage testAV = new TestAVPage(driver);

        // Navigate to PIN Setup
        noInternet.clickFix();
        network.clickSaveAndContinue();
        pairing.clickFix();
        activation.clickActivatedButton();
        pairSuccess.clickContinueToPinSetup();

        // Verify PIN Setup
        Assert.assertTrue(pinSetup.isDisplayed(),
                "PIN Setup screen is not displayed.");

        // Enter first PIN
        System.out.println("Entering PIN...");
        pinSetup.enterPin("123456");

        // Verify Confirm Code
        Assert.assertTrue(pinSetup.isConfirmCodeDisplayed(),
                "Confirm Code screen is not displayed.");

        // Re-enter PIN
        System.out.println("Confirming PIN...");
        pinSetup.enterPin("123456");

        // Verify Test AV
        Assert.assertTrue(testAV.isDisplayed(),
                "Test AV screen is not displayed.");

        System.out.println("✅ Test AV screen displayed successfully.");
    }

    @Test(priority = 6)
    public void verifyTestAVNavigatesToConferenceSignIn() throws InterruptedException {

        System.out.println("===== Test Started : verifyTestAVNavigatesToConferenceSignIn =====");

        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);
        ActivationPage activation = new ActivationPage(driver);
        PairSuccessPage pairSuccess = new PairSuccessPage(driver);
        PinSetupPage pinSetup = new PinSetupPage(driver);
        TestAVPage testAV = new TestAVPage(driver);
        ConferenceSignInPage conference = new ConferenceSignInPage(driver);

        noInternet.clickFix();
        network.clickSaveAndContinue();
        pairing.clickFix();
        activation.clickActivatedButton();
        pairSuccess.clickContinueToPinSetup();

        pinSetup.enterPin("123456");
        pinSetup.enterPin("123456");

        Assert.assertTrue(testAV.isDisplayed());

        testAV.skipCamera();
        Thread.sleep(1000);

        testAV.skipMicrophone();
        Thread.sleep(1000);

        testAV.skipSpeaker();
        Thread.sleep(1000);

        testAV.clickContinue();
        Assert.assertTrue(testAV.isPopupDisplayed());

// Continue from popup
        testAV.clickPopupContinue();


        Assert.assertTrue(conference.isDisplayed());

        System.out.println("✅ Conference Sign-In screen displayed successfully.");
    }

    @Test(priority = 7)
    public void ConferenceSignInNavigatestoMersivehomescreen () throws Exception {
        System.out.println("===== Test Started : ConferenceSignInNavigatestoMersivehomescreen =====");
        NoInternetPage noInternet = new NoInternetPage(driver);
        NetworkConfigurationPage network = new NetworkConfigurationPage(driver);
        PairingPage pairing = new PairingPage(driver);
        ActivationPage activation = new ActivationPage(driver);
        PairSuccessPage pairSuccess = new PairSuccessPage(driver);
        PinSetupPage pinSetup = new PinSetupPage(driver);
        TestAVPage testAV = new TestAVPage(driver);
        DonePage donePage = new DonePage(driver);
        ConferenceSignInPage conference = new ConferenceSignInPage(driver);
        noInternet.clickFix();
        network.clickSaveAndContinue();
        pairing.clickFix();
        activation.clickActivatedButton();
        pairSuccess.clickContinueToPinSetup();
        pinSetup.enterPin("123456");
        pinSetup.enterPin("123456");
        Assert.assertTrue(testAV.isDisplayed());
        testAV.skipCamera();
        testAV.skipMicrophone();
        testAV.skipSpeaker();
        testAV.clickContinue();
        Assert.assertTrue(testAV.isPopupDisplayed());
        testAV.clickPopupContinue();
        Assert.assertTrue(conference.isDisplayed());
        conference.clickSkip();
        Assert.assertTrue(donePage.isDisplayed());
        donePage.clickDone();
        System.out.println("clicked on Done button");
        Thread.sleep(3000);
        // Find the new Mersive Room window
        String handle = WindowHelper.findWindowHandle("Mersive Room");
        System.out.println("New Window Handle = " + handle);
        String hexHandle = WindowHelper.toHexHandle(handle);
        driver.quit();
        driver = DriverFactory.attachByHexHandle(hexHandle);
        HomeScreenPage homeScreen = new HomeScreenPage(driver);
        Assert.assertTrue(homeScreen.isHomeScreenLoaded(),
                "Home Screen is not displayed.");
        System.out.println("✅ Provisioning completed successfully. Home Screen loaded.");
    }
}