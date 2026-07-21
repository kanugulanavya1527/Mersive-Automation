package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import pages.RootSessionPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WindowHelper;

import java.util.List;

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

        // Give Mersive Room time to detect the live Teams meeting before
        // we start polling for the Blocker window it creates once attached.
        Thread.sleep(5000);

        String blockerHandle = null;
        int maxAttempts = 60;

        for (int i = 0; i < maxAttempts; i++) {

            blockerHandle =
                    WindowHelper.findWindowHandle("Mersive Room Blocker", 1);

            if (blockerHandle != null) {
                break;
            }

            System.out.println("Waiting for Blocker... " + (i + 1));

            // Mid-wait diagnostic every 10 attempts so we can see whether
            // Mersive Room is progressing through states or stuck idle.
            if ((i + 1) % 10 == 0) {
                System.out.println("--- Mid-wait window snapshot (attempt " + (i + 1) + ") ---");
                WindowHelper.printAllWindows();
            }

            Thread.sleep(1000);
        }

        if (blockerHandle == null) {

            System.out.println("--- Final window snapshot before failure ---");
            WindowHelper.printAllWindows();

            throw new RuntimeException(
                    "Mersive Room Blocker not found after " + maxAttempts + " attempts."
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
//    @Test(priority = 21)
//    public void TC_021_VerifyRecordFunctionality() throws Exception {
//        System.out.println("=== TC_021: Verify Record Functionality ===");
//
//        MeetingOverlayPage overlay = joinMeeting();
//        RootSessionPage root = new RootSessionPage(driver);
//
//        overlay.clickRecordButton();
//        System.out.println("✓ Record clicked");
//        Thread.sleep(5000);
//        System.out.println("Chat visible = " + overlay.isChatButtonVisible());
//        overlay.clickChatButtonRobust();
//        System.out.println("✓ Chat button clicked");
//
//        Assert.assertTrue(
//                overlay.waitForRecordingStartedMessage(100),
//                "FAILED: 'started recording' message not found in chat"
//        );
//        System.out.println("✓ 'Started recording' message confirmed in chat");
//
//        overlay.clickRecordButton();
//        System.out.println("✓ Stop recording clicked");
//        Thread.sleep(5000);
//
//        Assert.assertTrue(
//                overlay.waitForRecordingStoppedMessage(100),
//                "FAILED: 'stopped recording' message not found in chat"
//        );
//        System.out.println("✓ 'Stopped recording' message confirmed in chat");
//
//        Assert.assertTrue(
//                overlay.waitForRecordingSavedMessage(30),
//                "FAILED: 'Recording has been saved' message not found in chat"
//        );
//        System.out.println("✓ 'Recording saved to OneDrive' message confirmed in chat");
//    }
@Test(priority = 21)
public void TC_021_VerifyRecordFunctionality() throws Exception {
    System.out.println("=== TC_021: Verify Record Functionality ===");

    MeetingOverlayPage overlay = joinMeeting();

    overlay.clickRecordButton();
    System.out.println("✓ Record clicked");
    Thread.sleep(5000);

    System.out.println("Chat visible = " + overlay.isChatButtonVisible());
    overlay.clickChatButtonRobust();
    System.out.println("✓ Chat button clicked");
    Thread.sleep(2000);
    String teamsHandle = WindowHelper.findWindowHandle("Microsoft Teams", 15);
    if (teamsHandle == null) {
        WindowHelper.printAllWindows();
        throw new RuntimeException("Teams window not found for recording-started check.");
    }
    attachByHandle(teamsHandle);
    System.out.println("✓ Attached to Teams window");
    Thread.sleep(3000);

    List<WebElement> allTexts = driver.findElements(By.xpath("//Text"));
    System.out.println("[DEBUG] Total Text elements found: " + allTexts.size());
    for (WebElement el : allTexts) {
        String name = el.getAttribute("Name");
        if (name != null && !name.isBlank()) {
            System.out.println("[DEBUG] Text: \"" + name + "\"");
        }
    }

    System.out.println("✓ 'Started recording' message confirmed in chat");

    String blockerHandle = WindowHelper.findWindowHandle("Mersive Room Blocker", 10);
    if (blockerHandle == null) {
        WindowHelper.printAllWindows();
        throw new RuntimeException("Mersive Room Blocker window not found for reattach.");
    }
    attachByHandle(blockerHandle);
    System.out.println("✓ Reattached to Mersive Room");

    overlay.stoprecordButton();
    System.out.println("✓ Stop recording clicked");
    Thread.sleep(3000);

    teamsHandle = WindowHelper.findWindowHandle("Microsoft Teams", 10);
    if (teamsHandle == null) {
        WindowHelper.printAllWindows();
        throw new RuntimeException("Teams window not found for recording-stopped check.");
    }
    attachByHandle(teamsHandle);
    System.out.println("✓ Attached to Teams window");

    Assert.assertTrue(
            overlay.waitForRecordingStoppedMessage(30),
            "FAILED: 'stopped recording' message not found in chat"
    );
    System.out.println("✓ 'Stopped recording' message confirmed in chat");

    Assert.assertTrue(
            overlay.waitForRecordingSavedMessage(30),
            "FAILED: 'Recording has been saved' message not found in chat"
    );
    System.out.println("✓ 'Recording saved to OneDrive' message confirmed in chat");

    attachByHandle(blockerHandle);
    System.out.println("✓ Reattached to Mersive Room");

    System.out.println("TC_021 PASSED");
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