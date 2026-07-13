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

        MeetingOverlayPage overlay = joinMeeting();

        overlay.clickPeopleButton();
        Assert.assertTrue(overlay.waitForPeoplePanelOpened());

        overlay.clickPeopleButton();

        System.out.println("Waiting 30 seconds...");
        Thread.sleep(30000);

// Remote participant joins during this time.

        clickAdmitFromRoot();

        Thread.sleep(5000);

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened());

        System.out.println("In Meeting Count = " +
                driver.findElements(
                        By.xpath("//*[contains(@Name,'In this meeting')]")
                ).size());

        System.out.println("Navya Count = " +
                driver.findElements(
                        By.name("Navya Kanugula")
                ).size());

        Assert.fail("Debug");
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