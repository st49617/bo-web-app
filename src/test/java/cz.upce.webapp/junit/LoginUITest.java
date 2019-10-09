package cz.upce.webapp.junit;

import cz.upce.webapp.selenium.DashboardPage;
import cz.upce.webapp.selenium.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * @author Martin Volenec / st46661
 */

public class LoginUITest
{
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;


    @BeforeClass
    public static void setUp()
    {
        loginPage = new LoginPage();
    }

    @AfterClass
    public static void tearDown()
    {
        loginPage.closeWindow();
    }

    @Test
    public void loginPageCorrectTitle()
    {
        loginPage.visit();
        String actualTitle = loginPage.getTitle();
        assertEquals(loginPage.getExpectedTitle(), actualTitle);
    }

    @Test
    public void loginPageCorrectFormName()
    {
        String actualFormName = loginPage.getLoginFormName();
        assertEquals(loginPage.getExpectedFormName(), actualFormName);
    }

    @Test
    public void loginPageCorrectSubmitName()
    {
        String actualSubmitName = loginPage.getSubmitName();

        assertNotNull(actualSubmitName);
        assertEquals(loginPage.getExpectedSubmitName(), actualSubmitName);
    }

    @Test
    public void loginPageCorrectFormAction()
    {
        String actualFormAction = loginPage.getFormSubmitLink();

        assertNotNull(actualFormAction);
        assertEquals(loginPage.getFormAction(), actualFormAction);
    }

    @Test
    public void loginPageInCorrectLogin()
    {
        loginPage.submitLoginForm("test@test.cz", "wrong");
        assertEquals(LoginPage.INVALID_LOGIN_ERROR_MESSAGE, loginPage.getErrorMessage());
    }
}
