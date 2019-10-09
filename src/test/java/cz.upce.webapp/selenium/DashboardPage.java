package cz.upce.webapp.selenium;

import cz.upce.webapp.configuration.SeleniumConfig;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

/**
 * @author Martin Volenec / st46661
 */

public class DashboardPage
{
    private String loginURL = "http://localhost:9000";
    LoginPage loginPage;

    public DashboardPage()
    {
        LoginPage.driver.get(loginURL);
        loginPage = new LoginPage();
    }

    public void closeWindow()
    {
        LoginPage.driver.close();
    }

    public String getTitle()
    {
        return "Dashboard";
    }
}