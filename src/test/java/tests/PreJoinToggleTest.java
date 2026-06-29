package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import pages.PlatformSelectPage;
import pages.PreJoinPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PreJoinToggleTest extends BaseTest {

    // ── Navigation helper — shared by all tests in this class ──

    private PreJoinPage navigateToPreJoin() throws InterruptedException {
        HomeScreenPage home         = new HomeScreenPage(driver);
        PlatformSelectPage platform = new PlatformSelectPage(driver);
        PreJoinPage preJoin         = new PreJoinPage(driver);

        home.clickStartMeeting();

        Assert.assertTrue(
                platform.isPlatformScreenLoaded(),
                "Platform screen not loaded"
        );

        platform.clickMicrosoftTeams();

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Pre-join screen not loaded"
        );

        System.out.println("✓ Pre-join screen loaded");
        return preJoin;
    }

    // ── Tests ──────────────────────────────────────────────

    @Test(priority = 7)
    public void TC_007_VerifyCameraToggleOnOffOn() throws InterruptedException {
        System.out.println("=== TC_007: Camera Toggle ON -> OFF -> ON ===");

        PreJoinPage preJoin = navigateToPreJoin();

        Assert.assertTrue(
                preJoin.isCameraOn(),
                "TC_007 FAILED: Camera not initially ON"
        );
        System.out.println("✓ Camera initially ON");

        preJoin.clickCameraToggle();
        Assert.assertTrue(
                preJoin.waitForCameraOff(),
                "TC_007 FAILED: Camera did not turn OFF"
        );
        System.out.println("✓ Camera turned OFF");

        preJoin.clickCameraToggle();
        Assert.assertTrue(
                preJoin.waitForCameraOn(),
                "TC_007 FAILED: Camera did not turn ON again"
        );
        System.out.println("✓ Camera turned ON again");

        System.out.println("TC_007 PASSED");
    }

    @Test(priority = 8)
    public void TC_008_VerifyMicrophoneToggleOnOffOn() throws InterruptedException {
        System.out.println("=== TC_008: Microphone Toggle ON -> OFF -> ON ===");

        PreJoinPage preJoin = navigateToPreJoin();

        Assert.assertTrue(
                preJoin.isMicrophoneOn(),
                "TC_008 FAILED: Microphone not initially ON"
        );
        System.out.println("✓ Microphone initially ON");

        preJoin.clickMicrophoneToggle();
        Assert.assertTrue(
                preJoin.waitForMicrophoneOff(),
                "TC_008 FAILED: Microphone did not turn OFF"
        );
        System.out.println("✓ Microphone turned OFF");

        preJoin.clickMicrophoneToggle();
        Assert.assertTrue(
                preJoin.waitForMicrophoneOn(),
                "TC_008 FAILED: Microphone did not turn ON again"
        );
        System.out.println("✓ Microphone turned ON again");

        System.out.println("TC_008 PASSED");
    }
}