package cz.upce.webapp.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class EmailServiceTest extends Specification {
    @Autowired EmailService emailService
    def "SendMailWithAttachment"() {
        when:
        byte[] content = ['a','b','c']
            def s = emailService.sendMailWithAttachment(
                    "info@pardubicebezobalu.cz",
                    "pavel.jetensky@seznam.cz",
                    "pavel.jetensky@gmail.com", "ahoj",
                    "Tady pavel", "test.txt",
                    "plain/text", content)
            then:
            s=="sdf"
    }
}
