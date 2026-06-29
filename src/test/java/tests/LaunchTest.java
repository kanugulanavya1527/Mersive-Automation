package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.HomeScreenPage;
import pages.PlatformSelectPage;

public class LaunchTest extends BaseTest {

    @Test
    public void testOnlyLaunch() throws Exception {

        HomeScreenPage home = new HomeScreenPage(driver);
        PlatformSelectPage platform = new PlatformSelectPage(driver);

        home.clickStartMeeting();

        Thread.sleep(3000);

        platform.clickMicrosoftTeams();

        Thread.sleep(30000);
    }
}