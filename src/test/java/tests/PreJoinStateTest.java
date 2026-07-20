package tests;

import base.BaseTest;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

public class PreJoinStateTest extends BaseTest {

    private MeetingOverlayPage joinWithState(
            boolean cameraOn, boolean micOn) throws Exception {

        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin   = new PreJoinPage(driver);

        cards.clickJoinForFirstTeamsMeeting();

        Assert.assertTrue(preJoin.isPreJoinScreenLoaded(),
                "Pre-join screen not loaded");

        // Set Mic first (mic must be ON before camera can be ON)
        if (micOn && preJoin.isMicrophoneOff()) {
            preJoin.clickMicrophoneToggle();
            Thread.sleep(500);
        } else if (!micOn && preJoin.isMicrophoneOn()) {
            preJoin.clickMicrophoneToggle();
            Thread.sleep(500);
        }

        // Set Camera
        if (cameraOn && preJoin.isCameraOff()) {
            preJoin.clickCameraToggle();
            Thread.sleep(500);
        } else if (!cameraOn && preJoin.isCameraOn()) {
            preJoin.clickCameraToggle();
            Thread.sleep(500);
        }

        System.out.println("✓ Pre-join state: Camera="
                + cameraOn + " Mic=" + micOn);

        preJoin.clickJoinMicrosoftTeamsMeeting();
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room Blocker");

        if (blockerHandle == null) {
            throw new RuntimeException(
                    "Mersive Room Blocker not found.");
        }

        setLastMeetingOverlayHandle(blockerHandle);
        attachByHandle(blockerHandle);

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);
        Thread.sleep(5000);

        Assert.assertTrue(overlay.waitForMeetingJoinedScreen(),
                "Meeting screen not loaded");
        System.out.println("✓ Joined meeting");
        return overlay;
    }

    @Test(priority = 23)
    public void TC_023_VerifyCameraOffAndMicOffAfterJoining()
           throws Exception {
        System.out.println("=== TC_023: Camera OFF, Mic OFF ===");

        MeetingOverlayPage overlay = joinWithState(false, false);

        Assert.assertTrue(overlay.isOverlayCameraOff(),
                "Camera is not OFF in overlay");
        System.out.println("✓ Camera OFF confirmed");

        Assert.assertTrue(overlay.isOverlayMicMuted(),
                "Mic is not OFF in overlay");
        System.out.println("✓ Mic OFF confirmed");

        System.out.println("TC_023 PASSED");
    }

    @Test(priority = 24)
    public void TC_024_VerifyCameraOnAndMicOffAfterJoining()
            throws Exception {
        System.out.println("=== TC_024: Camera ON, Mic OFF ===");

        MeetingOverlayPage overlay = joinWithState(true, false);

        Assert.assertTrue(overlay.waitForOverlayCameraOn(),
                "Camera is not ON in overlay");
        System.out.println("✓ Camera ON confirmed");

        Assert.assertTrue(overlay.isOverlayMicMuted(),
                "Mic is not OFF in overlay");
        System.out.println("✓ Mic OFF confirmed");

        System.out.println("TC_024 PASSED");
    }

    @Test(priority = 25)
    public void TC_025_VerifyCameraOffAndMicOnAfterJoining()
            throws Exception {
        System.out.println("=== TC_025: Camera OFF, Mic ON ===");

        MeetingOverlayPage overlay = joinWithState(false, true);

        Assert.assertTrue(overlay.isOverlayCameraOff(),
                "Camera is not OFF in overlay");
        System.out.println("✓ Camera OFF confirmed");

        Assert.assertTrue(overlay.isOverlayMicUnmuted(),
                "Mic is not ON in overlay");
        System.out.println("✓ Mic ON confirmed");

        System.out.println("TC_025 PASSED");
    }

    @Test(priority = 26)
    public void TC_026_VerifyCameraOnAndMicOnAfterJoining()
            throws Exception {
        System.out.println("=== TC_026: Camera ON, Mic ON ===");

        MeetingOverlayPage overlay = joinWithState(true, true);

        Assert.assertTrue(overlay.waitForOverlayCameraOn(),
                "Camera is not ON in overlay");
        System.out.println("✓ Camera ON confirmed");

        Assert.assertTrue(overlay.isOverlayMicUnmuted(),
                "Mic is not ON in overlay");
        System.out.println("✓ Mic ON confirmed");

        System.out.println("TC_026 PASSED");
    }
}