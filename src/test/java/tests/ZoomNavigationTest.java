package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import pages.MeetingCardPage;
import pages.PlatformSelectPage;
import pages.PreJoinPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ZoomNavigationTest extends BaseTest {

    @Test(priority = 32)
    public void TC_032_VerifyZoomMeetingNavigatesToPreJoin() throws InterruptedException {

        System.out.println("=== TC_032: Zoom Meeting -> PreJoin ===");

        MeetingCardPage cardPage = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        Assert.assertFalse(
                cardPage.getAllMeetings().isEmpty(),
                "No meeting cards available"
        );

        cardPage.clickJoinForFirstZoomMeeting();

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Pre-join screen not loaded"
        );

        System.out.println("TC_032 PASSED");
    }
    @Test(priority = 33)
    public void TC_033_VerifyZoomCameraToggleOnOffOn() throws Exception {

        System.out.println(
                "=== TC_033: Zoom Camera Toggle ON -> OFF -> ON ==="
        );

        HomeScreenPage home =
                new HomeScreenPage(driver);

        PlatformSelectPage platform =
                new PlatformSelectPage(driver);

        PreJoinPage preJoin =
                new PreJoinPage(driver);

        home.clickStartMeeting();

        Assert.assertTrue(
                platform.isPlatformScreenLoaded(),
                "Platform screen not loaded"
        );

        platform.clickZoom();

        Thread.sleep(3000);

        System.out.println(
                "[Test] PreJoin loaded = "
                        + preJoin.isPreJoinScreenLoaded()
        );

        Assert.assertTrue(
                preJoin.isCameraOn(),
                "Camera not initially ON"
        );

        System.out.println("✓ Camera initially ON");

        preJoin.clickCameraToggle();

        Assert.assertTrue(
                preJoin.waitForCameraOff(),
                "Camera did not turn OFF"
        );

        System.out.println("✓ Camera OFF");

        preJoin.clickCameraToggle();

        Assert.assertTrue(
                preJoin.waitForCameraOn(),
                "Camera did not turn ON again"
        );

        System.out.println("✓ Camera ON again");

        System.out.println("TC_033 PASSED");
    }

    @Test(priority = 34)
    public void TC_034_VerifyZoomMicrophoneToggleOnOffOn() throws Exception {

        System.out.println(
                "=== TC_034: Zoom Microphone Toggle ON -> OFF -> ON ==="
        );

        HomeScreenPage home =
                new HomeScreenPage(driver);

        PlatformSelectPage platform =
                new PlatformSelectPage(driver);

        PreJoinPage preJoin =
                new PreJoinPage(driver);

        home.clickStartMeeting();

        Assert.assertTrue(
                platform.isPlatformScreenLoaded(),
                "Platform screen not loaded"
        );

        platform.clickZoom();

        Thread.sleep(3000);

        Assert.assertTrue(
                preJoin.isMicrophoneOn(),
                "Microphone not initially ON"
        );

        System.out.println("✓ Microphone initially ON");

        preJoin.clickMicrophoneToggle();

        Assert.assertTrue(
                preJoin.waitForMicrophoneOff(),
                "Microphone did not turn OFF"
        );

        System.out.println("✓ Microphone OFF");

        preJoin.clickMicrophoneToggle();

        Assert.assertTrue(
                preJoin.waitForMicrophoneOn(),
                "Microphone did not turn ON again"
        );

        System.out.println("✓ Microphone ON again");

        System.out.println("TC_034 PASSED");
    }
}