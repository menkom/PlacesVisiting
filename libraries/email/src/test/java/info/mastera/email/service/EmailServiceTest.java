package info.mastera.email.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import info.mastera.email.dto.EmailMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class EmailServiceTest {

    JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    EmailService emailService = new EmailService(javaMailSender);
    EmailMessage emailMessage;

    @BeforeEach
    void init() {
        emailMessage = new EmailMessage("title", "to", "text message");
    }

    @Test
    void sendTest() {
        emailService.sendMessage(emailMessage);

        ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(javaMailSender).send(mailMessageCaptor.capture());
        SimpleMailMessage resultToTest = mailMessageCaptor.getValue();
        Assertions.assertNotNull(resultToTest);
        Assertions.assertNotNull(resultToTest.getTo());
        Assertions.assertEquals(1, resultToTest.getTo().length);
        Assertions.assertEquals("to", resultToTest.getTo()[0]);
        Assertions.assertEquals("title", resultToTest.getSubject());
        Assertions.assertEquals("text message", resultToTest.getText());
    }

    @ParameterizedTest
    @CsvSource({
            ",,",
            "title,recipient,",
            "title,,body",
            ",recipient,body",
    })
    void emptyMessageFieldExceptionTest(String title, String recipient, String content) {
        emailMessage = new EmailMessage(title, recipient, content);

        assertThrows(IllegalArgumentException.class,
                () -> emailService.sendMessage(emailMessage));
    }

    @Test
    void emailLogsExceptionTest() {
        Mockito.doThrow(MailSendException.class)
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        final Logger logger = (Logger) LoggerFactory.getLogger(EmailService.class);
        final ListAppender<ILoggingEvent> logsCollector = new ListAppender<>();
        logger.addAppender(logsCollector);
        logsCollector.start();

        emailService.sendMessage(emailMessage);

        assertThat(logsCollector.list)
                .isNotEmpty()
                .extracting(ILoggingEvent::getMessage)
                .contains("Error sending email.");
    }
}