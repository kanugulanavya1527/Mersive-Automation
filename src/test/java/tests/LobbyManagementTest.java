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

    public void TC_040_VerifyAdmitParticipant()

            throws Exception {

        System.out.println("===================================");

        System.out.println("TC_040 Verify Admit Participant");

        MeetingOverlayPage overlay =
                joinMeeting();

        System.out.println("Step 1");

        System.out.println("Opening People panel");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened());

        System.out.println("People panel opened");

        Thread.sleep(2000);

        System.out.println("Closing People panel");

        overlay.clickPeopleButton();

        System.out.println("Waiting for join request...");

        System.out.println("Waiting 30 seconds for manual join...");
        Thread.sleep(30000);

        System.out.println("Admit Count = "
                + driver.findElements(By.name("Admit")).size());

        System.out.println("Deny Count = "
                + driver.findElements(By.name("Deny")).size());

        Assert.assertTrue(

                overlay.isJoinRequestDisplayed(),

                "Admit/Deny buttons not displayed"

        );

        System.out.println("Admit & Deny buttons displayed");

        overlay.clickAdmitButton();

        System.out.println("Admit clicked");

        Assert.assertTrue(

                overlay.waitForJoinRequestToDisappear(),

                "Join request popup still visible"

        );

        System.out.println("Popup disappeared");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened());

        System.out.println("Verify participant manually");

        System.out.println("TC_040 PASSED");

        System.out.println("===================================");

    }

    @Test(priority = 41)

    public void TC_041_VerifyDenyParticipant()

            throws Exception {

        System.out.println("===================================");

        System.out.println("TC_041 Verify Deny Participant");

        MeetingOverlayPage overlay =
                joinMeeting();

        System.out.println("Opening People panel");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened());

        Thread.sleep(2000);

        System.out.println("Closing People panel");

        overlay.clickPeopleButton();

        System.out.println("Waiting for join request...");

        Assert.assertTrue(

                overlay.isJoinRequestDisplayed(),

                "Admit/Deny buttons not displayed"

        );

        System.out.println("Admit & Deny buttons displayed");

        overlay.clickDenyButton();

        System.out.println("Deny clicked");

        Assert.assertTrue(

                overlay.waitForJoinRequestToDisappear(),

                "Join request popup still visible"

        );

        System.out.println("Popup disappeared");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened());

        System.out.println("Verify participant manually");

        System.out.println("TC_041 PASSED");

        System.out.println("===================================");

    }

}