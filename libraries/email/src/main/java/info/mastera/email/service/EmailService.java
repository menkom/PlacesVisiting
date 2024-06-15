package info.mastera.email.service;

import info.mastera.email.dto.EmailMessage;
import info.mastera.email.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(@NonNull EmailMessage emailMessage) {
        Validate.notNull(emailMessage, "Email message cannot be null");
        Validate.notEmpty(emailMessage.title(), "Email title cannot be empty");
        Validate.notEmpty(emailMessage.recipient(), "Email recipient cannot be empty");
        Validate.notEmpty(emailMessage.content(), "Email content cannot be empty");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailMessage.recipient());
            message.setSubject(emailMessage.title());
            message.setText(emailMessage.content());

            javaMailSender.send(message);
        } catch (MailSendException ex) {
            logger.error("Error sending email.", ex);
        }
    }
}
