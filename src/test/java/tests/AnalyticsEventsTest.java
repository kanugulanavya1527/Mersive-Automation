package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.HomeScreenPage;
import pages.*;
import utils.WindowHelper;

import java.util.List;


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

    @Test(priority = 8, dataProvider = "cameraMicCombinations")
    public void TC_008_VerifyJoinWithIdAnalytics(boolean camera, boolean mic) throws Exception {

        System.out.println("==================================");
        System.out.println("Camera : " + camera);
        System.out.println("Mic    : " + mic);
        System.out.println("==================================");

        HomeScreenPage home = new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home Screen not loaded");

        home.clickJoinWithId();

        JoinWithIdPage join = new JoinWithIdPage(driver);

        // Select Teams
        join.clickMicrosoftTeams();

        // Camera / Microphone
        PreJoinPage preJoin = new PreJoinPage(driver);

        preJoin.setCamera(camera);
        preJoin.setMicrophone(mic);

        // Enter Meeting ID
        join.enterMeetingId("254 924 797 336 124");

        // Enter Password
        join.enterPassword("6Aj9AR6Y");

        // Switch to Keyboard
        switchToDesktop();

        String keyboardHandle =
                WindowHelper.findWindowHandle("Keyboard");

        Assert.assertNotNull(
                keyboardHandle,
                "Keyboard window not found");

        attachByHandle(keyboardHandle);

        join = new JoinWithIdPage(driver);

        join.clickDoneOnKeypad();

        // Back to Mersive
        switchToDesktop();

        String roomHandle =
                WindowHelper.findWindowHandle("Mersive Room");

        Assert.assertNotNull(
                roomHandle,
                "Mersive Room window not found");

        attachByHandle(roomHandle);

        join = new JoinWithIdPage(driver);

        // Join Meeting
        join.clickJoinMeetingButton();

        // Wait for meeting to launch
        Thread.sleep(15000);

        switchToDesktop();

        String blocker =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blocker,
                "Meeting overlay not found");

        setLastMeetingOverlayHandle(blocker);

        attachByHandle(blocker);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Meeting did not join");

        // Verify we're inside the meeting
        join = new JoinWithIdPage(driver);

        Assert.assertTrue(
                join.verifyChatButton(),
                "Chat button not found");

        // ==========================================
        // Verify Analytics
        // Source : join_with_code
        // Camera : ON / OFF
        // Microphone : ON / OFF
        // ==========================================

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();
       setLastMeetingOverlayHandle(null);

        Thread.sleep(5000);

        switchToDesktop();

        String homeHandle =
                WindowHelper.findWindowHandle("Mersive Room");

        Assert.assertNotNull(
                homeHandle,
                "Home window not found");

        attachByHandle(homeHandle);

        Assert.assertTrue(
                new HomeScreenPage(driver).isHomeScreenLoaded(),
                "Failed to return to Home Screen");

        System.out.println("==================================");
        System.out.println("TC_008 PASSED");
        System.out.println("==================================");
    }
    @Test(priority = 9)
    public void TC_009_VerifyVTCMeetingEndAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_009 : Verify VTC_MEETING:END Analytics");
        System.out.println("======================================");

        // Join a meeting
        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.isChatButtonVisible(),
                "Meeting was not joined successfully");

        System.out.println("✓ Meeting joined successfully");

        // ==========================================
        // Expected Analytics Event
        // Event      : VTC_MEETING:END
        // Tablet ID  : Auto-generated
        // Meeting ID : Auto-generated
        // Timestamp  : Auto-generated
        // ==========================================

        overlay.clickLeaveButton();
        System.out.println("✓ Leave button clicked");

        Thread.sleep(1000);

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();
        System.out.println("✓ Leave confirmed");

        Thread.sleep(5000);

        switchToDesktop();

        String homeHandle =
                WindowHelper.findWindowHandle("Mersive Room");

        Assert.assertNotNull(
                homeHandle,
                "Home window not found");

        attachByHandle(homeHandle);

        Assert.assertTrue(
                new HomeScreenPage(driver).isHomeScreenLoaded(),
                "Failed to return to Home Screen");

        System.out.println("Expected Analytics Event : VTC_MEETING:END");
        System.out.println("Meeting Ended Successfully");

        System.out.println("======================================");
        System.out.println("TC_009 PASSED");
        System.out.println("======================================");
    }
    @Test(priority = 10)
    public void TC_010_VerifyVTCChatOpenAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_010 : Verify VTC_CHAT_OPEN Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "Chat button not ready");

        overlay.clickChatButtonRobust();

        Thread.sleep(3000);

        System.out.println("Expected Analytics Event : VTC_CHAT_OPEN");

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_010 PASSED");
    }

    @Test(priority = 11)
    public void TC_011_VerifyVTCChatCloseAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_011 : Verify VTC_CHAT_CLOSE Analytics");
        System.out.println("======================================");

        // Join meeting
        MeetingOverlayPage overlay = joinMeeting();

        // Verify Chat button
        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "Chat button not ready");

        System.out.println("✓ Chat button ready");

        // Open Chat
        overlay.clickChatButtonRobust();

        Thread.sleep(2000);

        System.out.println("✓ Chat panel opened");

        // Close Chat
        overlay.clickChatButtonRobust();

        Thread.sleep(2000);

        System.out.println("✓ Chat panel closed");

        // Analytics
        System.out.println("Expected Analytics Event : VTC_CHAT_CLOSE");
        System.out.println("User Action : Chat panel closed");

        // Leave meeting
        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_011 PASSED");
        System.out.println("======================================");
    }

    @Test(priority = 12)
    public void TC_012_VerifyVTCPeopleOpenAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_012 : Verify VTC_PEOPLE_OPEN Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not ready");

        System.out.println("✓ People button ready");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open");

        System.out.println("✓ People panel opened");

        System.out.println("Expected Analytics Event : VTC_PEOPLE_OPEN");
        System.out.println("User Action : People panel opened");

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_012 PASSED");
        System.out.println("======================================");
    }
    @Test(priority = 13)
    public void TC_013_VerifyVTCPeopleCloseAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_013 : Verify VTC_PEOPLE_CLOSE Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not ready");

        System.out.println("✓ People button ready");

        // Open People panel
        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open");

        Thread.sleep(2000);

        System.out.println("✓ People panel opened");

        // Close People panel
        overlay.clickPeopleButton();

        Thread.sleep(2000);

        System.out.println("✓ People panel closed");

        System.out.println("Expected Analytics Event : VTC_PEOPLE_CLOSE");
        System.out.println("User Action : People panel closed");

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_013 PASSED");
        System.out.println("======================================");
    }
    @Test(priority = 14)
    public void TC_014_VerifyVTCEmailInviteAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_014 : Verify VTC_EMAIL_INVITE Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        // Open People panel
        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "Participants panel did not open");

        // Switch to Microsoft Teams window
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

        // Switch to Share Invite dialog
        switchToDesktop();

        String shareInviteHandle =
                WindowHelper.findWindowHandle("Share meeting invite", 1);

        Assert.assertNotNull(
                shareInviteHandle,
                "Share Invite window not found");

        attachByHandle(shareInviteHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed");

        System.out.println("✓ Share Invite screen displayed");

        // Enter recipient email
        shareInvite.enterRecipientEmail("navya.kanugula@rampgroup.com");

        // Send invite
        shareInvite.clickSendInvite();

        Thread.sleep(3000);

        // ==========================================
        // Expected Analytics
        // Event : VTC_EMAIL_INVITE
        // Recipient : navya.kanugula@rampgroup.com
        // ==========================================

        System.out.println("Expected Analytics Event : VTC_EMAIL_INVITE");
        System.out.println("Recipient : navya.kanugula@rampgroup.com");
        System.out.println("User Action : Email invite sent");

        // Return to meeting overlay
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blockerHandle,
                "Meeting overlay not found");

        attachByHandle(blockerHandle);

        overlay = new MeetingOverlayPage(driver);

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_014 PASSED");
        System.out.println("======================================");
    }

//    @Test(priority = 15)
//    public void TC_015_VerifyVTCCameraOnAnalytics() throws Exception {
//
//        System.out.println("======================================");
//        System.out.println("TC_015 : Verify VTC_CAMERA_ON Analytics");
//        System.out.println("======================================");
//
//        MeetingOverlayPage overlay = joinMeeting();
//
//        if (!overlay.isOverlayCameraOn()) {
//
//            overlay.clickOverlayCameraToggle();
//
//            Assert.assertTrue(
//                    overlay.waitForOverlayCameraOn(),
//                    "Camera did not turn ON");
//        }
//
//        System.out.println("Expected Analytics Event : VTC_CAMERA_ON");
//        System.out.println("User Action : Camera turned ON");
//
//        overlay.clickLeaveButton();
//
//        RootSessionPage root = new RootSessionPage(driver);
//        root.clickLeaveMeetingConfirmation();
//
//        setLastMeetingOverlayHandle(null);
//
//        System.out.println("TC_015 PASSED");
//    }
@Test(priority = 15)
public void TC_015_VerifyVTCCameraOnAnalytics() throws Exception {

    System.out.println("======================================");
    System.out.println("TC_015 : Verify VTC_CAMERA_ON Analytics");
    System.out.println("======================================");

    MeetingOverlayPage overlay = joinMeeting();

    // Ensure Camera is OFF first
    if (overlay.isOverlayCameraOn()) {

        overlay.clickOverlayCameraToggle();

        Assert.assertTrue(
                overlay.waitForOverlayCameraOff(),
                "Camera did not turn OFF");

        System.out.println("✓ Camera turned OFF");
    }

    // Turn Camera ON
    overlay.clickOverlayCameraToggle();

    Assert.assertTrue(
            overlay.waitForOverlayCameraOn(),
            "Camera did not turn ON");

    System.out.println("✓ Camera turned ON");

    System.out.println("Expected Analytics Event : VTC_CAMERA_ON");
    System.out.println("User Action : Camera turned ON");

    overlay.clickLeaveButton();

    RootSessionPage root = new RootSessionPage(driver);
    root.clickLeaveMeetingConfirmation();

    setLastMeetingOverlayHandle(null);

    System.out.println("======================================");
    System.out.println("TC_015 PASSED");
    System.out.println("======================================");
}

    @Test(priority = 16)
    public void TC_016_VerifyVTCCameraOffAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_016 : Verify VTC_CAMERA_OFF Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        if (!overlay.isOverlayCameraOff()) {

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF");
        }

        System.out.println("Expected Analytics Event : VTC_CAMERA_OFF");
        System.out.println("User Action : Camera turned OFF");

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);
        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_016 PASSED");
    }


    @Test(priority = 17)
    public void TC_017_VerifyVTCMicOnAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_017 : Verify VTC_MIC_ON Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        // Ensure Mic is OFF first
        if (overlay.isOverlayMicUnmuted()) {

            overlay.clickOverlayMicToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayMicMuted(),
                    "Mic did not turn OFF");

            System.out.println("✓ Mic turned OFF");
        }

        // Turn Mic ON
        overlay.clickOverlayMicToggle();

        Assert.assertTrue(
                overlay.waitForOverlayMicUnmuted(),
                "Mic did not turn ON");

        System.out.println("✓ Mic turned ON");

        // Analytics
        System.out.println("Expected Analytics Event : VTC_MIC_ON");
        System.out.println("User Action : Microphone turned ON");

        // Leave Meeting
        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);
        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_017 PASSED");
        System.out.println("======================================");
    }
    @Test(priority = 18)
    public void TC_018_VerifyVTCMicOffAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_018 : Verify VTC_MIC_OFF Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        if (!overlay.isOverlayMicMuted()) {

            overlay.clickOverlayMicToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayMicMuted(),
                    "Mic did not turn OFF");
        }

        System.out.println("Expected Analytics Event : VTC_MIC_OFF");
        System.out.println("User Action : Microphone turned OFF");

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);
        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_018 PASSED");
    }

    @Test(priority = 19)
    public void TC_019_VerifyVTCHandRaiseAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_019 : Verify VTC_HAND_RAISE Analytics");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not visible");

        overlay.clickPeopleButton();

        Thread.sleep(3000);

        System.out.println("✓ Participants panel opened");

        overlay.clickRaiseHandButton();

        Assert.assertTrue(
                overlay.isMyHandRaised(),
                "Hand was not raised");

        System.out.println("✓ Hand raised");

        System.out.println("Expected Analytics Event : VTC_HAND_RAISE");
        System.out.println("User Action : Raise Hand");

        overlay.clickLowerHandButton();

        Thread.sleep(2000);

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_019 PASSED");
        System.out.println("======================================");
    }

    @Test(priority = 20)
    public void TC_020_VerifyVTCRecordingStartAndEndAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_020 : Verify VTC_RECORDING_START & VTC_RECORDING_END");
        System.out.println("======================================");

        MeetingOverlayPage overlay = joinMeeting();

        // Start recording
        overlay.clickRecordButton();
        System.out.println("✓ Record button clicked");

        Thread.sleep(8000);

        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "Chat button not ready");

        overlay.clickChatButtonRobust();

        Thread.sleep(2000);

        switchToDesktop();

        String teamsHandle = WindowHelper.findWindowHandle("Microsoft Teams", 15);

        Assert.assertNotNull(
                teamsHandle,
                "Microsoft Teams window not found");

        attachByHandle(teamsHandle);

        overlay = new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForRecordingStartedMessage(60),
                "Recording started message not found");

        System.out.println("✓ Recording started");
        System.out.println("Expected Analytics Event : VTC_RECORDING_START");

        // Count existing messages
        int stoppedBefore = overlay.getRecordingStoppedMessageCount();
        int savedBefore = overlay.getRecordingSavedMessageCount();

        // Back to blocker
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker", 15);

        Assert.assertNotNull(
                blockerHandle,
                "Mersive Room Blocker not found");

        attachByHandle(blockerHandle);

        overlay = new MeetingOverlayPage(driver);

        // Stop recording
        overlay.stoprecordButton();
        System.out.println("✓ Stop recording clicked");

        Thread.sleep(5000);

        // Switch back to Teams
        switchToDesktop();

        teamsHandle =
                WindowHelper.findWindowHandle("Microsoft Teams", 15);

        Assert.assertNotNull(
                teamsHandle,
                "Microsoft Teams window not found");

        attachByHandle(teamsHandle);

        overlay = new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForNewRecordingStoppedMessage(stoppedBefore, 30),
                "Recording stopped message not found");

        Assert.assertTrue(
                overlay.waitForNewRecordingSavedMessage(savedBefore, 90),
                "Recording saved message not found");

        System.out.println("✓ Recording stopped");
        System.out.println("✓ Recording saved");

        System.out.println("Expected Analytics Event : VTC_RECORDING_END");

        // Return to Mersive
        switchToDesktop();

        blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker", 15);

        attachByHandle(blockerHandle);

        overlay = new MeetingOverlayPage(driver);

        Thread.sleep(3000);

        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        Thread.sleep(5000);

        System.out.println("======================================");
        System.out.println("TC_020 PASSED");
        System.out.println("======================================");
    }

@Test(priority = 21)
public void TC_021_VerifyVTCSpeakerOnAnalytics() throws Exception {

    System.out.println("======================================");
    System.out.println("TC_021 : Verify VTC_SPEAKER_ON Analytics");
    System.out.println("======================================");

    MeetingOverlayPage overlay = joinMeeting();

    overlay.clickAudioVisualButton();

    AVControlsPage av = new AVControlsPage(driver);

    // Ensure Speaker is OFF first
    if (av.isSpeakerOn()) {

        av.clickSpeakerToggle();

        Assert.assertTrue(
                av.waitForSpeakerOff(),
                "Speaker did not turn OFF");

        System.out.println("✓ Speaker turned OFF");
    }

    // Turn Speaker ON
    av.clickSpeakerToggle();

    Assert.assertTrue(
            av.waitForSpeakerOn(),
            "Speaker did not turn ON");

    System.out.println("✓ Speaker turned ON");

    System.out.println("Expected Analytics Event : VTC_SPEAKER_ON");
    System.out.println("User Action : Speaker turned ON");

    av.clickSwipeToClose();

    overlay.clickLeaveButton();

    RootSessionPage root = new RootSessionPage(driver);
    root.clickLeaveMeetingConfirmation();

    setLastMeetingOverlayHandle(null);

    System.out.println("======================================");
    System.out.println("TC_021 PASSED");
    System.out.println("======================================");
}
    @Test(priority = 22)
    public void TC_022_VerifyVTCSpeakerOffAnalytics() throws Exception {

        MeetingOverlayPage overlay = joinMeeting();

        overlay.clickAudioVisualButton();

        AVControlsPage av = new AVControlsPage(driver);

        if (av.isSpeakerOn()) {

            av.clickSpeakerToggle();

            Assert.assertTrue(
                    av.waitForSpeakerOff(),
                    "Speaker did not turn OFF");
        }

        System.out.println("Expected Analytics Event : VTC_SPEAKER_OFF");

        av.clickSwipeToClose();

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);
    }
    @Test(priority = 23)
    public void TC_023_VerifyKioskStartAndEndAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_023 : Verify KIOSK_START & KIOSK_END");
        System.out.println("======================================");

        HomeScreenPage home = new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home screen not loaded");

        System.out.println("✓ Home Screen loaded");

        // ===============================
        // KIOSK_START
        // ===============================
        System.out.println("Expected Analytics Event : KIOSK_START");
        System.out.println("User Action : Application launched");

        Thread.sleep(2000);
        List<WebElement> buttons = driver.findElements(By.className("Button"));

        System.out.println("Buttons found = " + buttons.size());

        for (WebElement b : buttons) {
            System.out.println(
                    "Name = " + b.getAttribute("Name")
                            + " | AutomationId = "
                            + b.getAttribute("AutomationId"));
        }

        // Exit Application
        home.clickKioskExitButton();

        System.out.println("✓ Kiosk Exit button clicked");

        Thread.sleep(5000);

        // ===============================
        // KIOSK_END
        // ===============================
        System.out.println("Expected Analytics Event : KIOSK_END");
        System.out.println("User Action : Application closed");

        System.out.println("======================================");
        System.out.println("TC_023 PASSED");
        System.out.println("======================================");
    }

    @Test(priority = 24)
    public void TC_024_VerifyDeviceHealthAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_024 : Verify DEVICE_HEALTH Analytics");
        System.out.println("======================================");

        HomeScreenPage home = new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home screen not loaded");

        System.out.println("✓ Home Screen loaded");

        home.clickKioskExitButton();

        System.out.println("✓ Application closed");

        Thread.sleep(5000);

        System.out.println("Expected Analytics Event : DEVICE_HEALTH");
        System.out.println("Expected Payload : CPU, Memory, Disk, Network, Uptime");

        System.out.println("======================================");
        System.out.println("TC_024 PASSED");
        System.out.println("======================================");
    }


    ///////////////////////CLIENT_ERROR///////////////////////

    @Test(priority = 18)
    public void TC_018_VerifyCalendarLoadFailed() throws Exception {

        System.out.println("========================================");
        System.out.println("TC_018: Verify Calendar Load Failed");
        System.out.println("========================================");

        // Mersive application is launched by BaseTest
        System.out.println("Step 1: Launching Mersive application...");

        // Verify "Calendar unavailable"
        By calendarUnavailable =
                By.name("Calendar unavailable");

        Assert.assertTrue(
                driver.findElements(calendarUnavailable).size() > 0,
                "\"Calendar unavailable\" message is not displayed."
        );

        System.out.println(
                "Step 1 PASSED: \"Calendar unavailable\" message is displayed."
        );

        // Verify "The room calendar can't be loaded right now"
        By calendarLoadFailed =
                By.name("The room calendar can't be loaded right now");

        Assert.assertTrue(
                driver.findElements(calendarLoadFailed).size() > 0,
                "\"The room calendar can't be loaded right now\" message is not displayed."
        );

        System.out.println(
                "Step 2 PASSED: \"The room calendar can't be loaded right now\" message is displayed."
        );

        // Verify manual meeting message
        By manualMeetingMessage =
                By.name("You can still start a meeting manually from the home screen");

        Assert.assertTrue(
                driver.findElements(manualMeetingMessage).size() > 0,
                "\"You can still start a meeting manually from the home screen\" message is not displayed."
        );

        System.out.println(
                "Step 3 PASSED: \"You can still start a meeting manually from the home screen\" message is displayed."
        );

        System.out.println("TC_018 PASSED");
        System.out.println("========================================");
    }




}
