package cz.upce.webapp.selenium.old;

import cz.upce.webapp.selenium.testframework.pages.DashboardPage;
import cz.upce.webapp.selenium.testframework.pages.LoginPage;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Martin Volenec / st46661
 */

@Ignore
public class DashboardUITest
{
    @Autowired
    private DashboardPage dashboardPage;

    @Autowired
    private LoginPage loginPage;

    @Test
    public void dashboardPageCorrectTitle()
    {
        loginPage.visit();
        loginPage.submitLoginForm("test@test.cz", "test");

        assertEquals(dashboardPage.getTitle(), loginPage.driver.getTitle());
    }
}
