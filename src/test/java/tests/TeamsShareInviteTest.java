package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

        String shareInvitationHandle = null;

        for (int i = 0; i < 10; i++) {

            Thread.sleep(1000);

            switchToDesktop();

            shareInvitationHandle =
                    WindowHelper.findWindowHandle("Share meeting invite");

            if (shareInvitationHandle != null) {
                break;
            }
        }

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found");

        System.out.println("Share Invite Handle = " + shareInvitationHandle);

        attachByHandle(shareInvitationHandle);


        ShareInvitePage shareInvite = new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        shareInvite.clickCloseButton();

        System.out.println("✓ Share Invite screen closed");



        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        System.out.println("Blocker Handle = " + blockerHandle);

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Failed to return to meeting screen");

        System.out.println("✓ Returned to meeting screen");
        System.out.println("TC_021 PASSED");

    }

    @Test(priority = 22)
    public void TC_022_VerifySendInviteWithValidEmail() throws Exception {

        System.out.println("=== TC_022: Verify Send Invite With Valid Email ===");

        // Join meeting
        MeetingOverlayPage overlay = joinMeeting();

        // Open People panel
        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        // Attach to Microsoft Teams window
        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        Assert.assertNotNull(
                teamsHandle,
                "Microsoft Teams window not found");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        // Click Share Invite
        teamsOverlay.clickShareInviteButton();

        Thread.sleep(3000);

        // Attach to Share Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite");

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found");

        attachByHandle(shareInvitationHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        // Verify Share Invite screen
        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        // Enter email
        shareInvite.enterRecipientEmail("test@example.com");

        // Click Send Invite
        shareInvite.clickSendInvite();

        Thread.sleep(3000);

        // Return to meeting screen
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blockerHandle,
                "Failed to return to meeting screen");

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed");

        System.out.println("✓ Returned to meeting screen");

        System.out.println("TC_022 PASSED");
    }

    @Test(priority = 23)
    public void TC_023_VerifySendInviteWithoutEmail() throws Exception {

        System.out.println("=== TC_023: Empty Email Validation ===");

        // Join meeting
        MeetingOverlayPage overlay = joinMeeting();

        // Open People panel
        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        // Attach to Microsoft Teams window
        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        Assert.assertNotNull(
                teamsHandle,
                "Microsoft Teams window not found");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        // Click Share Invite
        teamsOverlay.clickShareInviteButton();

        Thread.sleep(3000);

        // Attach to Share Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite");

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found");

        attachByHandle(shareInvitationHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        // Verify Share Invite screen
        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        // Click Send Invite without entering email
        shareInvite.clickSendInvite();

        Thread.sleep(2000);

        Thread.sleep(3000);

        String message = shareInvite.getValidationMessage();

        Assert.assertEquals(
                message,
                "Enter at least one email address.",
                "Incorrect validation message");

        System.out.println("✓ Empty email validation displayed");

        // Close Share Invite screen
        shareInvite.clickCloseButton();

        System.out.println("✓ Share Invite screen closed");

        // Return to meeting screen
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blockerHandle,
                "Failed to return to meeting screen");

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed");

        System.out.println("✓ Returned to meeting screen");

        System.out.println("TC_023 PASSED");
    }
    @Test(priority = 24)
    public void TC_024_VerifyInvalidEmailValidation() throws Exception {

        System.out.println("=== TC_024: Invalid Email Validation ===");

        // Join meeting
        MeetingOverlayPage overlay = joinMeeting();

        // Open People panel
        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        // Attach to Microsoft Teams window
        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        Assert.assertNotNull(
                teamsHandle,
                "Microsoft Teams window not found");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        // Click Share Invite
        teamsOverlay.clickShareInviteButton();

        Thread.sleep(3000);

        // Attach to Share Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite");

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found");

        attachByHandle(shareInvitationHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        // Verify Share Invite screen
        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        // Enter invalid email
        shareInvite.enterRecipientEmail("abc");

        // Click Send Invite
        shareInvite.clickSendInvite();
        Assert.assertTrue(
                shareInvite.isValidationMessageDisplayed(),
                "Validation message not displayed");

        String message = shareInvite.getValidationMessage();

        Assert.assertTrue(
                message.contains("Invalid email"),
                "Incorrect validation message");

        Thread.sleep(2000);

        // Verify Share Invite screen is still displayed
        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Invalid email was accepted");

        System.out.println("✓ Invalid email validation displayed");

        // Close Share Invite screen
        shareInvite.clickCloseButton();

        System.out.println("✓ Share Invite screen closed");

        // Return to meeting screen
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blockerHandle,
                "Failed to return to meeting screen");

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed");

        System.out.println("✓ Returned to meeting screen");

        System.out.println("TC_024 PASSED");
    }

    @Test(priority = 25)
    public void TC_025_VerifyCloseShareInviteScreen() throws Exception {

        System.out.println("=== TC_025: Close Share Invite ===");

        // Join meeting
        MeetingOverlayPage overlay = joinMeeting();

        // Open People panel
        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        // Attach to Microsoft Teams window
        switchToDesktop();

        String teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams");

        Assert.assertNotNull(
                teamsHandle,
                "Microsoft Teams window not found");

        attachByHandle(teamsHandle);

        MeetingOverlayPage teamsOverlay =
                new MeetingOverlayPage(driver);

        // Click Share Invite
        teamsOverlay.clickShareInviteButton();

        Thread.sleep(3000);

        // Attach to Share Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite");

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found");

        attachByHandle(shareInvitationHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        // Verify Share Invite screen
        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        // Close Share Invite screen
        shareInvite.clickCloseButton();

        System.out.println("✓ Share Invite screen closed");

        // Return to meeting screen
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blockerHandle,
                "Failed to return to meeting screen");

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed");

        System.out.println("✓ Returned to meeting screen");

        System.out.println("TC_025 PASSED");
    }
}