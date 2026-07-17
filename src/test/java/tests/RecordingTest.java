package tests;

import base.BaseTest;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import pages.RootSessionPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

public class RecordingTest extends BaseTest {

    private MeetingOverlayPage joinMeeting() throws Exception {

        MeetingCardPage meetingCard = new MeetingCardPage(driver);

        meetingCard.clickJoinForFirstTeamsMeeting();

        PreJoinPage preJoin = new PreJoinPage(driver);

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Pre-Join screen did not appear"
        );

        preJoin.clickJoinMicrosoftTeamsMeeting();

        Thread.sleep(3000);

        try {
            preJoin.clickJoinNow();
        } catch (Exception ignored) {
        }

        switchToDesktop();

        Thread.sleep(2000);

        String blockerHandle = null;

        for (int i = 0; i < 30; i++) {

            blockerHandle =
                    WindowHelper.findWindowHandle("Mersive Room Blocker", 1);

            if (blockerHandle != null) {
                break;
            }

            System.out.println("Waiting for Blocker... " + (i + 1));
            Thread.sleep(1000);
        }

        if (blockerHandle == null) {

            WindowHelper.printAllWindows();

            throw new RuntimeException(
                    "Mersive Room Blocker not found."
            );
        }

        attachByHandle(blockerHandle);
        setLastMeetingOverlayHandle(blockerHandle);

        System.out.println("✓ Reattached to Mersive Room");

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load"
        );

        System.out.println("✓ Meeting joined");

        return overlay;
    }

    @Test(priority = 21)
    public void TC_021_VerifyRecordFunctionality() throws Exception {
        System.out.println("=== TC_021: Verify Record Functionality ===");

        MeetingOverlayPage overlay = joinMeeting();
        RootSessionPage root = new RootSessionPage(driver);

        overlay.clickRecordButton();
        System.out.println("✓ Record clicked");

        Assert.assertTrue(
                root.waitForRecordingPopup(30),
                "FAILED: Recording popup not visible"
        );
        System.out.println("✓ Recording started — popup confirmed");

        overlay.clickRecordButton();
        System.out.println("✓ Stop recording clicked");

        Thread.sleep(3000);

        throw new RuntimeException(
                "BUG: Stop Recording dialog appears, but Stop/Cancel buttons are not automatable and cause Root session to hang."
        );
    }

    @Test(priority = 22)
    public void TC_022_VerifyTranscribeFunctionality() throws Exception {
        System.out.println("=== TC_022: Verify Transcribe Functionality ===");

        MeetingOverlayPage overlay = joinMeeting();
        RootSessionPage root       = new RootSessionPage(driver);

        overlay.clickTranscribeButton();
        System.out.println("✓ Transcribe clicked");

        Assert.assertTrue(root.waitForTranscriptionPopup(30),
                "FAILED: Transcription popup not visible");
        System.out.println("✓ Transcription started — popup confirmed");

        overlay.clickTranscribeButton();
        System.out.println("✓ Stop transcription clicked");

        Assert.assertTrue(root.clickStopTranscriptionInDialog(10),
                "FAILED: Stop button not clicked");
        System.out.println("✓ Stop confirmed in dialog");

        Assert.assertTrue(root.waitForTranscriptionStoppedToast(20),
                "FAILED: Transcription stopped toast not visible");
        System.out.println("✓ Transcription stopped confirmed");

        System.out.println("TC_022 PASSED");
    }
}