package cz.upce.webapp.selenium;

import cz.upce.webapp.selenium.testframework.SeleniumTestCase;
import cz.upce.webapp.selenium.testframework.pages.DashboardPage;
import cz.upce.webapp.selenium.testframework.pages.LoginPage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Martin Volenec / st46661
 */

@SpringBootTest
public class LoginUITest extends Specification
{
    @Autowired private LoginPage loginPage;
    @Autowired private DashboardPage dashboardPage;

    def "loginPageCorrectTitle"()
    {
        when:
            loginPage.visit()
            def actualTitle = loginPage.driver.getTitle();
        then:
            loginPage.getExpectedTitle() == actualTitle;
    }
/*
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
    */
}
