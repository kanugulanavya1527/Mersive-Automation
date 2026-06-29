package tests;

import base.BaseTest;
import pages.MeetingCardPage;
import pages.PreJoinPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class MeetingCardTest extends BaseTest {

    @Test(priority = 9)
    public void TC_009_VerifyMeetingCardDetailsForAllStates() {
        System.out.println("=== TC_009: Verify Meeting Card Details ===");

        MeetingCardPage cardPage = new MeetingCardPage(driver);

        List<MeetingCardPage.MeetingCard> meetings = cardPage.getAllMeetings();

        System.out.println("Total meeting cards found: " + meetings.size());

        Assert.assertFalse(
                meetings.isEmpty(),
                "TC_009 FAILED: No meeting cards found on screen"
        );

        for (int i = 0; i < meetings.size(); i++) {
            MeetingCardPage.MeetingCard card = meetings.get(i);

            System.out.println("--- Card " + (i + 1) + " ---");
            System.out.println("Title    : " + card.title);
            System.out.println("Time     : " + card.time);
            System.out.println("Status   : " + card.status);
            System.out.println("Platform : " + card.platform);
            System.out.println("Join Btn : " + card.joinButtonVisible);

            Assert.assertTrue(
                    card.joinButtonVisible,
                    "TC_009 FAILED: JOIN button missing for card " + (i + 1)
            );

            Assert.assertNotNull(
                    card.title,
                    "TC_009 FAILED: Title null for card " + (i + 1)
            );

            Assert.assertFalse(
                    card.title.trim().isEmpty(),
                    "TC_009 FAILED: Title empty for card " + (i + 1)
            );
        }

        System.out.println("TC_009 PASSED");
    }

    @Test(priority = 10)
    public void TC_010_VerifyJoinButtonNavigatesToPreJoinScreen() throws InterruptedException {
        System.out.println("=== TC_010: JOIN Button Navigates to Pre-Join ===");

        MeetingCardPage cardPage = new MeetingCardPage(driver);
        PreJoinPage preJoin      = new PreJoinPage(driver);

        List<MeetingCardPage.MeetingCard> meetings = cardPage.getAllMeetings();

        Assert.assertFalse(
                meetings.isEmpty(),
                "TC_010 FAILED: No meeting cards available"
        );

        cardPage.clickJoinButtonByIndex(0);
        System.out.println("✓ Clicked JOIN button");

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "TC_010 FAILED: Pre-join screen not loaded"
        );

        Assert.assertTrue(
                preJoin.isCameraOn(),
                "TC_010 FAILED: Camera not visible on pre-join"
        );

        Assert.assertTrue(
                preJoin.isMicrophoneOn(),
                "TC_010 FAILED: Microphone not visible on pre-join"
        );

        System.out.println("TC_010 PASSED: Pre-join screen loaded, camera ON, mic ON");
    }

    @Test(priority = 11)
    public void TC_011_VerifyCameraAndMicToggleViaJoinButton() throws InterruptedException {
        System.out.println("=== TC_011: Camera and Mic Toggle via JOIN button ===");

        MeetingCardPage cardPage = new MeetingCardPage(driver);
        PreJoinPage preJoin      = new PreJoinPage(driver);

        Assert.assertFalse(
                cardPage.getAllMeetings().isEmpty(),
                "TC_011 FAILED: No meeting cards available"
        );

        cardPage.clickJoinButtonByIndex(0);
        System.out.println("✓ Clicked JOIN button");

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "TC_011 FAILED: Pre-join screen not loaded"
        );

        // Camera OFF
        preJoin.clickCameraToggle();
        Assert.assertTrue(preJoin.waitForCameraOff(),
                "TC_011 FAILED: Camera did not turn OFF");
        System.out.println("✓ Camera OFF");

        // Camera ON
        preJoin.clickCameraToggle();
        Assert.assertTrue(preJoin.waitForCameraOn(),
                "TC_011 FAILED: Camera did not turn ON");
        System.out.println("✓ Camera ON");

        // Mic OFF
        preJoin.clickMicrophoneToggle();
        Assert.assertTrue(preJoin.waitForMicrophoneOff(),
                "TC_011 FAILED: Mic did not turn OFF");
        System.out.println("✓ Mic OFF");

        // Mic ON
        preJoin.clickMicrophoneToggle();
        Assert.assertTrue(preJoin.waitForMicrophoneOn(),
                "TC_011 FAILED: Mic did not turn ON");
        System.out.println("✓ Mic ON");

        System.out.println("TC_011 PASSED");
    }
}