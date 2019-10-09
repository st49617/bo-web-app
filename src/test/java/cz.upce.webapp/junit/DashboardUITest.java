package cz.upce.webapp.junit;

import cz.upce.webapp.selenium.DashboardPage;
import cz.upce.webapp.selenium.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Martin Volenec / st46661
 */

public class DashboardUITest
{
    private static DashboardPage dashboardPage;
    private static LoginPage loginPage;

    @BeforeClass
    public static void setUp()
    {
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
    }

    @AfterClass
    public static void tearDown()
    {
        dashboardPage.closeWindow();
    }

    @Test
    public void dashboardPageCorrectTitle()
    {
        loginPage.submitLoginForm("test@test.cz", "test");

        assertEquals(dashboardPage.getTitle(), loginPage.driver().getTitle());
    }
}
