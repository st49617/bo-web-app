package cz.upce.webapp.selenium.testframework;

import org.openqa.selenium.WebDriver;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {
 
 @Override
 public void afterTestMethod(TestContext testContext) throws Exception {
  // testContext.getApplicationContext().getBean(WebDriver.class).quit();
 }
 
}