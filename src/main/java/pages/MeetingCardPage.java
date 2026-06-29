package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class MeetingCardPage extends BasePage {

    public MeetingCardPage(RemoteWebDriver driver) {
        super(driver);
    }

    // ── Locators ───────────────────────────────────────────

    private final By joinButtons     = By.name("JOIN");
    private final By meetingStatuses = By.xpath("//Text[@Name='In use' or @Name='Available']");
    private final By meetingPlatforms = By.xpath("//Text[@Name='Teams' or @Name='Zoom']");
    private final By zoomLabels      = By.xpath("//Text[@Name='Zoom']");

    private final By rangeMeetingTimes =
            By.xpath("//Text[@ClassName='TextBlock' and contains(@Name,' - ')]");
    private final By singleMeetingTimes =
            By.xpath("//Text[@ClassName='TextBlock' and " +
                    "(contains(@Name,'AM') or contains(@Name,'PM')) and " +
                    "not(contains(@Name,' - '))]");

    // ── Data Model ─────────────────────────────────────────

    public static class MeetingCard {
        public String title;
        public String time;
        public String status;
        public String platform;
        public boolean joinButtonVisible;
    }

    // ── Public Methods ─────────────────────────────────────
    private List<WebElement> waitForJoinButtons(int timeoutSeconds) {
        try {
            return new WebDriverWait(driver, timeoutSeconds).until(d -> {
                List<WebElement> found = d.findElements(joinButtons);
                return found.isEmpty() ? null : found;
            });
        } catch (TimeoutException e) {
            return new ArrayList<>();
        }
    }

    public List<MeetingCard> getAllMeetings() {
        List<MeetingCard> meetings = new ArrayList<>();
        List<WebElement> joinBtns  = driver.findElements(joinButtons);

        System.out.println("[MeetingCardPage] JOIN buttons found: " + joinBtns.size());

        for (int i = 0; i < joinBtns.size(); i++) {
            MeetingCard card    = new MeetingCard();
            card.title          = getTitleByIndex(i);
            card.time           = getTimeByIndex(i);
            card.status         = getTextByIndex(meetingStatuses, i);
            card.platform       = getTextByIndex(meetingPlatforms, i);
            card.joinButtonVisible = true;
            meetings.add(card);
        }

        return meetings;
    }

    public void clickJoinButtonByIndex(int index) {
        List<WebElement> buttons = driver.findElements(joinButtons);
        if (index >= buttons.size()) {
            throw new RuntimeException(
                    "JOIN button not found at index: " + index);
        }
        buttons.get(index).click();
    }
    public void clickJoinForFirstZoomMeeting()
            throws InterruptedException {

        List<WebElement> zoomLabels = new ArrayList<>();
        List<WebElement> joinBtns = new ArrayList<>();

        for (int i = 0; i < 30; i++) {

            zoomLabels =
                    driver.findElements(
                            By.xpath("//Text[@Name='Zoom']"));

            joinBtns =
                    driver.findElements(By.name("JOIN"));

            System.out.println(
                    "Attempt "
                            + (i + 1)
                            + " -> Zoom="
                            + zoomLabels.size()
                            + " JOIN="
                            + joinBtns.size());

            if (!zoomLabels.isEmpty()
                    && !joinBtns.isEmpty()) {
                break;
            }

            Thread.sleep(1000);
        }

        if (zoomLabels.isEmpty()
                || joinBtns.isEmpty()) {

            throw new RuntimeException(
                    "No Zoom meeting found after waiting 30 seconds."
            );
        }

        for (WebElement zoom : zoomLabels) {

            int zoomY =
                    zoom.getLocation().getY();

            WebElement closestJoin = null;
            int closestDistance =
                    Integer.MAX_VALUE;

            for (WebElement join : joinBtns) {

                int joinY =
                        join.getLocation().getY();

                int distance =
                        Math.abs(joinY - zoomY);

                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestJoin = join;
                }
            }

            if (closestJoin != null
                    && closestDistance < 150) {

                System.out.println(
                        "Clicking JOIN at Y="
                                + closestJoin
                                .getLocation()
                                .getY());

                new Actions(driver)
                        .moveToElement(closestJoin)
                        .click()
                        .perform();

                Thread.sleep(2000);
                return;
            }
        }

        throw new RuntimeException(
                "Could not match JOIN button to Zoom meeting."
        );
    }
    public void clickJoinForFirstTeamsMeeting() {
        List<WebElement> joinBtns = waitForJoinButtons(20);
        if (joinBtns.isEmpty())
            throw new RuntimeException("No joinable meeting cards on the home screen.");

        // Look for a "Teams" label element
        By teamsLabels = By.xpath("//Text[@Name='Teams']");
        List<WebElement> teams = driver.findElements(teamsLabels);
        if (teams.isEmpty())
            throw new RuntimeException(
                    "No Teams meeting is scheduled on the room right now. "
                            + "Schedule a Teams meeting covering the test window.");

        // Match each Teams label to the JOIN button on the same card row (closest Y)
        for (WebElement label : teams) {
            int labelY = label.getLocation().getY();
            WebElement best = null;
            int bestDist = Integer.MAX_VALUE;
            for (WebElement join : joinBtns) {
                int d = Math.abs(join.getLocation().getY() - labelY);
                if (d < bestDist) { bestDist = d; best = join; }
            }
            if (best != null && bestDist < 150) {   // same card row
                best.click();
                System.out.println("[MeetingCardPage] Joined first Teams meeting card");
                return;
            }
        }
        throw new RuntimeException("Could not match a JOIN button to a Teams card.");
    }



    public String getMeetingCardTitle() {

        List<WebElement> texts = driver.findElements(By.className("TextBlock"));

        for (int i = 0; i < texts.size(); i++) {

            String text = texts.get(i).getText().trim();

            if (text.equalsIgnoreCase("Zoom") || text.equalsIgnoreCase("Teams")) {

                if (i + 1 < texts.size()) {

                    String title = texts.get(i + 1).getText().trim();

                    System.out.println("[MeetingCardPage] Card title: " + title);

                    return title;
                }
            }
        }

        return null;
    }

    // ── Private Helpers ────────────────────────────────────

    private String getTextByIndex(By locator, int index) {
        List<WebElement> elements = driver.findElements(locator);
        return index < elements.size()
                ? elements.get(index).getText().trim() : "";
    }

    private String getTimeByIndex(int index) {
        List<WebElement> rangeTimes = driver.findElements(rangeMeetingTimes);
        if (index < rangeTimes.size())
            return rangeTimes.get(index).getText().trim();

        List<WebElement> singleTimes = driver.findElements(singleMeetingTimes);
        return index < singleTimes.size()
                ? singleTimes.get(index).getText().trim() : "";
    }

    private String getTitleByIndex(int index) {

        List<WebElement> texts =
                driver.findElements(By.className("TextBlock"));

        for (int i = 0; i < texts.size(); i++) {

            String text =
                    texts.get(i).getText().trim();

            if (text.equalsIgnoreCase("Zoom")
                    || text.equalsIgnoreCase("Teams")) {

                for (int j = i + 1; j < texts.size(); j++) {

                    String title =
                            texts.get(j).getText().trim();

                    if (!title.isEmpty()
                            && !title.equalsIgnoreCase("Zoom")
                            && !title.equalsIgnoreCase("Teams")
                            && !title.equalsIgnoreCase("In use")
                            && !title.equalsIgnoreCase("Available")
                            && !title.contains("AM")
                            && !title.contains("PM")
                            && !title.equals("|")) {

                        return title;
                    }
                }
            }
        }

        return "";
    }
}