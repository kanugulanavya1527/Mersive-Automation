package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import pages.MeetingCardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * CalendarSyncTest - TC_1b: Calendar sync
 *
 * Precondition (manual, one-time setup before running):
 *   Schedule a Teams meeting inviting the room's resource mailbox.
 *   (Meeting title is dynamic/varies per run, so this test does NOT
 *   match on a specific title — it verifies that whatever meeting
 *   was scheduled has synced and appears as a valid card on the
 *   Mersive Room home screen.)
 */
public class CalendarSyncTest extends BaseTest {

    // How long to wait for the calendar sync to reflect the invite
    private static final int MAX_WAIT_SECONDS = 90;

    @Test(priority = 1)
    public void TC_1b_VerifyCalendarSyncShowsInvitedMeeting() throws InterruptedException {
        System.out.println("=== TC_1b: Verify Calendar Sync (invited meeting shows on tablet) ===");

        HomeScreenPage home = new HomeScreenPage(driver);

        boolean homeLoaded = false;
        for (int i = 0; i < 15; i++) {
            if (home.isHomeScreenLoaded()) {
                homeLoaded = true;
                break;
            }
            System.out.println("Waiting for Home Screen... " + (i + 1));
            Thread.sleep(1000);
        }
        Assert.assertTrue(homeLoaded, "TC_1b FAILED: Home screen not loaded");
        System.out.println("✓ Home screen loaded");

        MeetingCardPage cardPage = new MeetingCardPage(driver);

        boolean found = false;
        MeetingCardPage.MeetingCard matchedCard = null;

        for (int elapsed = 0; elapsed < MAX_WAIT_SECONDS; elapsed += 5) {

            List<MeetingCardPage.MeetingCard> meetings = cardPage.getAllMeetings();

            System.out.println("Poll at " + elapsed + "s — cards found: " + meetings.size());

            for (MeetingCardPage.MeetingCard card : meetings) {

                boolean hasTitle = card.title != null && !card.title.trim().isEmpty();
                boolean hasTime  = card.time != null && !card.time.trim().isEmpty();

                if (hasTitle && hasTime) {
                    found = true;
                    matchedCard = card;
                    break;
                }
            }

            if (found) {
                break;
            }

            Thread.sleep(5000);
        }

        Assert.assertTrue(found,
                "TC_1b FAILED: No valid meeting card (with title + time) appeared on tablet within "
                        + MAX_WAIT_SECONDS + "s. Verify a meeting invite was sent to the room's resource mailbox.");

        System.out.println("✓ Meeting card found:");
        System.out.println("  Title    : " + matchedCard.title);
        System.out.println("  Time     : " + matchedCard.time);
        System.out.println("  Status   : " + matchedCard.status);
        System.out.println("  Platform : " + matchedCard.platform);
        System.out.println("  Join Btn : " + matchedCard.joinButtonVisible);

        Assert.assertTrue(matchedCard.joinButtonVisible,
                "TC_1b FAILED: JOIN button not visible on synced meeting card");

        System.out.println("TC_1b PASSED — calendar sync confirmed (meeting card present with title, time, join button)");
    }
}