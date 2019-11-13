package cz.upce.webapp.selenium.old;

import cz.upce.webapp.dao.testutil.Creator;
import cz.upce.webapp.selenium.testframework.SeleniumTestCase;
import cz.upce.webapp.selenium.testframework.pages.LoginPage;
import cz.upce.webapp.selenium.testframework.pages.DashboardPage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.Assert.*;

/**
 * @author Martin Volenec / st46661
 */

public class LoginUITest extends SeleniumTestCase
{
//    @Autowired private LoginPage loginPage;
//    @Autowired private DashboardPage dashboardPage;
//
//    @Before
//    public void visit() {
//        loginPage.visit();
//    }
//
//    @Test
//    public void loginPageCorrectTitle()
//    {
//        String actualTitle = loginPage.driver.getTitle();
//        assertEquals(loginPage.getExpectedTitle(), actualTitle);
//    }
//
//    @Test
//    public void loginPageCorrectFormName()
//    {
//        String actualFormName = loginPage.getLoginFormName();
//        assertEquals(loginPage.getExpectedFormName(), actualFormName);
//    }
//
//    @Test
//    public void loginPageCorrectSubmitName()
//    {
//        String actualSubmitName = loginPage.getSubmitName();
//
//        assertNotNull(actualSubmitName);
//        assertEquals(loginPage.getExpectedSubmitName(), actualSubmitName);
//    }
//
//    @Test
//    public void loginPageCorrectFormAction()
//    {
//        String actualFormAction = loginPage.getFormSubmitLink();
//
//        assertNotNull(actualFormAction);
//        assertEquals(loginPage.getFormAction(), actualFormAction);
//    }
//
//    @Test
//    public void loginPageInCorrectLogin()
//    {
//        loginPage.submitLoginForm("test@test.cz", "wrong");
//        assertEquals(LoginPage.INVALID_LOGIN_ERROR_MESSAGE, loginPage.getErrorMessage());
//    }
}
