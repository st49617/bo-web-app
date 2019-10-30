package cz.upce.webapp.selenium.old;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginIntegrationTest
{

    @Test
    public void loginPageCorrectTitle()
    {
        int i=0;
        while (true) {
            i++;
            if (i==100000) {
                System.out.println(0);
                i=0;
            }
        }
    }


}
