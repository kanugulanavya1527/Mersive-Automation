package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import pages.PlatformSelectPage;
import pages.PreJoinPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

    @Test(priority = 5)
    public void TC_005_ClickStartMeetingAndVerifyNavigation() {
        System.out.println("=== TC_005: Start Meeting Navigation ===");

        HomeScreenPage home = new HomeScreenPage(driver);
        PlatformSelectPage platform = new PlatformSelectPage(driver);

        home.clickStartMeeting();
        System.out.println("Clicked Start a meeting");

        Assert.assertTrue(
                platform.isPlatformScreenLoaded(),
                "TC_005 FAILED: Platform selection screen not loaded"
        );

        System.out.println("TC_005 PASSED: Microsoft Teams and Zoom options visible");
    }

    @Test(priority = 6)
    public void TC_006_ClickTeamsAndVerifyPreJoinScreen() throws InterruptedException {
        System.out.println("=== TC_006: Click Teams and Verify Pre-Join Screen ===");

        HomeScreenPage home         = new HomeScreenPage(driver);
        PlatformSelectPage platform = new PlatformSelectPage(driver);
        PreJoinPage preJoin         = new PreJoinPage(driver);

        home.clickStartMeeting();
        System.out.println("Clicked Start a meeting");

        Assert.assertTrue(
                platform.isPlatformScreenLoaded(),
                "TC_006 FAILED: Platform screen not loaded"
        );

        platform.clickMicrosoftTeams();
        System.out.println("Clicked Microsoft Teams");

        Assert.assertTrue(
                preJoin.isPreJoinScreenLoaded(),
                "TC_006 FAILED: Pre-join screen not loaded"
        );

        System.out.println("TC_006 PASSED: Pre-join screen loaded with camera and mic visible");
    }
}