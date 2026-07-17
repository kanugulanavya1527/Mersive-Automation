package pages;

import base.BasePage;
import io.appium.java_client.windows.WindowsElement;
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

    private final By joinButtons = By.name("JOIN");
    private final By meetingStatuses = By.xpath("//Text[@Name='In use' or @Name='Available']");
    //private final By meetingPlatforms = By.xpath("//Text[@Name='Teams' or @Name='Zoom']");
    //private final By zoomLabels      = By.xpath("//Text[@Name='Zoom']");
    private final By zoomLabels =
            By.xpath("//Text[translate(@Name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='zoom']");

    private final By teamIcons =
            By.className("Image");
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
        List<WebElement> joinBtns = driver.findElements(joinButtons);
        List<WebElement> allTexts = driver.findElements(By.className("TextBlock"));

        System.out.println("[MeetingCardPage] JOIN buttons found: " + joinBtns.size());

        List<int[]> yPositions = new ArrayList<>();
        List<String> textValues = new ArrayList<>();
        for (WebElement el : allTexts) {
            String t = el.getText().trim();
            if (t.isEmpty()) continue;
            textValues.add(t);
            yPositions.add(new int[]{el.getLocation().getY()});
        }

        for (int i = 0; i < joinBtns.size(); i++) {
            int joinY = joinBtns.get(i).getLocation().getY();

            MeetingCard card = new MeetingCard();
          //  card.title = pickByOffset(textValues, yPositions, joinY, 60, 200, true);
            card.title = getTitleByIndex(i);
           // card.time = pickByOffset(textValues, yPositions, joinY, 0, 260, false);
            card.time = getTimeByIndex(i);
            card.status = "";
            for (int j = 0; j < textValues.size(); j++) {
                String t = textValues.get(j);
                if ((t.equalsIgnoreCase("In use") || t.equalsIgnoreCase("Available"))
                        && Math.abs(joinY - yPositions.get(j)[0]) < 260) {
                    card.status = t;
                    break;
                }
            }
            card.platform = getPlatformByIndex(i);
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

    //    public void clickJoinForFirstTeamsMeeting() {
//        List<WebElement> joinBtns = waitForJoinButtons(20);
//        if (joinBtns.isEmpty())
//            throw new RuntimeException("No joinable meeting cards on the home screen.");
//
//        // Look for a "Teams" label element
//        By teamsLabels = By.className("Image");
//        List<WebElement> teams = driver.findElements(teamsLabels);
//        if (teams.isEmpty())
//            throw new RuntimeException(
//                    "No Teams meeting is scheduled on the room right now. "
//                            + "Schedule a Teams meeting covering the test window.");
//
//        // Match each Teams label to the JOIN button on the same card row (closest Y)
//        for (WebElement label : teams) {
//            int labelY = label.getLocation().getY();
//            WebElement best = null;
//            int bestDist = Integer.MAX_VALUE;
//            for (WebElement join : joinBtns) {
//                int d = Math.abs(join.getLocation().getY() - labelY);
//                if (d < bestDist) { bestDist = d; best = join; }
//            }
//            if (best != null && bestDist < 150) {   // same card row
//                best.click();
//                System.out.println("[MeetingCardPage] Joined first Teams meeting card");
//                return;
//            }
//        }
//        throw new RuntimeException("Could not match a JOIN button to a Teams card.");
//    }
//
    public void clickJoinForFirstTeamsMeeting() {

        List<WebElement> joinBtns = waitForJoinButtons(20);

        if (joinBtns.isEmpty()) {
            throw new RuntimeException("No joinable meeting cards on the home screen.");
        }

        // Teams icon is now an Image instead of Text
        List<WebElement> images = driver.findElements(By.className("Image"));

        if (images.isEmpty()) {
            throw new RuntimeException("No Images found on Home Screen.");
        }

        for (WebElement image : images) {

            int x = image.getLocation().getX();
            int y = image.getLocation().getY();
            int width = image.getSize().getWidth();
            int height = image.getSize().getHeight();

            System.out.println(
                    "Image -> X=" + x +
                            " Y=" + y +
                            " W=" + width +
                            " H=" + height);

            // Ignore background images and keep only the Teams icon
            if (width == 44 && height == 41 && x > 2500) {

                WebElement bestJoin = null;
                int bestDistance = Integer.MAX_VALUE;

                for (WebElement join : joinBtns) {

                    int distance =
                            Math.abs(join.getLocation().getY() - y);

                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestJoin = join;
                    }
                }

                if (bestJoin != null) {

                    System.out.println(
                            "[MeetingCardPage] Teams icon matched with JOIN button");

                    bestJoin.click();

                    System.out.println(
                            "[MeetingCardPage] Joined first Teams meeting");

                    return;
                }
            }
        }

        throw new RuntimeException("Could not locate Teams meeting card.");
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

        List<WebElement> joinBtns = driver.findElements(joinButtons);
        if (index >= joinBtns.size()) return "";

        int joinY = joinBtns.get(index).getLocation().getY();

        List<WebElement> times = driver.findElements(
                By.xpath("//Text[contains(@Name,'AM') or contains(@Name,'PM')]"));

        String bestTime = "";
        int bestGap = Integer.MAX_VALUE;

        for (WebElement el : times) {
            int y = el.getLocation().getY();

            // exclude the wall clock (fixed near top of screen)
            if (y < 200) continue;

            int gap = joinY - y;
            if (gap >= 0 && gap <= 250 && gap < bestGap) {
                bestGap = gap;
                bestTime = el.getText().trim();
            }
        }

        return bestTime;
    }

    private String getPlatformByIndex(int index) {

        List<WebElement> joinBtns = driver.findElements(joinButtons);

        int joinY = joinBtns.get(index).getLocation().getY();

        System.out.println("\n===========================");
        System.out.println("JOIN Y = " + joinY);
        System.out.println("===========================");

        List<WebElement> images = driver.findElements(By.className("Image"));

        for(WebElement img : images){

            System.out.println(
                    "IMAGE -> X=" + img.getLocation().getX()
                            + " Y=" + img.getLocation().getY()
                            + " W=" + img.getSize().getWidth()
                            + " H=" + img.getSize().getHeight());
        }

        List<WebElement> texts = driver.findElements(By.className("TextBlock"));

        for(WebElement t : texts){

            System.out.println(
                    "TEXT = " + t.getText()
                            + "  X=" + t.getLocation().getX()
                            + " Y=" + t.getLocation().getY());
        }

        return "";
    }


// ── getAllMeetings now waits per-card before reading ────

    public static void printAllTextBlocks() {
        List<WebElement> texts = driver.findElements(By.className("TextBlock"));
        System.out.println("Total TextBlocks = " + texts.size());
        for (WebElement el : texts) {
            System.out.println(
                    "Name=\"" + el.getText() + "\"" +
                            " X=" + el.getLocation().getX() +
                            " Y=" + el.getLocation().getY() +
                            " W=" + el.getSize().getWidth() +
                            " H=" + el.getSize().getHeight());
        }
    }

    public void printAllImages() {

        List<WebElement> images = driver.findElements(By.className("Image"));

        System.out.println("Total Images = " + images.size());

        int i = 1;

        for (WebElement image : images) {

            System.out.println("--------------------------");
            System.out.println("Image " + i++);
            System.out.println("Y = " + image.getLocation().getY());
            System.out.println("X = " + image.getLocation().getX());
            System.out.println("Width = " + image.getSize().getWidth());
            System.out.println("Height = " + image.getSize().getHeight());

            try {
                System.out.println("AutomationId = " + image.getAttribute("AutomationId"));
                System.out.println("ClassName = " + image.getAttribute("ClassName"));
                System.out.println("Name = " + image.getAttribute("Name"));
            } catch (Exception ignored) {
            }
        }

        List<WebElement> joins = driver.findElements(By.name("JOIN"));

        System.out.println("\n===== JOIN BUTTONS =====");

        for (WebElement join : joins) {

            System.out.println(
                    "JOIN -> X="
                            + join.getLocation().getX()
                            + " Y="
                            + join.getLocation().getY());
        }
    }

    public static void printAllJoinButtons() {
        List<WebElement> joins = driver.findElements(By.name("JOIN"));
        System.out.println("Total JOIN matches = " + joins.size());
        for (WebElement el : joins) {
            System.out.println(
                    "Name=\"" + el.getText() + "\"" +
                            " X=" + el.getLocation().getX() +
                            " Y=" + el.getLocation().getY());
        }
    }

    // ── Wait until the card row for this JOIN index has a REAL title ────
    private boolean cardHasRealTitle(int joinY) {
        List<WebElement> texts = driver.findElements(By.className("TextBlock"));
        for (WebElement el : texts) {
            String t = el.getText().trim();
            if (t.isEmpty()) continue;

            // skip on-screen keyboard row (fixed Y=452 in all captures) and single characters
            if (t.length() <= 2) continue;

            int y = el.getLocation().getY();
            int gap = joinY - y;

            if (gap >= 80 && gap <= 160) return true;
        }
        return false;
    }

    private void waitForCardToRender(int joinIndex, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            List<WebElement> joinBtns = driver.findElements(joinButtons);
            if (joinIndex >= joinBtns.size()) return;
            int joinY = joinBtns.get(joinIndex).getLocation().getY();
            if (cardHasRealTitle(joinY)) return;
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }
    }


    private String pickByOffset(List<String> texts, List<int[]> ys, int joinY,

                                int minGap, int maxGap, boolean isTitle) {

        String best = "";

        int bestGap = Integer.MAX_VALUE;

        for (int i = 0; i < texts.size(); i++) {

            String t = texts.get(i);

            if (t.length() <= 2) continue;

            if (t.equalsIgnoreCase("In use") || t.equalsIgnoreCase("Available")) continue;

            if (t.equalsIgnoreCase("Zoom") || t.equalsIgnoreCase("Teams")) continue;

            if (t.equals("|")) continue;

            // exclude home-screen UI chrome

            if (t.equalsIgnoreCase("Start a meeting") || t.equalsIgnoreCase("Join with ID")

                    || t.equalsIgnoreCase("Settings") || t.equalsIgnoreCase("Screen key")

                    || t.equalsIgnoreCase("AirPlay") || t.equalsIgnoreCase("Google Cast")

                    || t.equalsIgnoreCase("Miracast") || t.equalsIgnoreCase("HDMI In")

                    || t.contains("InfoComm") || t.contains("Visit ")

                    || t.equalsIgnoreCase("Conference Room3") || t.contains("Wednesday")

                    || t.contains("Kumar Medikonda") || t.contains("onwards"))

                continue;

            boolean looksLikeTime = t.matches(".*\\d.*(AM|PM).*");

            if (isTitle && looksLikeTime) continue;

            if (!isTitle && !looksLikeTime) continue;

            int gap = joinY - ys.get(i)[0];

            if (gap >= minGap && gap <= maxGap && gap < bestGap) {

                bestGap = gap;

                best = t;

            }

        }

        return best;

    }
    private String getTitleByIndex(int index) {

        List<WebElement> joinBtns = driver.findElements(joinButtons);

        if (index >= joinBtns.size())
            return "";

        int joinY = joinBtns.get(index).getLocation().getY();

        List<WebElement> texts = driver.findElements(By.className("TextBlock"));

        String bestTitle = "";
        int bestGap = Integer.MAX_VALUE;

        for (WebElement el : texts) {

            String text = el.getText().trim();

            if (text.isEmpty())
                continue;

            if (text.length() <= 2)
                continue;

            // Ignore status
            if (text.equalsIgnoreCase("In use")
                    || text.equalsIgnoreCase("Available"))
                continue;

            // Ignore platform
            if (text.equalsIgnoreCase("Zoom")
                    || text.equalsIgnoreCase("Teams"))
                continue;

            // Ignore separator
            if (text.equals("|"))
                continue;

            // Ignore day/date
            if (text.contains("Monday")
                    || text.contains("Tuesday")
                    || text.contains("Wednesday")
                    || text.contains("Thursday")
                    || text.contains("Friday")
                    || text.contains("Saturday")
                    || text.contains("Sunday"))
                continue;

            // Ignore times
            if (text.matches(".*\\d.*AM.*")
                    || text.matches(".*\\d.*PM.*"))
                continue;

            // Ignore home page labels
            if (text.equalsIgnoreCase("AirPlay")
                    || text.equalsIgnoreCase("Google Cast")
                    || text.equalsIgnoreCase("Miracast")
                    || text.equalsIgnoreCase("HDMI In")
                    || text.equalsIgnoreCase("Start a meeting")
                    || text.equalsIgnoreCase("Join with ID")
                    || text.equalsIgnoreCase("Settings")
                    || text.equalsIgnoreCase("Screen key")
                    || text.contains("Visit")
                    || text.contains("InfoComm"))
                continue;

            int y = el.getLocation().getY();

            int gap = joinY - y;

            // Title should be above JOIN
            if (gap <= 0 || gap > 200)
                continue;

            // Skip room/organizer line
            if (gap < 80)
                continue;

            if (gap < bestGap) {
                bestGap = gap;
                bestTitle = text;
            }
        }

        return bestTitle;
    }

    public static void printGroupStructure() {

        List<WebElement> groups =
                driver.findElements(By.className("Group"));

        System.out.println("Groups = " + groups.size());

        for (int i = 0; i < groups.size(); i++) {

            WebElement g = groups.get(i);

            System.out.println("----------------------------");
            System.out.println("GROUP " + i);

            List<WebElement> children =
                    g.findElements(By.xpath("./*"));

            System.out.println("Children = " + children.size());

            for (WebElement c : children) {

                System.out.println(
                        c.getTagName()
                                + " | "
                                + c.getAttribute("LocalizedControlType")
                                + " | "
                                + c.getText());
            }
        }
    }

}