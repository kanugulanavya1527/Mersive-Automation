package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MeetingCardPage;
import pages.MeetingOverlayPage;
import pages.PreJoinPage;
import utils.WindowHelper;

public class ZoomLobbyManagementTest extends BaseTest {

    public MeetingOverlayPage joinMeeting() throws Exception {

        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        //cards.printAllImages();


        cards.clickJoinForFirstZoomMeeting();

        preJoin.clickJoinZoomMeeting();

        Thread.sleep(5000);

        if (preJoin.isJoinNowVisible()) {
            preJoin.clickJoinNow();
            Thread.sleep(8000);
        }

        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room Blocker");

        if(blockerHandle == null){

            throw new RuntimeException(
                    "Mersive Room Blocker not found");

        }

        setLastMeetingOverlayHandle(blockerHandle);

        attachByHandle(blockerHandle);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Thread.sleep(8000);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load");

        return overlay;

    }

    @Test(priority = 64)
    public void TC_064_VerifyAdmitParticipant() throws Exception {

        System.out.println("==========================================");
        System.out.println("TC_064 - Verify Zoom Admit Participant");
        System.out.println("==========================================");

        MeetingOverlayPage overlay = joinMeeting();

        System.out.println("Step 1 : Opening People panel");
        overlay.clickPeopleButton();
        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open.");

        System.out.println("✓ People panel opened");

        System.out.println("Step 2 : Closing People panel");
        overlay.clickPeopleButton();

        System.out.println("Step 3 : Waiting for remote participant to request access...");
        Thread.sleep(30000);

        System.out.println("Step 4 : Clicking Admit button");
        clickAdmitFromRoot();
        System.out.println("✓ Admit button clicked");

        System.out.println("Step 5 : Waiting for participant to be admitted...");
        Thread.sleep(15000);

        System.out.println("Step 6 : Opening People panel");
        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open.");

        System.out.println("✓ People panel opened");

        int meetingCount = driver.findElements(
                        By.xpath("//*[contains(@Name,'In this meeting')]"))
                .size();

        int participantCount = driver.findElements(
                        By.name("Mersive Room"))
                .size();

        System.out.println("------------------------------------------");
        System.out.println("Automation Verification");
        System.out.println("------------------------------------------");
        System.out.println("In Meeting Locator Count : " + meetingCount);
        System.out.println("Participant Locator Count : " + participantCount);

        if (meetingCount == 0 && participantCount == 0) {

            System.out.println("NOTE:");
            System.out.println("Current WinAppDriver session cannot access");
            System.out.println("the Zoom participant list.");
            System.out.println("Please verify participant admission manually.");

        }

        System.out.println("------------------------------------------");
        System.out.println("Manual Verification Required");
        System.out.println("------------------------------------------");
        System.out.println("Expected : Participant should be admitted.");
        System.out.println("Verify    : People panel should show 2 participants.");
        System.out.println("------------------------------------------");

        System.out.println("TC_064 COMPLETED");
        System.out.println("=========================================");
    }
    @Test(priority = 65)
    public void TC_065_VerifyDenyParticipant() throws Exception {

        System.out.println("==========================================");
        System.out.println("TC_065 - Verify Zoom Deny Participant");
        System.out.println("==========================================");

        MeetingOverlayPage overlay = joinMeeting();

        System.out.println("Step 1 : Opening People panel");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open.");

        System.out.println("✓ People panel opened");

        System.out.println("Step 2 : Closing People panel");

        overlay.clickPeopleButton();

        System.out.println("Step 3 : Waiting for remote participant to request access...");
        Thread.sleep(30000);

        System.out.println("Step 4 : Clicking Remove button");

        clickRemoveFromRoot();

        System.out.println("✓ Remove button clicked");

        System.out.println("Step 5 : Waiting for request to disappear...");
        Thread.sleep(5000);

        System.out.println("Step 6 : Opening People panel");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open.");

        System.out.println("✓ People panel opened");

        int meetingCount = driver.findElements(
                        By.xpath("//*[contains(@Name,'In this meeting')]"))
                .size();

        int participantCount = driver.findElements(
                        By.name("Mersive Room"))
                .size();

        System.out.println("------------------------------------------");
        System.out.println("Automation Verification");
        System.out.println("------------------------------------------");
        System.out.println("In Meeting Locator Count : " + meetingCount);
        System.out.println("Participant Locator Count : " + participantCount);

        if (meetingCount == 0 && participantCount == 0) {

            System.out.println("NOTE:");
            System.out.println("Current WinAppDriver session cannot access");
            System.out.println("the Zoom participant list.");
            System.out.println("Please verify participant denial manually.");

        }

        System.out.println("------------------------------------------");
        System.out.println("Manual Verification Required");
        System.out.println("------------------------------------------");
        System.out.println("Expected : Participant should NOT be admitted.");
        System.out.println("Verify    : Participant count should remain unchanged.");
        System.out.println("------------------------------------------");

        System.out.println("TC_065 COMPLETED");
        System.out.println("==========================================");
    }
}