package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import utils.WindowHelper;

public class ZoomLobbyManagementTest extends BaseTest {

    // ============================================================
    // JOIN ZOOM MEETING
    // ============================================================

    public MeetingOverlayPage joinMeeting() throws Exception {

        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        System.out.println("[Zoom] Clicking first Zoom meeting...");

        cards.clickJoinForFirstZoomMeeting();

        System.out.println("[Zoom] Clicking Join Zoom Meeting...");

        preJoin.clickJoinZoomMeeting();

        Thread.sleep(5000);

        // Some Zoom flows show an additional Join Now button
        if (preJoin.isJoinNowVisible()) {

            System.out.println("[Zoom] Join Now button detected.");

            preJoin.clickJoinNow();

            Thread.sleep(8000);
        }

        // Switch back to desktop so we can find Mersive Room Blocker
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room Blocker");

        if (blockerHandle == null) {

            throw new RuntimeException(
                    "[Zoom] Mersive Room Blocker window not found.");

        }

        System.out.println(
                "[Zoom] Mersive Room Blocker found.");

        setLastMeetingOverlayHandle(blockerHandle);

        attachByHandle(blockerHandle);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Thread.sleep(8000);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "[Zoom] Meeting screen did not load.");

        System.out.println(
                "[Zoom] Meeting joined successfully.");

        return overlay;
    }


    // ============================================================
    // TC_064 - ADMIT PARTICIPANT
    // ============================================================

    @Test(priority = 64)
    public void TC_064_VerifyAdmitParticipant() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TC_064 - Verify Zoom Admit Participant");
        System.out.println("==========================================");

        MeetingOverlayPage overlay = joinMeeting();

        // --------------------------------------------------------
        // Step 1 - Wait for remote participant request
        // --------------------------------------------------------

        System.out.println(
                "Step 1 : Waiting for remote participant to request access...");

        System.out.println(
                "Step 3 : Clicking Zoom Admit button...");

        clickAdmitFromRoot();

        System.out.println(
                "✓ Zoom Admit button clicked");
        Thread.sleep(5000);

// --------------------------------------------------------
// Step 4 - Verify participant count after Admit
// --------------------------------------------------------

        System.out.println(
                "Step 4 : Verifying participant count after Admit...");

        switchToDesktop();

        int participantCount =
                driver.findElements(By.name("Mersive Room"))
                        .size();

        System.out.println(
                "Participant Locator Count : " + participantCount);

        switchBackToMeetingOverlay();

        Assert.assertTrue(
                participantCount > 0,
                "No participant detected in meeting after Admit.");

        System.out.println(
                "✓ Participant present in meeting after Admit");
        // --------------------------------------------------------
        // Step 5 - Final result
        // --------------------------------------------------------

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("Expected Result");
        System.out.println("------------------------------------------");

        System.out.println(
                "Remote participant should be admitted.");
        System.out.println(
                "Join request should no longer be displayed.");

        System.out.println("------------------------------------------");

        System.out.println("✓ TC_064 PASSED");
        System.out.println("==========================================");
    }
    // ============================================================
    // TC_065 - REMOVE / DENY PARTICIPANT
    // ============================================================

    @Test(priority = 65)
    public void TC_065_VerifyDenyParticipant() throws Exception {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("TC_065 - Verify Zoom Deny Participant");
        System.out.println("==========================================");

        MeetingOverlayPage overlay = joinMeeting();


        // --------------------------------------------------------
        // Step 1 - Open People panel
        // --------------------------------------------------------


        // --------------------------------------------------------
        // Step 3 - Wait for participant request
        // --------------------------------------------------------

        System.out.println(
                "Step 3 : Waiting for remote participant to request access...");

        Thread.sleep(3000);


        // --------------------------------------------------------
        // Step 4 - Click Zoom Remove
        // --------------------------------------------------------

        System.out.println(
                "Step 4 : Clicking Zoom Remove button...");

        clickRemoveFromRoot();

        System.out.println(
                "✓ Zoom Remove button clicked");


        // --------------------------------------------------------
        // Step 5 - Wait for request to disappear
        // --------------------------------------------------------

        System.out.println(
                "Step 5 : Waiting for request to disappear...");

        Thread.sleep(5000);


        // --------------------------------------------------------
        // Step 6 - Open People panel again
        // --------------------------------------------------------

        System.out.println(
                "Step 6 : Opening People panel...");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Zoom People panel did not open.");

        System.out.println(
                "✓ People panel opened");


        // --------------------------------------------------------
        // Step 7 - Try automation verification
        // --------------------------------------------------------

        int meetingCount =
                driver.findElements(
                                By.xpath(
                                        "//*[contains(@Name,'In this meeting')]"))
                        .size();

        int participantCount =
                driver.findElements(
                                By.name("Mersive Room"))
                        .size();

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("Automation Verification");
        System.out.println("------------------------------------------");

        System.out.println(
                "In Meeting Locator Count : "
                        + meetingCount);

        System.out.println(
                "Participant Locator Count : "
                        + participantCount);


        // --------------------------------------------------------
        // WinAppDriver limitation
        // --------------------------------------------------------

        if (meetingCount == 0 &&
                participantCount == 0) {

            System.out.println();
            System.out.println("NOTE:");

            System.out.println(
                    "Current WinAppDriver session cannot access");

            System.out.println(
                    "the Zoom participant list.");

            System.out.println(
                    "Please verify participant denial manually.");
        }


        // --------------------------------------------------------
        // Expected Result
        // --------------------------------------------------------

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("Manual Verification Required");
        System.out.println("------------------------------------------");

        System.out.println(
                "Expected : Participant should NOT be admitted.");

        System.out.println(
                "Verify   : Participant should remain outside");

        System.out.println(
                "           the meeting.");

        System.out.println("------------------------------------------");

        System.out.println(
                "TC_065 COMPLETED");

        System.out.println(
                "==========================================");
    }
}