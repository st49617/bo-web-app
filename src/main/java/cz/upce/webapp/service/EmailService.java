package cz.upce.webapp.service;

import cz.upce.webapp.controller.dto.PriceListDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;


    public String sendMailWithAttachment(final String from, String to, final String cc, String subject, String body, final String attachmentFilename, String contentType, byte[] attachment)
    {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText(body);
            helper.setFrom(from);
            helper.setTo(to);

            if (!StringUtils.isEmpty(cc)) {
                helper.setCc(cc);
            }
            if (!StringUtils.isEmpty(subject)) {
                helper.setSubject(subject);
            }
            helper.addAttachment(
                    attachmentFilename,
                    new ByteArrayResource(attachment),
                    contentType);
        };

        try {
            mailSender.send(preparator);
            return "E-mail s objednávkou byl v pořádku odeslán.";
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
            return "E-mail s objednávkou se nepodařilo odeslat.";
        }
    }
}
