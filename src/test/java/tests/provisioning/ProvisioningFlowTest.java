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

    @Test(priority = 8)
    public void TC_008_VerifyNoNetworkdetectedscreen() throws Exception {

        System.out.println("===== TC_008 :Verify No Network detected screen =====");

        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);

        Assert.assertTrue(noInternetPage.isNoInternetScreenDisplayed(),
                "No Network Detected screen is not displayed.");

        // Exit application
        noInternetPage.clickExit();

        noInternetPage.clickYesQuit();

        // Relaunch application
        relaunchMersiveApp();

        // Verify resume screen
        noInternetPage = new NoInternetPage(driver);

        Assert.assertTrue(noInternetPage.isNoInternetScreenDisplayed(),
                "Application did not resume to No Network screen.");

        System.out.println("✓ TC_008 Passed");
    }
    @Test(priority = 9)
    public void TC_009_VerifyResumeFromNetworkConfigurationScreen() throws Exception {

        System.out.println("===== TC_009 : Verify Resume From Network Configuration Screen =====");

        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);

        // Navigate to Network Configuration screen
        noInternetPage.clickFix();

        Assert.assertTrue(networkPage.isDisplayed(),
                "Network Configuration screen is not displayed.");

        // Exit application
        networkPage.clickExit();
        networkPage.clickYesQuit();

        // Relaunch application
        relaunchMersiveApp();

        // Verify application resumes to No Network Detected screen
        noInternetPage = new NoInternetPage(driver);

        Assert.assertTrue(noInternetPage.isNoInternetScreenDisplayed(),
                "Application did not resume to No Network Detected screen.");

        System.out.println("✓ TC_009 Passed");
    }
    @Test(priority = 10)
    public void TC_010_VerifyPairingNotDetectedScreen() throws Exception {

        System.out.println("===== TC_010 : Verify Pairing Not Detected Screen =====");

        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);

        // Navigate to Pairing Not Detected screen
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();

        // Verify Pairing screen
        Assert.assertTrue(pairingPage.isDisplayed(),
                "Pairing Not Detected screen is not displayed.");

        // Exit application
        pairingPage.clickExit();
        pairingPage.clickYesQuit();

        // Relaunch application
        relaunchMersiveApp();

        // Verify Pairing Not Detected screen
        pairingPage = new PairingPage(driver);

        Assert.assertTrue(pairingPage.isDisplayed(),
                "Application did not resume to Pairing Not Detected screen.");

        System.out.println("✓ TC_010 Passed");
    }

    @Test(priority = 11)
    public void TC_011_VerifyTwoWaysToActivateScreen() throws Exception {

        System.out.println("===== TC_011 : Verify Two Ways To Activate Screen =====");

        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);

        // Navigate to Two Ways to Activate screen
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();
        pairingPage.clickFix();

        // Verify Activation screen
        Assert.assertTrue(activationPage.isDisplayed(),
                "Two Ways To Activate screen is not displayed.");

        // Exit application
        activationPage.clickExit();
        activationPage.clickYesQuit();

        // Relaunch application
        relaunchMersiveApp();

        // Verify application resumes to Pairing Not Detected screen
        pairingPage = new PairingPage(driver);

        Assert.assertTrue(pairingPage.isDisplayed(),
                "Application did not resume to Pairing Not Detected screen.");

        System.out.println("✓ TC_011 Passed");
    }
    @Test(priority = 12)
    public void TC_012_VerifyPairedSuccessfullyScreen() throws Exception {

        System.out.println("===== TC_012 : Verify Paired Successfully Screen =====");

        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);
        PairSuccessPage pairSuccessPage = new PairSuccessPage(driver);
        PinSetupPage pinSetupPage = new PinSetupPage(driver);

        // Navigate to Paired Successfully screen
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();
        pairingPage.clickFix();
        activationPage.clickActivatedButton();

        // Verify Paired Successfully screen
        Assert.assertTrue(pairSuccessPage.isDisplayed(),
                "Paired Successfully screen is not displayed.");

        // Exit application
        pairSuccessPage.clickExit();
        pairSuccessPage.clickYesQuit();

        // Relaunch application
        relaunchMersiveApp();

        // Verify application resumes to PIN screen


        PinNotDetectedPage pinNotDetectedPage = new PinNotDetectedPage(driver);

        Assert.assertTrue(pinNotDetectedPage.isDisplayed(),
                "Application did not resume to PIN not detected screen.");
        System.out.println("✓ TC_012 Passed");
    }

    @Test(priority = 13)
    public void TC_013_VerifyResumeFromSetupPinScreen() throws Exception {

        System.out.println("===== TC_013 : Verify Resume From Set Up PIN Code Screen =====");

        // Navigate to Set up a PIN code screen
        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);
        PairSuccessPage pairSuccessPage = new PairSuccessPage(driver);
        PinSetupPage pinSetupPage = new PinSetupPage(driver);
        // Navigate to Paired Successfully screen
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();
        pairingPage.clickFix();
        activationPage.clickActivatedButton();
        pairSuccessPage.clickContinueToPinSetup();
        pairSuccessPage.clickExit();
        pairSuccessPage.clickYesQuit();
        // Relaunch app
        relaunchMersiveApp();
        // Verify resumed screen
        PinNotDetectedPage pinNotDetectedPage = new PinNotDetectedPage(driver);
        Assert.assertTrue(
                pinNotDetectedPage.isDisplayed(),
                "Application did not resume to PIN not detected screen."
        );
    }

    @Test(priority = 14)
    public void TC_014_VerifyResumeFromConfirmCodeScreen() throws Exception {

        System.out.println("===== TC_014 : Verify Resume From Confirm Code Screen =====");

        // Navigate to Confirm Code screen
        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);
        PairSuccessPage pairSuccessPage = new PairSuccessPage(driver);
        PinSetupPage pinSetupPage = new PinSetupPage(driver);
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();
        pairingPage.clickFix();
        activationPage.clickActivatedButton();
        pairSuccessPage.clickContinueToPinSetup();
        // Go from Set up a PIN code -> Confirm code
        pinSetupPage.enterPin("123456");
        Thread.sleep(1000);
        pairSuccessPage.clickExit();
        pairSuccessPage.clickYesQuit();
        // Relaunch app
        relaunchMersiveApp();
        // Verify resumed screen
        PinNotDetectedPage pinNotDetectedPage = new PinNotDetectedPage(driver);
        Assert.assertTrue(
                pinNotDetectedPage.isDisplayed(),
                "Application did not resume to PIN not detected screen."
        );
    }

    @Test(priority = 15)
    public void TC_015_VerifyResumeFromTestAVScreen() throws Exception {

        System.out.println("===== TC_015 : Verify Resume From Test AV Screen =====");

        // Navigate to Test AV screen
        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);
        PairSuccessPage pairSuccessPage = new PairSuccessPage(driver);
        PinSetupPage pinSetupPage = new PinSetupPage(driver);
        TestAVPage testAVPage = new TestAVPage(driver);

        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();
        pairingPage.clickFix();
        activationPage.clickActivatedButton();
        pairSuccessPage.clickContinueToPinSetup();

        // Complete PIN setup to reach Test AV screen
        pinSetupPage.enterPin("123456");
        pinSetupPage.enterPin("123456");
        pairSuccessPage.clickExit();
        pinSetupPage.enterAdminPasscode();

        relaunchMersiveApp();
        testAVPage = new TestAVPage(driver);
        Assert.assertTrue(
                testAVPage.isDisplayedAfterResume(),
                "Application did not resume to Test the room's AV equipment screen."
        );
    }

    @Test(priority = 16)
    public void TC_016_VerifyResumeFromConferenceSignInScreen() throws Exception {

        System.out.println("===== TC_016 : Verify Resume From Conference Sign-In Screen =====");
        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);
        PairSuccessPage pairSuccessPage = new PairSuccessPage(driver);
        PinSetupPage pinSetupPage = new PinSetupPage(driver);
        TestAVPage testAVPage = new TestAVPage(driver);
        ConferenceSignInPage conferenceSignInPage = new ConferenceSignInPage(driver);
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();
        pairingPage.clickFix();
        activationPage.clickActivatedButton();
        pairSuccessPage.clickContinueToPinSetup();
        pinSetupPage.enterPin("123456");
        pinSetupPage.enterPin("123456");
        testAVPage.skipCamera();
        testAVPage.skipMicrophone();
        testAVPage.skipSpeaker();
        testAVPage.clickContinue();
        Assert.assertTrue(testAVPage.isPopupDisplayed());
        testAVPage.clickPopupContinue();
        // Exit from Conference Sign-In
        conferenceSignInPage.clickExit();
        // Admin passcode
        pinSetupPage.enterAdminPasscode();
        // Relaunch
        relaunchMersiveApp();
        // Verify resume
        testAVPage = new TestAVPage(driver);
        Assert.assertTrue(
                testAVPage.isDisplayedAfterResume(),
                "Application did not resume to Test AV screen."
        );
    }

    @Test(priority = 17)
    public void TC_017_VerifyResumeFromDoneScreen() throws Exception {

        System.out.println("===== TC_017 : Verify Resume From Done Screen =====");

        NoInternetPage noInternetPage = new NoInternetPage(driver);
        NetworkConfigurationPage networkPage = new NetworkConfigurationPage(driver);
        PairingPage pairingPage = new PairingPage(driver);
        ActivationPage activationPage = new ActivationPage(driver);
        PairSuccessPage pairSuccessPage = new PairSuccessPage(driver);
        PinSetupPage pinSetupPage = new PinSetupPage(driver);
        TestAVPage testAVPage = new TestAVPage(driver);
        ConferenceSignInPage conferenceSignInPage = new ConferenceSignInPage(driver);
        DonePage donePage = new DonePage(driver);
        // Complete provisioning
        noInternetPage.clickFix();
        networkPage.clickSaveAndContinue();

        pairingPage.clickFix();
        activationPage.clickActivatedButton();

        pairSuccessPage.clickContinueToPinSetup();

        pinSetupPage.enterPin("123456");
        pinSetupPage.enterPin("123456");

        testAVPage.skipCamera();
        testAVPage.skipMicrophone();
        testAVPage.skipSpeaker();
        testAVPage.clickContinue();
        Assert.assertTrue(testAVPage.isPopupDisplayed());
        testAVPage.clickPopupContinue();
        conferenceSignInPage.clickSkip();
        Assert.assertTrue(donePage.isDisplayed());
        // Exit from Done screen
        donePage.clickExit();
        // Enter admin passcode
        pinSetupPage.enterAdminPasscode();
        // Relaunch application
        relaunchMersiveApp();
        // Recreate page object after relaunch
        donePage = new DonePage(driver);
        Assert.assertTrue(
                donePage.isDisplayed(),
                "Application did not resume to Mersive Tablet is ready for use screen."
        );
    }
}