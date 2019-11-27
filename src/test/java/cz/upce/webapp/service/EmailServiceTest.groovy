package cz.upce.webapp.service

import org.apache.commons.mail.util.MimeMessageParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.subethamail.wiser.Wiser
import org.subethamail.wiser.WiserMessage
import spock.lang.Specification

@SpringBootTest(
        properties = [
                "spring.mail.host=localhost",
                "spring.mail.port=8085" ])
class EmailServiceTest extends Specification {

    Wiser wiser = new Wiser(8085)

    @Autowired
    ApplicationContext applicationContext

    void setup() {
        wiser.start()
    }

    @Autowired EmailService emailService
    def "SendMailWithAttachment"() {

        when:
        byte[] content = ['a','b','c']
            def s = emailService.sendMailWithAttachment(
                    "info@pardubicebezobalu.cz",
                    "pavel.jetensky@seznam.cz",
                    null, "ahoj",
                    "Tady Pavel", "test.txt",
                    "plain/text", content)
        def messages = wiser.getMessages()
        def messageParser = new MimeMessageParser(
                messages[0].getMimeMessage()
        )
        messageParser.parse()
        then:
            messageParser.getFrom() == "info@pardubicebezobalu.cz"
            messageParser.getPlainContent() == "Tady Pavel"
            messageParser
                    .findAttachmentByName("test.txt")
                    .data == ['a','b','c']

    }

    void cleanup() {
        wiser.stop()
    }
}
