package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MeetingOverlayPage;
import pages.ShareInvitePage;
import base.BaseTest;
import utils.WindowHelper;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MeetingCardPage;
import pages.PreJoinPage;



public class TeamsShareInviteTest extends BaseTest {
    private MeetingOverlayPage joinMeeting() throws Exception {
        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        cards.clickJoinForFirstTeamsMeeting();

        new WebDriverWait(driver, 20).until(
                d -> {
                    try {
                        return new PreJoinPage(driver).isPreJoinScreenLoaded();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        preJoin.clickJoinMicrosoftTeamsMeeting();

        Thread.sleep(5000);

        if (preJoin.isJoinNowVisible()) {
            preJoin.clickJoinNow();
            Thread.sleep(8000);
        }

        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        if (blockerHandle == null) {
            throw new RuntimeException("Mersive Room Blocker not found.");
        }

        setLastMeetingOverlayHandle(blockerHandle);
        attachByHandle(blockerHandle);

        MeetingOverlayPage overlay = new MeetingOverlayPage(driver);
        Thread.sleep(8000);

        Assert.assertTrue(overlay.waitForMeetingJoinedScreen(),
                "Meeting screen did not load");
        System.out.println("✓ Meeting joined");

        return overlay;
    }

    @Test(priority = 21)
    public void TC_021_VerifyTeamsShareInviteScreen() throws Exception {

        System.out.println("=== TC_021: Verify Teams Share Invite Screen ===");

        MeetingOverlayPage overlay = joinMeeting();

        // Click People
        Assert.assertTrue(overlay.waitForPeopleButtonReady(),
                "People button not visible");
        overlay.clickPeopleButton();



        Assert.assertTrue(overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        System.out.println("Teams Handle = " + teamsHandle);


        attachByHandle(teamsHandle);

// Create a new page object after attaching
        MeetingOverlayPage teamsOverlay = new MeetingOverlayPage(driver);

        System.out.println("✓ Attached to Teams window");

// Click Share Invite
        teamsOverlay.clickShareInviteButton();
        Thread.sleep(3000);
        switchToDesktop();
        String shareInvitationhandle = WindowHelper.findWindowHandle("Share meeting invite");
        System.out.println("Share Invite Handle = "+ shareInvitationhandle);
        attachByHandle(shareInvitationhandle);

        // Verify Share Invite Screen
        ShareInvitePage shareInvite = new ShareInvitePage(driver);

        Assert.assertTrue(shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        System.out.println("TC_021 PASSED");
    }

    @Test(priority = 22)
    public void TC_022_VerifySendInviteWithValidEmail() throws Exception {

        System.out.println("=== TC_022: Verify Send Invite With Valid Email ===");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Assert.assertTrue(overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay = new MeetingOverlayPage(driver);

        teamsOverlay.clickShareInviteButton();


        ShareInvitePage shareInvite = new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        shareInvite.enterRecipientEmail("test@example.com");

        shareInvite.clickSendInvite();

        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed");

        System.out.println("TC_022 PASSED");
    }

    @Test(priority = 23)
    public void TC_023_VerifySendInviteWithoutEmail() throws Exception {

        System.out.println("=== TC_023: Empty Email Validation ===");

        MeetingOverlayPage overlay = joinMeeting();

        overlay.clickPeopleButton();
        overlay.waitForPeoplePanelOpened();

        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        teamsOverlay.clickShareInviteButton();

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        shareInvite.clickSendInvite();

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Unexpectedly left Share Invite screen");

        System.out.println("TC_023 PASSED");
    }

    @Test(priority = 24)
    public void TC_024_VerifyInvalidEmailValidation() throws Exception {

        System.out.println("=== TC_024: Invalid Email ===");

        MeetingOverlayPage overlay = joinMeeting();

        overlay.clickPeopleButton();
        overlay.waitForPeoplePanelOpened();

        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        teamsOverlay.clickShareInviteButton();

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        shareInvite.enterRecipientEmail("abc");

        shareInvite.clickSendInvite();

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Invalid email accepted");

        System.out.println("TC_024 PASSED");
    }

    @Test(priority = 25)
    public void TC_025_VerifyCloseShareInviteScreen() throws Exception {

        System.out.println("=== TC_025: Close Share Invite ===");

        MeetingOverlayPage overlay = joinMeeting();

        overlay.clickPeopleButton();
        overlay.waitForPeoplePanelOpened();

        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        teamsOverlay.clickShareInviteButton();

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        shareInvite.clickCloseButton();

        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed");

        System.out.println("TC_025 PASSED");
    }

}