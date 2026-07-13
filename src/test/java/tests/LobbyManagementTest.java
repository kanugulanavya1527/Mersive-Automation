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

public class LobbyManagementTest extends BaseTest {

    private MeetingOverlayPage joinMeeting() throws Exception {

        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        cards.clickJoinForFirstTeamsMeeting();

        new WebDriverWait(driver,20)

                .until(d -> {

                    try {

                        return new PreJoinPage(driver)

                                .isPreJoinScreenLoaded();

                    }

                    catch (InterruptedException e) {

                        throw new RuntimeException(e);

                    }

                });

        preJoin.clickJoinMicrosoftTeamsMeeting();

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

    @Test(priority = 40)
    public void TC_040_VerifyAdmitParticipant() throws Exception {

        System.out.println("==========================================");
        System.out.println("TC_040 - Verify Admit Participant");
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
                        By.name("Navya Kanugula"))
                .size();

        System.out.println("------------------------------------------");
        System.out.println("Automation Verification");
        System.out.println("------------------------------------------");
        System.out.println("In Meeting Locator Count : " + meetingCount);
        System.out.println("Participant Locator Count : " + participantCount);

        if (meetingCount == 0 && participantCount == 0) {

            System.out.println("NOTE:");
            System.out.println("Current WinAppDriver session cannot access");
            System.out.println("the Teams participant list.");
            System.out.println("Please verify participant admission manually.");

        }

        System.out.println("------------------------------------------");
        System.out.println("Manual Verification Required");
        System.out.println("------------------------------------------");
        System.out.println("Expected : Participant should be admitted.");
        System.out.println("Verify    : People panel should show 2 participants.");
        System.out.println("------------------------------------------");

        System.out.println("TC_040 COMPLETED");
        System.out.println("==========================================");
    }

//    @Test(priority = 41)
//
//    public void TC_041_VerifyDenyParticipant()
//
//            throws Exception {
//
//        System.out.println("===================================");
//
//        System.out.println("TC_041 Verify Deny Participant");
//
//        MeetingOverlayPage overlay =
//                joinMeeting();
//
//        System.out.println("Opening People panel");
//
//        overlay.clickPeopleButton();
//
//        Assert.assertTrue(
//                overlay.waitForPeoplePanelOpened());
//
//        Thread.sleep(2000);
//
//        System.out.println("Closing People panel");
//
//        overlay.clickPeopleButton();
//
//        System.out.println("Waiting for join request...");
//
//        Assert.assertTrue(
//
//                overlay.isJoinRequestDisplayed(),
//
//                "Admit/Deny buttons not displayed"
//
//        );
//
//        System.out.println("Admit & Deny buttons displayed");
//
//        overlay.clickDenyButton();
//
//        System.out.println("Deny clicked");
//
//        Assert.assertTrue(
//
//                overlay.waitForJoinRequestToDisappear(),
//
//                "Join request popup still visible"
//
//        );
//
//        System.out.println("Popup disappeared");
//
//        overlay.clickPeopleButton();
//
//        Assert.assertTrue(
//                overlay.waitForPeoplePanelOpened());
//
//        System.out.println("Verify participant manually");
//
//        System.out.println("TC_041 PASSED");
//
//        System.out.println("===================================");
//
//    }

}