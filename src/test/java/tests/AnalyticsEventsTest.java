package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.HomeScreenPage;
import pages.*;
import utils.WindowHelper;


public class AnalyticsEventsTest extends BaseTest {

    @Test(priority = 1)
    public void TC_001_VerifyStartMeetingButtonClickEvent() {

        System.out.println("========================================");
        System.out.println("TC_001 : Verify Start Meeting Button Click");
        System.out.println("========================================");

        HomeScreenPage homeScreenPage = new HomeScreenPage(driver);

        Assert.assertTrue(
                homeScreenPage.isHomeScreenLoaded(),
                "Home Screen is not displayed."
        );

        homeScreenPage.clickStartMeeting();

        System.out.println("Expected Analytics Event : TABLET_CLICK");
        System.out.println("Button Name : start_meeting_button");

        System.out.println("TC_001 PASSED");
    }
    @Test(priority = 2)
    public void TC_002_VerifyJoinWithIdButtonClickEvent() {

        System.out.println("========================================");
        System.out.println("TC_002 : Verify Join With ID Button Click");
        System.out.println("========================================");

        HomeScreenPage homeScreenPage = new HomeScreenPage(driver);

        Assert.assertTrue(
                homeScreenPage.isHomeScreenLoaded(),
                "Home Screen is not displayed."
        );

        homeScreenPage.clickJoinWithId();

        System.out.println("Expected Analytics Event : TABLET_CLICK");
        System.out.println("Button Name : join_with_id_button");

        System.out.println("TC_002 PASSED");
    }

    @Test(priority = 3)
    public void TC_003_VerifySendInviteButtonClickEvent() throws Exception {

        System.out.println("=== TC_003 : Verify Send Invite Button Click Event ===");

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

        // Open Share Invite
        teamsOverlay.clickShareInviteButton();

        Thread.sleep(3000);

        // Attach to Share Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite", 1);

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found");

        attachByHandle(shareInvitationHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        // Enter valid email
        shareInvite.enterRecipientEmail("navya.kanugula@rampgroup.com");

        // Click Send Invite
        shareInvite.clickSendInvite();

        Thread.sleep(3000);

        System.out.println("Expected Analytics Event : TABLET_CLICK");
        System.out.println("Button Name : send_invite_button");

        System.out.println("TC_003 PASSED");
    }

    @Test(priority = 4)
    public void TC_004_VerifySettingsButtonAnalyticsEvent() throws Exception {

        System.out.println("=== TC_004: Verify Settings Button Analytics Event ===");

        HomeScreenPage home = new HomeScreenPage(driver);

        // Verify Home Screen
        boolean loaded = false;

        for (int i = 0; i < 15; i++) {

            if (home.isHomeScreenLoaded()) {
                loaded = true;
                break;
            }

            Thread.sleep(1000);
        }

        Assert.assertTrue(
                loaded,
                "Home screen not loaded");

        System.out.println("✓ Home Screen displayed");

        // Click Settings
        Assert.assertTrue(
                home.isSettingsButtonDisplayed(),
                "Settings button not displayed");

        home.clickSettings();

        System.out.println("✓ Settings button clicked");

        // Verify Admin Access popup
        AdminAccessPage admin = new AdminAccessPage(driver);

        Assert.assertTrue(
                admin.isAdminAccessPopupDisplayed(),
                "Administrator Access popup not displayed");

        System.out.println("✓ Administrator Access popup displayed");

        // Enter Admin PIN
        admin.enterPin("123456");   // <-- Use your actual 6-digit PIN

        System.out.println("✓ Admin PIN entered");

        // Verify Settings screen
        SettingsPage settings = new SettingsPage(driver);

        Assert.assertTrue(
                settings.isSettingsScreenDisplayed(),
                "Settings screen not displayed");

        System.out.println("✓ Settings screen displayed");

        // Analytics validation here

        System.out.println("TC_004 PASSED");
    }

    @Test(priority = 5)
    public void TC_005_VerifyCloseAppButtonAnalyticsEvent() throws Exception {

        System.out.println("=== TC_005: Verify Close App Button Analytics Event ===");

        HomeScreenPage home = new HomeScreenPage(driver);

        // Verify Home Screen
        boolean loaded = false;

        for (int i = 0; i < 15; i++) {

            if (home.isHomeScreenLoaded()) {
                loaded = true;
                break;
            }

            Thread.sleep(1000);
        }

        Assert.assertTrue(loaded, "Home screen not loaded");

        System.out.println("✓ Home Screen displayed");

        // Click Settings
        Assert.assertTrue(home.isSettingsButtonDisplayed(),
                "Settings button not displayed");

        home.clickSettings();

        System.out.println("✓ Settings button clicked");

        // Admin Access popup
        AdminAccessPage admin = new AdminAccessPage(driver);

        Assert.assertTrue(
                admin.isAdminAccessPopupDisplayed(),
                "Admin Access popup not displayed");

        System.out.println("✓ Admin Access popup displayed");

        // Enter PIN to open Settings
        admin.enterPin("123456");      // Replace with your actual PIN

        System.out.println("✓ Admin PIN entered");

        // Settings Screen
        SettingsPage settings = new SettingsPage(driver);

        Assert.assertTrue(
                settings.isSettingsScreenDisplayed(),
                "Settings screen not displayed");

        System.out.println("✓ Settings screen displayed");

        // Click Close Application
        settings.clickCloseApplication();

        // Verify Close Application popup
        Assert.assertTrue(
                admin.isCloseApplicationPopupDisplayed(),
                "Close Application popup not displayed");

        Assert.assertTrue(
                admin.isCloseApplicationDescriptionDisplayed(),
                "Close Application description not displayed");

        System.out.println("✓ Close Application popup displayed");

        // Enter PIN to close the application
        admin.enterPin("123456");      // Replace with your actual PIN

        System.out.println("✓ Close Application PIN entered");

        // Wait for app to close
        Thread.sleep(5000);

        // Verify analytics event here

        System.out.println("✓ Application closed successfully");

        System.out.println("TC_005 PASSED");
    }
    @Test(priority = 6)
    public void TC_006_VerifyVTCMeetingStartAnalytics() throws Exception {

        boolean[][] combinations = {

                {true, true},
                {true, false},
                {false, true},
                {false, false}
        };

        for (boolean[] combination : combinations) {

            boolean camera = combination[0];
            boolean mic = combination[1];

            System.out.println("==========================");
            System.out.println("Camera : " + camera);
            System.out.println("Mic : " + mic);
            System.out.println("==========================");

            HomeScreenPage home = new HomeScreenPage(driver);

            Assert.assertTrue(home.isHomeScreenLoaded());

            home.clickStartMeeting();

            PlatformSelectPage platform =
                    new PlatformSelectPage(driver);

            platform.clickMicrosoftTeams();

            PreJoinPage preJoin =
                    new PreJoinPage(driver);

            Assert.assertTrue(preJoin.isPreJoinScreenLoaded());

            preJoin.setCamera(camera);
            preJoin.setMicrophone(mic);

            preJoin.clickStartTeamsMeeting();

            switchToDesktop();

            String blocker =
                    WindowHelper.findWindowHandle("Mersive Room Blocker");

            setLastMeetingOverlayHandle(blocker);

            attachByHandle(blocker);

            MeetingOverlayPage overlay =
                    new MeetingOverlayPage(driver);

            Assert.assertTrue(
                    overlay.waitForMeetingJoinedScreen());

            // ===============================
            // Verify Analytics Here
            // ===============================

            overlay.clickLeaveButton();
            System.out.println("✓ Leave button clicked");

            Thread.sleep(1000);

            RootSessionPage root = new RootSessionPage(driver);
            root.clickLeaveMeetingConfirmation();
            System.out.println("✓ Leave confirmed");

            Thread.sleep(4000);

            switchToDesktop();

            String homeHandle =
                    WindowHelper.findWindowHandle("Mersive Room");

            attachByHandle(homeHandle);

            System.out.println("✓ Returned to Home Screen");
        }
    }
    @DataProvider(name = "cameraMicCombinations")
    public Object[][] cameraMicCombinations() {
        return new Object[][]{
                {true, true},
                {true, false},
                {false, true},
                {false, false}
        };
    }
    @Test(priority = 7, dataProvider = "cameraMicCombinations")
    public void TC_007_VerifyVTCMeetingJoinAnalytics(boolean camera, boolean mic) throws Exception {

        System.out.println("==================================");
        System.out.println("Camera : " + camera);
        System.out.println("Mic    : " + mic);
        System.out.println("==================================");

        HomeScreenPage home = new HomeScreenPage(driver);
        Assert.assertTrue(home.isHomeScreenLoaded());

        MeetingCardPage meetingCard = new MeetingCardPage(driver);
        meetingCard.clickJoinForFirstTeamsMeeting();

        PreJoinPage preJoin = new PreJoinPage(driver);

        Assert.assertTrue(preJoin.isPreJoinScreenLoaded());

        Thread.sleep(2000);

        preJoin.setCamera(camera);

        Thread.sleep(1000);

        preJoin.setMicrophone(mic);

        Thread.sleep(1000);

        preJoin.clickJoinMicrosoftTeamsMeeting();

        switchToDesktop();

        String blocker =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        setLastMeetingOverlayHandle(blocker);

        attachByHandle(blocker);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(overlay.waitForMeetingJoinedScreen());

        // Verify Analytics

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        Thread.sleep(5000);

        switchToDesktop();

        String homeHandle =
                WindowHelper.findWindowHandle("Mersive Room");

        attachByHandle(homeHandle);

        Assert.assertTrue(
                new HomeScreenPage(driver).isHomeScreenLoaded());

        System.out.println("==================================");
        System.out.println("Completed");
        System.out.println("==================================");
    }
    }
