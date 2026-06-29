package tests;

import base.BaseTest;
import pages.HomeScreenPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomeScreenTest extends BaseTest {

    @Test(priority = 1)
    public void TC_001_VerifyHomeScreenLoaded() {
        System.out.println("=== TC_001: Verify Home Screen Loaded ===");

        HomeScreenPage page = new HomeScreenPage(driver);

        Assert.assertTrue(
                page.isHomeScreenLoaded(),
                "TC_001 FAILED: Home screen not visible"
        );

        System.out.println("TC_001 PASSED: Home screen visible");
    }

    @Test(priority = 2)
    public void TC_002_VerifyBottomActionButtonsVisible() {
        System.out.println("=== TC_002: Verify Bottom Action Buttons ===");

        HomeScreenPage page = new HomeScreenPage(driver);

        Assert.assertTrue(
                page.areBottomActionButtonsVisible(),
                "TC_002 FAILED: One or more bottom buttons not visible"
        );

        System.out.println("TC_002 PASSED: Start a meeting, Join with ID, Settings visible");
    }

    @Test(priority = 3)
    public void TC_003_VerifyWirelessCastingButtonsVisible() {
        System.out.println("=== TC_003: Verify Wireless Casting Buttons ===");

        HomeScreenPage page = new HomeScreenPage(driver);

        Assert.assertTrue(
                page.areCastingButtonsVisible(),
                "TC_003 FAILED: One or more casting buttons not visible"
        );

        System.out.println("TC_003 PASSED: AirPlay, Google Cast, Miracast, HDMI In visible");
    }

    @Test(priority = 4)
    public void TC_004_VerifyDateAndTimeDisplayed() {
        System.out.println("=== TC_004: Verify Date and Time ===");

        HomeScreenPage page = new HomeScreenPage(driver);

        String time = page.getDisplayedTime();
        String date = page.getDisplayedDate();

        Assert.assertNotNull(time, "TC_004 FAILED: Time not visible");
        Assert.assertNotNull(date, "TC_004 FAILED: Date not visible");

        System.out.println("TC_004 PASSED: Time = " + time + " | Date = " + date);
    }
}