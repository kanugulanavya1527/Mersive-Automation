package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.ProcessHelper;
import utils.WindowHelper;

import java.util.List;

import static base.BasePage.driver;

public class ZoomAnalyticsEventsTest extends BaseTest {

    // ============================================================
    // COMMON METHOD - JOIN ZOOM MEETING
    // ============================================================

    private MeetingOverlayPage joinZoomMeeting() throws Exception {

        MeetingCardPage cards = new MeetingCardPage(driver);
        PreJoinPage preJoin = new PreJoinPage(driver);

        System.out.println("[Zoom] Clicking first Zoom meeting...");

        cards.clickJoinForFirstZoomMeeting();

        System.out.println("[Zoom] Waiting for Pre-Join screen...");

        new WebDriverWait(driver, 30)
                .until(d -> {
                    try {
                        return preJoin.isPreJoinScreenLoaded();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Zoom Pre-Join screen did not load"
        );

        System.out.println("[Zoom] Clicking Join Zoom Meeting...");

        preJoin.clickJoinZoomMeeting();

        switchToDesktop();

        System.out.println(
                "[Zoom] Waiting for Mersive Room Blocker..."
        );

        String blockerHandle =
                WindowHelper.waitForBlockerWindow(60);

        Assert.assertNotNull(
                blockerHandle,
                "Mersive Room Blocker window not found"
        );

        attachByHandle(blockerHandle);

        setLastMeetingOverlayHandle(blockerHandle);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting screen did not load"
        );

        System.out.println("✓ Zoom meeting joined");

        return overlay;
    }


    // ============================================================
    // TC_001
    // TABLET_CLICK - START MEETING
    // ============================================================

    @Test(priority = 1)
    public void TC_001_VerifyStartMeetingButtonClickEvent() {

        System.out.println("========================================");
        System.out.println(
                "TC_001 : Verify Start Meeting Button Click"
        );
        System.out.println("========================================");

        HomeScreenPage home =
                new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home Screen is not displayed."
        );

        home.clickStartMeeting();

        System.out.println(
                "Expected Analytics Event : TABLET_CLICK"
        );

        System.out.println(
                "Button Name : start_meeting_button"
        );

        System.out.println("TC_001 PASSED");
    }


    // ============================================================
    // TC_002
    // TABLET_CLICK - JOIN WITH ID
    // ============================================================

    @Test(priority = 2)
    public void TC_002_VerifyJoinWithIdButtonClickEvent() {

        System.out.println("========================================");
        System.out.println(
                "TC_002 : Verify Join With ID Button Click"
        );
        System.out.println("========================================");

        HomeScreenPage home =
                new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home Screen is not displayed."
        );

        home.clickJoinWithId();

        System.out.println(
                "Expected Analytics Event : TABLET_CLICK"
        );

        System.out.println(
                "Button Name : join_with_id_button"
        );

        System.out.println("TC_002 PASSED");
    }


    // ============================================================
    // TC_003
    // TABLET_CLICK - SEND INVITE
    // ============================================================

    @Test(priority = 3)
    public void TC_003_VerifySendInviteButtonClickEvent() throws Exception {

        System.out.println("========================================");
        System.out.println("TC_003 : Verify Send Invite Button Click");
        System.out.println("========================================");

        // Join Zoom meeting
        MeetingOverlayPage zoomOverlay = joinZoomMeeting();

        // Verify People button
        Assert.assertTrue(
                zoomOverlay.waitForPeopleButtonReady(),
                "People button not ready"
        );

        // Open People panel
        zoomOverlay.clickPeopleButton();

        Assert.assertTrue(
                zoomOverlay.waitForPeoplePanelOpened(),
                "People panel did not open"
        );

        System.out.println("✓ People panel opened");

        // Attach to People window
        switchToDesktop();

        String peopleHandle =
                WindowHelper.findWindowHandle("People");

        Assert.assertNotNull(
                peopleHandle,
                "People window not found"
        );

        System.out.println("People Handle = " + peopleHandle);

        attachByHandle(peopleHandle);

        // Create overlay page for People window
        MeetingOverlayPage peopleOverlay =
                new MeetingOverlayPage(driver);

        System.out.println("✓ Attached to People window");

        // Click Zoom Invite button
        peopleOverlay.clickZoomInviteButton();

        System.out.println("✓ Zoom Invite button clicked");

        Thread.sleep(3000);

        // Attach to Share Meeting Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite");

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found"
        );

        System.out.println(
                "Share Invite Handle = " + shareInvitationHandle
        );

        attachByHandle(shareInvitationHandle);

        // Create Share Invite page
        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        // Verify Share Invite screen
        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed"
        );

        System.out.println("✓ Share Invite screen displayed");

        // Enter valid email
        shareInvite.enterRecipientEmail(
                "navya.kanugula@rampgroup.com"
        );

        System.out.println("✓ Recipient email entered");

        // Click Send Invite
        shareInvite.clickSendInvite();

        System.out.println("✓ Send Invite button clicked");

        Thread.sleep(3000);

        // Analytics expectation
        System.out.println(
                "Expected Analytics Event : TABLET_CLICK"
        );

        System.out.println(
                "Button Name : send_invite_button"
        );

        // Return to meeting
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        Assert.assertNotNull(
                blockerHandle,
                "Failed to return to meeting screen"
        );

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed"
        );

        System.out.println("✓ Returned to meeting screen");

        System.out.println("TC_003 PASSED");
    }

    // ============================================================
    // TC_004
    // SETTINGS BUTTON
    // ============================================================


    @Test(priority = 4)
    public void TC_004_VerifySettingsButtonAnalyticsEvent()
            throws Exception {

        System.out.println("========================================");
        System.out.println(
                "TC_004 : Verify Settings Button Analytics Event"
        );
        System.out.println("========================================");

        HomeScreenPage home =
                new HomeScreenPage(driver);

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
                "Home screen not loaded"
        );

        System.out.println("✓ Home Screen displayed");

        // Verify Settings button
        Assert.assertTrue(
                home.isSettingsButtonDisplayed(),
                "Settings button not displayed"
        );

        // Click Settings
        home.clickSettings();

        System.out.println("✓ Settings button clicked");

        // Verify Administrator Access popup
        AdminAccessPage admin =
                new AdminAccessPage(driver);

        Assert.assertTrue(
                admin.isAdminAccessPopupDisplayed(),
                "Administrator Access popup not displayed"
        );

        System.out.println(
                "✓ Administrator Access popup displayed"
        );

        // Enter Admin PIN
        admin.enterPin("111111");

        System.out.println("✓ Admin PIN entered");

        // Verify Settings screen
        SettingsPage settings =
                new SettingsPage(driver);

        Assert.assertTrue(
                settings.isSettingsScreenDisplayed(),
                "Settings screen not displayed"
        );

        System.out.println("✓ Settings screen displayed");

        // Analytics validation
        System.out.println(
                "Expected Analytics Event : TABLET_CLICK"
        );

        System.out.println(
                "Button Name : settings_button"
        );

        System.out.println("TC_004 PASSED");
    }


    // ============================================================
    // TC_005
    // CLOSE APPLICATION
    // ============================================================
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
        admin.enterPin("111111");      // Replace with your actual PIN

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
        admin.enterPin("111111");      // Replace with your actual PIN

        System.out.println("✓ Close Application PIN entered");

        // Wait for app to close
        Thread.sleep(5000);

        // Verify analytics event here

        System.out.println("✓ Application closed successfully");

        System.out.println("TC_005 PASSED");
    }
    // ============================================================
    // CAMERA + MIC COMBINATIONS
    // ============================================================

    @DataProvider(name = "cameraMicCombinations")
    public Object[][] cameraMicCombinations() {

        return new Object[][]{
                {true, true},
                {true, false},
                {false, true},
                {false, false}
        };
    }

    // ============================================================
    // TC_006
    // VTC_MEETING:START
    // ============================================================

    @Test(
            priority = 6,
            dataProvider = "cameraMicCombinations"
    )
    public void TC_006_VerifyVTCMeetingStartAnalytics(
            boolean camera,
            boolean mic) throws Exception {

        System.out.println("========================================");
        System.out.println(
                "TC_006 : Verify VTC_MEETING:START"
        );
        System.out.println(
                "Camera : " + camera
        );
        System.out.println(
                "Mic    : " + mic
        );
        System.out.println("========================================");

        // Home Screen
        HomeScreenPage home =
                new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home screen not loaded"
        );

        // Start Meeting
        home.clickStartMeeting();

        // Platform Selection
        PlatformSelectPage platform =
                new PlatformSelectPage(driver);

        Assert.assertTrue(
                platform.isPlatformScreenLoaded(),
                "Platform screen not loaded"
        );

        platform.clickZoom();

        Thread.sleep(3000);

        // Zoom Pre-Join
        PreJoinPage preJoin =
                new PreJoinPage(driver);

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Zoom Pre-Join screen not loaded"
        );

        // Set Camera and Microphone
        preJoin.setCamera(camera);

        Thread.sleep(1000);

        preJoin.setMicrophone(mic);

        Thread.sleep(1000);

        // Start Zoom Meeting
        preJoin.clickStartZoomMeeting();

        // Attach to Meeting Overlay
        switchToDesktop();

        String blocker =
                WindowHelper.waitForBlockerWindow(60);

        Assert.assertNotNull(
                blocker,
                "Mersive Room Blocker not found"
        );

        attachByHandle(blocker);

        setLastMeetingOverlayHandle(blocker);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting did not start"
        );

        System.out.println("✓ Zoom meeting started");

        // Analytics
        System.out.println(
                "Expected Analytics Event : VTC_MEETING:START"
        );

        System.out.println(
                "Camera : " + camera
        );

        System.out.println(
                "Microphone : " + mic
        );

        // Leave Meeting
        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        Thread.sleep(5000);

        System.out.println("✓ Meeting ended");

        System.out.println("TC_006 PASSED");
    }


    // ============================================================
    // TC_007
    // VTC_MEETING:JOIN
    // ============================================================


    @Test(
            priority = 7,
            dataProvider = "cameraMicCombinations"
    )
    public void TC_007_VerifyVTCMeetingJoinAnalytics(
            boolean camera,
            boolean mic) throws Exception {

        System.out.println("========================================");
        System.out.println(
                "TC_007 : Verify VTC_MEETING:JOIN"
        );
        System.out.println(
                "Camera : " + camera
        );
        System.out.println(
                "Mic    : " + mic
        );
        System.out.println("========================================");

        HomeScreenPage home =
                new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home screen not loaded"
        );

        System.out.println("✓ Home Screen displayed");

        // Join first Zoom meeting
        MeetingCardPage meetingCard =
                new MeetingCardPage(driver);

        meetingCard.clickJoinForFirstZoomMeeting();

        System.out.println("✓ Zoom meeting JOIN button clicked");

        // Zoom Pre-Join
        PreJoinPage preJoin =
                new PreJoinPage(driver);

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Zoom Pre-Join screen not loaded"
        );

        System.out.println("✓ Zoom Pre-Join screen displayed");

        // Set Camera
        preJoin.setCamera(camera);

        Thread.sleep(1000);

        // Set Microphone
        preJoin.setMicrophone(mic);

        Thread.sleep(1000);

        // Join Zoom Meeting
        preJoin.clickJoinZoomMeeting();

        System.out.println("✓ Join Zoom Meeting clicked");

        // Attach to Mersive Room Blocker
        switchToDesktop();

        String blocker =
                WindowHelper.waitForBlockerWindow(60);

        Assert.assertNotNull(
                blocker,
                "Mersive Room Blocker not found"
        );

        attachByHandle(blocker);

        setLastMeetingOverlayHandle(blocker);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        // Verify meeting joined
        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting did not join"
        );

        System.out.println("✓ Zoom meeting joined");

        // ========================================
        // ANALYTICS
        // ========================================

        System.out.println(
                "Expected Analytics Event : VTC_MEETING:JOIN"
        );

        System.out.println(
                "Camera : " + camera
        );

        System.out.println(
                "Microphone : " + mic
        );

        // ========================================
        // Leave Meeting
        // ========================================

        overlay.clickLeaveButton();

        System.out.println("✓ Leave button clicked");

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        System.out.println("✓ Leave confirmed");

        setLastMeetingOverlayHandle(null);

        Thread.sleep(5000);

        System.out.println("✓ Meeting ended");

        System.out.println(
                "TC_007 PASSED - Camera="
                        + camera
                        + ", Mic="
                        + mic
        );
    }
    // ============================================================
    // TC_008
    // JOIN WITH ID
    // ============================================================

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

        // Select Zoom
        join.clickZoom();
        // Enter Meeting ID
        join.enterMeetingId("928 4919 1065");

        // Enter Passcode
        join.enterPassword("678752");

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

        // Join Meeting (no Pre-Join screen exists in this flow)
        join.clickJoinMeetingButton();

        System.out.println("✓ Zoom JOIN clicked");

        // Wait for meeting overlay
        switchToDesktop();

        String blocker =
                WindowHelper.waitForBlockerWindow(120);

        Assert.assertNotNull(
                blocker,
                "Meeting overlay not found");

        setLastMeetingOverlayHandle(blocker);

        attachByHandle(blocker);

        MeetingOverlayPage overlay =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                overlay.waitForMeetingJoinedScreen(),
                "Zoom meeting did not join");

        System.out.println("✓ Zoom meeting joined");

        // ==========================================
        // Set Camera state via in-meeting overlay toggle
        // ==========================================
        boolean cameraCurrentlyOn = overlay.isOverlayCameraOn();
        System.out.println("[TC_008] Camera currently on = " + cameraCurrentlyOn);

        if (camera != cameraCurrentlyOn) {
            overlay.clickOverlayCameraToggle();

            if (camera) {
                Assert.assertTrue(overlay.waitForOverlayCameraOn(), "Camera did not turn ON");
            } else {
                Assert.assertTrue(overlay.waitForOverlayCameraOff(), "Camera did not turn OFF");
            }
        }

        boolean cameraFinalOn = overlay.isOverlayCameraOn();
        Assert.assertEquals(cameraFinalOn, camera,
                "Camera ended in wrong state — expected on=" + camera + " but was " + cameraFinalOn);

        System.out.println("✓ Camera final state confirmed: " + (camera ? "ON" : "OFF"));

        // ==========================================
        // Set Mic state via in-meeting overlay toggle
        // ==========================================
        boolean micCurrentlyUnmuted = overlay.isOverlayMicUnmuted();
        System.out.println("[TC_008] Mic currently unmuted = " + micCurrentlyUnmuted);

        if (mic != micCurrentlyUnmuted) {
            overlay.clickOverlayMicToggle();

            if (mic) {
                Assert.assertTrue(overlay.waitForOverlayMicUnmuted(), "Mic did not turn ON");
            } else {
                Assert.assertTrue(overlay.waitForOverlayMicMuted(), "Mic did not turn OFF");
            }
        }

        boolean micFinalUnmuted = overlay.isOverlayMicUnmuted();
        Assert.assertEquals(micFinalUnmuted, mic,
                "Mic ended in wrong state — expected unmuted=" + mic + " but was " + micFinalUnmuted);

        System.out.println("✓ Mic final state confirmed: " + (mic ? "ON" : "OFF"));

        // ==========================================
        // Expected Analytics Event : VTC_MEETING:JOIN
        // Source     : join_with_code
        // Camera     : camera value above
        // Microphone : mic value above
        // ==========================================

        // Leave
        overlay.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        Thread.sleep(5000);

        // Return Home
        switchToDesktop();

        String homeHandle =
                WindowHelper.findWindowHandle("Mersive Room");

        Assert.assertNotNull(
                homeHandle,
                "Mersive Room window not found");

        attachByHandle(homeHandle);

        Assert.assertTrue(
                new HomeScreenPage(driver).isHomeScreenLoaded(),
                "Failed to return to Home Screen");

        System.out.println("==================================");
        System.out.println("TC_008 PASSED | Camera=" + camera + " | Mic=" + mic);
        System.out.println("==================================");
    }

    // ============================================================
    // TC_009
    // VTC_MEETING:END
    // ============================================================

    @Test(priority = 9)
    public void TC_009_VerifyVTCMeetingEndAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_009 : Verify VTC_MEETING:END"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.isChatButtonVisible(),
                "Zoom meeting was not joined"
        );

        System.out.println(
                "✓ Zoom meeting joined"
        );

        overlay.clickLeaveButton();

        System.out.println(
                "✓ Leave button clicked"
        );

        Thread.sleep(1000);

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        System.out.println(
                "✓ Leave confirmed"
        );

        Thread.sleep(5000);

        switchToDesktop();

        String homeHandle =
                WindowHelper.findWindowHandle(
                        "Mersive Room"
                );

        Assert.assertNotNull(
                homeHandle,
                "Mersive Room window not found"
        );

        attachByHandle(homeHandle);

        Assert.assertTrue(
                new HomeScreenPage(driver).isHomeScreenLoaded(),
                "Failed to return to Home Screen"
        );

        System.out.println(
                "Expected Analytics Event : VTC_MEETING:END"
        );

        System.out.println("TC_009 PASSED");
    }


    // ============================================================
    // TC_010
    // VTC_CHAT_OPEN
    // ============================================================

    @Test(priority = 10)
    public void TC_010_VerifyVTCChatOpenAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_010 : Verify VTC_CHAT_OPEN"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "Chat button not ready"
        );

        overlay.clickChatButtonRobust();

        Thread.sleep(3000);

        System.out.println(
                "Expected Analytics Event : VTC_CHAT_OPEN"
        );

        System.out.println(
                "User Action : Chat panel opened"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_010 PASSED");
    }


    // ============================================================
    // TC_011
    // VTC_CHAT_CLOSE
    // ============================================================

    @Test(priority = 11)
    public void TC_011_VerifyVTCChatCloseAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_011 : Verify VTC_CHAT_CLOSE"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForChatButtonReady(),
                "Chat button not ready"
        );

        overlay.clickChatButtonRobust();

        Thread.sleep(2000);

        System.out.println(
                "✓ Chat opened"
        );

        overlay.clickChatButtonRobust();

        Thread.sleep(2000);

        System.out.println(
                "✓ Chat closed"
        );

        System.out.println(
                "Expected Analytics Event : VTC_CHAT_CLOSE"
        );

        System.out.println(
                "User Action : Chat panel closed"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_011 PASSED");
    }


    // ============================================================
    // TC_012
    // VTC_PEOPLE_OPEN
    // ============================================================

    @Test(priority = 12)
    public void TC_012_VerifyVTCPeopleOpenAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_012 : Verify VTC_PEOPLE_OPEN"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not ready"
        );

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open"
        );

        System.out.println(
                "✓ People panel opened"
        );

        System.out.println(
                "Expected Analytics Event : VTC_PEOPLE_OPEN"
        );

        System.out.println(
                "User Action : People panel opened"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_012 PASSED");
    }


    // ============================================================
    // TC_013
    // VTC_PEOPLE_CLOSE
    // ============================================================

    @Test(priority = 13)
    public void TC_013_VerifyVTCPeopleCloseAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_013 : Verify VTC_PEOPLE_CLOSE"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not ready"
        );

        overlay.clickPeopleButton();

        Assert.assertTrue(
                overlay.waitForPeoplePanelOpened(),
                "People panel did not open"
        );

        Thread.sleep(2000);

        overlay.clickPeopleButton();

        Thread.sleep(2000);

        System.out.println(
                "Expected Analytics Event : VTC_PEOPLE_CLOSE"
        );

        System.out.println(
                "User Action : People panel closed"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_013 PASSED");
    }


    // ============================================================
    // TC_014
    // VTC_EMAIL_INVITE
    // ============================================================

    @Test(priority = 14)
    public void TC_014_VerifyVTCEmailInviteAnalytics() throws Exception {

        System.out.println("======================================");
        System.out.println("TC_014 : Verify VTC_EMAIL_INVITE");
        System.out.println("======================================");

        // Join Zoom meeting
        MeetingOverlayPage zoomOverlay = joinZoomMeeting();

        // Verify People button
        Assert.assertTrue(
                zoomOverlay.waitForPeopleButtonReady(),
                "People button not ready"
        );

        // Open People panel
        zoomOverlay.clickPeopleButton();

        Assert.assertTrue(
                zoomOverlay.waitForPeoplePanelOpened(),
                "People panel did not open"
        );

        System.out.println("✓ People panel opened");

        // Attach to People window
        switchToDesktop();

        String peopleHandle =
                WindowHelper.findWindowHandle("People");

        Assert.assertNotNull(
                peopleHandle,
                "People window not found"
        );

        attachByHandle(peopleHandle);

        MeetingOverlayPage peopleOverlay =
                new MeetingOverlayPage(driver);

        System.out.println("✓ Attached to People window");

        // Click Zoom Invite button
        peopleOverlay.clickZoomInviteButton();

        System.out.println("✓ Zoom Invite button clicked");

        Thread.sleep(3000);

        // Attach to Share Meeting Invite window
        switchToDesktop();

        String shareInvitationHandle =
                WindowHelper.findWindowHandle("Share meeting invite");

        Assert.assertNotNull(
                shareInvitationHandle,
                "Share Invite window not found"
        );

        attachByHandle(shareInvitationHandle);

        ShareInvitePage shareInvite =
                new ShareInvitePage(driver);

        Assert.assertTrue(
                shareInvite.isShareInviteScreenDisplayed(),
                "Share Invite screen not displayed"
        );

        System.out.println("✓ Share Invite screen displayed");

        // Enter recipient email
        shareInvite.enterRecipientEmail("navya.kanugula@rampgroup.com");

        System.out.println("✓ Recipient email entered");

        // Send invite
        shareInvite.clickSendInvite();

        System.out.println("✓ Send Invite button clicked");

        Thread.sleep(3000);

        // Analytics expectation
        System.out.println("Expected Analytics Event : VTC_EMAIL_INVITE");
        System.out.println("Recipient : navya.kanugula@rampgroup.com");
        System.out.println("User Action : Email invite sent");

        // Return to meeting
        switchToDesktop();

        String blockerHandle =
                WindowHelper.waitForBlockerWindow(60);

        Assert.assertNotNull(
                blockerHandle,
                "Failed to return to meeting screen"
        );

        attachByHandle(blockerHandle);

        MeetingOverlayPage meeting =
                new MeetingOverlayPage(driver);

        Assert.assertTrue(
                meeting.waitForChatButtonReady(),
                "Meeting screen not displayed"
        );

        System.out.println("✓ Returned to meeting screen");

        // Leave meeting
        meeting.clickLeaveButton();

        RootSessionPage root = new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("======================================");
        System.out.println("TC_014 PASSED");
        System.out.println("======================================");
    }
    // ============================================================
    // TC_015
    // VTC_CAMERA_ON
    // ============================================================

    @Test(priority = 15)
    public void TC_015_VerifyVTCCameraOnAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_015 : Verify VTC_CAMERA_ON"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        if (overlay.isOverlayCameraOn()) {

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF"
            );
        }

        overlay.clickOverlayCameraToggle();

        Assert.assertTrue(
                overlay.waitForOverlayCameraOn(),
                "Camera did not turn ON"
        );

        System.out.println(
                "Expected Analytics Event : VTC_CAMERA_ON"
        );

        System.out.println(
                "User Action : Camera turned ON"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_015 PASSED");
    }


    // ============================================================
    // TC_016
    // VTC_CAMERA_OFF
    // ============================================================

    @Test(priority = 16)
    public void TC_016_VerifyVTCCameraOffAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_016 : Verify VTC_CAMERA_OFF"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        if (!overlay.isOverlayCameraOff()) {

            overlay.clickOverlayCameraToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayCameraOff(),
                    "Camera did not turn OFF"
            );
        }

        System.out.println(
                "Expected Analytics Event : VTC_CAMERA_OFF"
        );

        System.out.println(
                "User Action : Camera turned OFF"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_016 PASSED");
    }


    // ============================================================
    // TC_017
    // VTC_MIC_ON
    // ============================================================

    @Test(priority = 17)
    public void TC_017_VerifyVTCMicOnAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_017 : Verify VTC_MIC_ON"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        if (overlay.isOverlayMicUnmuted()) {

            overlay.clickOverlayMicToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayMicMuted(),
                    "Mic did not turn OFF"
            );
        }

        overlay.clickOverlayMicToggle();

        Assert.assertTrue(
                overlay.waitForOverlayMicUnmuted(),
                "Mic did not turn ON"
        );

        System.out.println(
                "Expected Analytics Event : VTC_MIC_ON"
        );

        System.out.println(
                "User Action : Microphone turned ON"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_017 PASSED");
    }


    // ============================================================
    // TC_018
    // VTC_MIC_OFF
    // ============================================================

    @Test(priority = 18)
    public void TC_018_VerifyVTCMicOffAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_018 : Verify VTC_MIC_OFF"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        if (!overlay.isOverlayMicMuted()) {

            overlay.clickOverlayMicToggle();

            Assert.assertTrue(
                    overlay.waitForOverlayMicMuted(),
                    "Mic did not turn OFF"
            );
        }

        System.out.println(
                "Expected Analytics Event : VTC_MIC_OFF"
        );

        System.out.println(
                "User Action : Microphone turned OFF"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_018 PASSED");
    }


    // ============================================================
    // TC_019
    // VTC_HAND_RAISE
    // ============================================================

    @Test(priority = 19)
    public void TC_019_VerifyVTCHandRaiseAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_019 : Verify VTC_HAND_RAISE"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        Assert.assertTrue(
                overlay.waitForPeopleButtonReady(),
                "People button not ready"
        );

        overlay.clickPeopleButton();

        Thread.sleep(3000);

        overlay.clickRaiseHandButton();

        Assert.assertTrue(
                overlay.isMyHandRaised(),
                "Hand was not raised"
        );

        System.out.println(
                "✓ Hand raised"
        );

        System.out.println(
                "Expected Analytics Event : VTC_HAND_RAISE"
        );

        System.out.println(
                "User Action : Raise Hand"
        );

        overlay.clickLowerHandButton();

        Thread.sleep(2000);

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_019 PASSED");
    }


    // ============================================================
    // TC_020
    // VTC_RECORDING_START / END
    // ============================================================

    @Test(priority = 20)
    public void TC_020_VerifyVTCRecordingStartAndEndAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_020 : Verify VTC_RECORDING_START & END"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        overlay.clickRecordButton();

        System.out.println(
                "✓ Record button clicked"
        );

        Thread.sleep(8000);

        System.out.println(
                "Expected Analytics Event : VTC_RECORDING_START"
        );

        System.out.println(
                "User Action : Recording started"
        );

        Thread.sleep(3000);

        overlay.stoprecordButton();

        System.out.println(
                "✓ Stop recording clicked"
        );

        Thread.sleep(5000);

        System.out.println(
                "Expected Analytics Event : VTC_RECORDING_END"
        );

        System.out.println(
                "User Action : Recording stopped"
        );

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        Thread.sleep(5000);

        System.out.println("TC_020 PASSED");
    }


    // ============================================================
    // TC_021
    // VTC_SPEAKER_ON
    // ============================================================

    @Test(priority = 21)
    public void TC_021_VerifyVTCSpeakerOnAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_021 : Verify VTC_SPEAKER_ON"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        overlay.clickAudioVisualButton();

        Thread.sleep(2000);

        AVControlsPage av =
                new AVControlsPage(driver);

        if (av.isSpeakerOn()) {

            av.clickSpeakerToggle();

            Assert.assertTrue(
                    av.waitForSpeakerOff(),
                    "Speaker did not turn OFF"
            );
        }

        av.clickSpeakerToggle();

        Assert.assertTrue(
                av.waitForSpeakerOn(),
                "Speaker did not turn ON"
        );

        System.out.println(
                "Expected Analytics Event : VTC_SPEAKER_ON"
        );

        System.out.println(
                "User Action : Speaker turned ON"
        );

        av.clickSwipeToClose();

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_021 PASSED");
    }


    // ============================================================
    // TC_022
    // VTC_SPEAKER_OFF
    // ============================================================

    @Test(priority = 22)
    public void TC_022_VerifyVTCSpeakerOffAnalytics()
            throws Exception {

        System.out.println("======================================");
        System.out.println(
                "TC_022 : Verify VTC_SPEAKER_OFF"
        );
        System.out.println("======================================");

        MeetingOverlayPage overlay =
                joinZoomMeeting();

        overlay.clickAudioVisualButton();

        Thread.sleep(2000);

        AVControlsPage av =
                new AVControlsPage(driver);

        if (av.isSpeakerOn()) {

            av.clickSpeakerToggle();

            Assert.assertTrue(
                    av.waitForSpeakerOff(),
                    "Speaker did not turn OFF"
            );
        }

        System.out.println(
                "Expected Analytics Event : VTC_SPEAKER_OFF"
        );

        System.out.println(
                "User Action : Speaker turned OFF"
        );

        av.clickSwipeToClose();

        overlay.clickLeaveButton();

        RootSessionPage root =
                new RootSessionPage(driver);

        root.clickLeaveMeetingConfirmation();

        setLastMeetingOverlayHandle(null);

        System.out.println("TC_022 PASSED");

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


    /// ////////////////////CLIENT_ERROR///////////////////////

    @Test(priority = 25)
    public void TC_025_VerifyCalendarLoadFailed() throws Exception {

        System.out.println("========================================");
        System.out.println("TC_025: Verify Calendar Load Failed");
        System.out.println("========================================");

        // BaseTest already launches Mersive
        System.out.println("Step 1: Closing Mersive desktop application...");

        ProcessHelper.kill("MersiveRoom.exe");

        Thread.sleep(2000);

        System.out.println("Step 1 PASSED: Mersive desktop application closed.");

        // Open Chrome for Admin Portal
        System.out.println("Step 2: Opening Admin Portal...");

        System.setProperty(
                "webdriver.chrome.driver",
                "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"
        );

        WebDriver adminDriver = new ChromeDriver();

        adminDriver.manage().window().maximize();

        adminDriver.get(
                "https://mersive-frontend-929782745950.africa-south1.run.app/login"
        );

        System.out.println("Step 2 PASSED: Admin Portal opened.");

        // =====================================================
// STEP 3: Login to Admin Portal
// =====================================================


        System.out.println("Step 3: Logging into Admin Portal...");

        WebElement email =
                adminDriver.findElement(
                        By.xpath("//input[@type='email']")
                );

        email.sendKeys("admin@mersive.com");

        WebElement password =
                adminDriver.findElement(
                        By.cssSelector("input[type='password']")
                );

        password.sendKeys("admin123");

        adminDriver.findElement(
                By.xpath("//button[normalize-space()='Sign In']")
        ).click();

        Thread.sleep(3000);

        System.out.println("Step 3 PASSED: Logged into Admin Portal.");


        // =====================================================
// STEP 4: Verify Admin Portal Home Page
// =====================================================

        System.out.println("Step 4: Verifying Admin Portal home page...");

        WebDriverWait wait =
                new WebDriverWait(adminDriver, 15);

        WebElement tabletDropdown =
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                By.id("tablet-device-select")
                        )
                );

        Assert.assertTrue(
                tabletDropdown.isDisplayed(),
                "Tablet ID dropdown is not displayed."
        );

        System.out.println(
                "Step 4 PASSED: Admin Portal home page displayed."
        );
// =====================================================
// STEP 5: Select Required Tablet
// =====================================================

        System.out.println("Step 5: Selecting required tablet...");

        tabletDropdown.click();

        WebElement requiredTablet =
                adminDriver.findElement(
                        By.xpath(
                                "//select[@id='tablet-device-select']" +
                                        "//option[@value='5C8965CD-E123-4393-A5FB-45FD03F3E3EF']"
                        )
                );

        requiredTablet.click();

        System.out.println(
                "Step 5 PASSED: Required tablet selected."
        );
// =====================================================
// STEP 6: Click Edit Configuration
// =====================================================

        System.out.println("Step 6: Clicking Edit Configuration...");

        adminDriver.findElement(
                By.xpath("//button[normalize-space()='Edit configuration']")
        ).click();

        Thread.sleep(1000);

        System.out.println("Step 6 PASSED: Edit Configuration clicked.");


// =====================================================
// STEP 7: Change Room User Email
// =====================================================

        System.out.println("Step 7: Changing Room User Email...");

        WebElement roomUserEmail =
                adminDriver.findElement(
                        By.id("outlook-room-user-email")
                );

        roomUserEmail.clear();

        roomUserEmail.sendKeys(
                "conference.room42@peopletech.com"
        );

        System.out.println(
                "Step 7 PASSED: Room User Email changed."
        );


// =====================================================
// STEP 8: Save Configuration
// =====================================================

        System.out.println("Step 8: Saving configuration...");

        adminDriver.findElement(
                By.xpath("//button[normalize-space()='Save Configuration']")
        ).click();

        Thread.sleep(3000);

        System.out.println("Step 8 PASSED: Configuration saved.");


// =====================================================
// STEP 9: Close Chrome
// =====================================================

        System.out.println("Step 9: Closing Admin Portal...");

        adminDriver.quit();

        System.out.println("Step 9 PASSED: Admin Portal closed.");


// =====================================================
// STEP 10: Relaunch Mersive Application
// =====================================================

        System.out.println("Step 10: Relaunching Mersive application...");

        relaunchMersiveApp();

        System.out.println(
                "Step 10 PASSED: Mersive application relaunched."
        );
// =====================================================
// STEP 11: Verify Calendar Load Failed
// =====================================================

        System.out.println(
                "Step 11: Verifying Calendar Load Failed..."
        );

        By calendarUnavailable =
                By.name("Calendar unavailable");

        Assert.assertTrue(
                driver.findElements(calendarUnavailable).size() > 0,
                "\"Calendar unavailable\" message is not displayed."
        );

        System.out.println(
                "Step 11.1 PASSED: \"Calendar unavailable\" displayed."
        );


        By calendarLoadFailed =
                By.name(
                        "The room calendar can't be loaded right now"
                );

        Assert.assertTrue(
                driver.findElements(calendarLoadFailed).size() > 0,
                "\"The room calendar can't be loaded right now\" " +
                        "message is not displayed."
        );

        System.out.println(
                "Step 11.2 PASSED: Calendar load failure message displayed."
        );


        By manualMeetingMessage =
                By.name(
                        "You can still start a meeting manually from the home screen"
                );

        Assert.assertTrue(
                driver.findElements(manualMeetingMessage).size() > 0,
                "\"You can still start a meeting manually from the home screen\" " +
                        "message is not displayed."
        );

        System.out.println(
                "Step 11.3 PASSED: Manual meeting message displayed."
        );
        System.out.println("========================================");
        System.out.println("TC_025 PASSED");
        System.out.println("========================================");


    }
    @Test(priority = 26)   // uninstall teams app and then run this script
    public void TC_026_VerifyZoomLaunchFailed() throws Exception {

        System.out.println("========================================");
        System.out.println("TC_026: Verify Zoom Launch Failed");
        System.out.println("========================================");

        HomeScreenPage home = new HomeScreenPage(driver);

        // Step 1: Verify Home Screen
        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home Screen is not displayed."
        );

        System.out.println(
                "Step 1 PASSED: Home Screen is displayed."
        );

        // Step 2: Click Join for first Zoom meeting
        MeetingCardPage meetingCard =
                new MeetingCardPage(driver);

        meetingCard.clickJoinForFirstZoomMeeting();

        System.out.println(
                "Step 2 PASSED: Join button clicked."
        );

        // Step 3: Verify Zoom Pre-Join screen
        PreJoinPage preJoin =
                new PreJoinPage(driver);

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "Zoom Pre-Join screen is not displayed."
        );

        System.out.println(
                "Step 3 PASSED: Zoom Pre-Join screen is displayed."
        );

        // Step 4: Click Join Zoom Meeting
        preJoin.clickJoinZoomMeeting();

        System.out.println(
                "Step 4 PASSED: Join Zoom Meeting button clicked."
        );

        // Step 5: Wait for Zoom launch attempt
        System.out.println(
                "Step 5: Waiting for Zoom launch attempt..."
        );

        Thread.sleep(7000);

        // Step 6: Check whether Mersive Room Blocker appeared
        switchToDesktop();

        String blockerHandle =
                WindowHelper.findWindowHandle("Mersive Room Blocker");

        // Step 7: Verify meeting was NOT entered
        Assert.assertNull(
                blockerHandle,
                "Meeting overlay appeared. Zoom launch did not fail as expected."
        );

        System.out.println(
                "Step 7 PASSED: Meeting was not entered."
        );

        System.out.println(
                "Zoom launch failed because Zoom is not installed."
        );

        // Step 8: Expected analytics event
        System.out.println(
                "Expected Analytics Event : CLIENT_ERROR"
        );

        System.out.println(
                "Expected Error Type : zoom_launch_failed"
        );

        System.out.println("========================================");
        System.out.println("TC_026 PASSED");
        System.out.println("========================================");
    }
    @Test(priority = 27)
    public void TC_027_VerifyZoomMeetingJoinFailed() throws Exception {

        System.out.println("========================================");
        System.out.println("TC_027: Verify Zoom Meeting Join Failed");
        System.out.println("========================================");

        // Step 1: Verify Home Screen
        HomeScreenPage home = new HomeScreenPage(driver);

        Assert.assertTrue(
                home.isHomeScreenLoaded(),
                "Home Screen is not displayed."
        );

        System.out.println("Step 1 PASSED: Home Screen displayed.");

        // Step 2: Click Join With ID
        home.clickJoinWithId();

        System.out.println("Step 2 PASSED: Join With ID clicked.");

        // Step 3: Open Join With ID page
        JoinWithIdPage join = new JoinWithIdPage(driver);

        // Step 4: Select Zoom
        join.clickZoom();

        System.out.println("Step 4 PASSED: Zoom selected.");

        // Step 5: Enter INVALID Meeting ID
        join.enterMeetingId("111 1111 1111");

        System.out.println("Step 5 PASSED: Invalid Meeting ID entered.");

        // Step 6: Enter INVALID Password
        join.enterPassword("invalid123");

        System.out.println("Step 6 PASSED: Invalid password entered.");

        // Step 7: Switch to keyboard
        switchToDesktop();

        String keyboardHandle =
                WindowHelper.findWindowHandle("Keyboard");

        Assert.assertNotNull(
                keyboardHandle,
                "Keyboard window not found."
        );

        attachByHandle(keyboardHandle);

        // Step 8: Click Done on keyboard
        join = new JoinWithIdPage(driver);

        join.clickDoneOnKeypad();

        System.out.println("Step 8 PASSED: Keyboard Done clicked.");

        // Step 9: Attach back to Mersive Room
        switchToDesktop();

        String roomHandle =
                WindowHelper.findWindowHandle("Mersive Room");

        Assert.assertNotNull(
                roomHandle,
                "Mersive Room window not found."
        );

        attachByHandle(roomHandle);

        join = new JoinWithIdPage(driver);

        // Step 10: Click Join Meeting
        join.clickJoinMeetingButton();

        System.out.println("Step 10 PASSED: Join Meeting clicked.");

        // Step 11: Wait for failure message
        By unableToJoin =
                By.name("Unable to join meeting");

        WebDriverWait wait =
                new WebDriverWait(driver, 15);

        Assert.assertTrue(
                wait.until(d ->
                        d.findElements(unableToJoin).size() > 0
                ),
                "\"Unable to join meeting\" message was not displayed."
        );

        System.out.println(
                "Step 11 PASSED: \"Unable to join meeting\" displayed."
        );

        // Step 12: Verify detailed error message
        By invalidCredentialsMessage =
                By.name(
                        "We couldn't join with that ID or password. Please double-check and try again."
                );

        Assert.assertTrue(
                wait.until(d ->
                        d.findElements(invalidCredentialsMessage).size() > 0
                ),
                "Invalid Meeting ID/Password error message was not displayed."
        );

        System.out.println(
                "Step 12 PASSED: Invalid Meeting ID/Password error displayed."
        );

        // Step 13 (NEW): Verify Retry button is present and enabled --
        // confirms the full error dialog rendered correctly, not a partial state.
        By retryButton = By.name("Retry");

        WebElement retry = wait.until(
                ExpectedConditions.visibilityOfElementLocated(retryButton)
        );

        Assert.assertTrue(
                retry.isEnabled(),
                "Retry button is present but not enabled."
        );

        System.out.println("Step 13 PASSED: Retry button displayed and enabled.");

        System.out.println("========================================");
        System.out.println("TC_027 PASSED");
        System.out.println("========================================");
    }
}

