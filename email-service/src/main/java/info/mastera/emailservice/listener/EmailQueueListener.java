package info.mastera.emailservice.listener;

import info.mastera.email.dto.EmailMessage;
import info.mastera.email.service.EmailService;
import info.mastera.email.util.Constants;
import info.mastera.emailservice.mapper.EmailCalendarMessageMapper;
import info.mastera.rabbitmq.dto.CalendarEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailQueueListener.class);

    private final EmailService emailService;
    private final EmailCalendarMessageMapper emailCalendarMessageMapper;

    public EmailQueueListener(EmailService emailService,
                              EmailCalendarMessageMapper emailCalendarMessageMapper) {
        this.emailService = emailService;
        this.emailCalendarMessageMapper = emailCalendarMessageMapper;
    }

    @RabbitListener(queues = "${queue.inbound.email-calendar-event}", messageConverter = "jsonMessageConverter")
    public void receivedMessageInQueue(CalendarEvent message) {
        logger.debug("Received email message : {}", message);
        if (Constants.EMAIL_PATTERN.matcher(message.recipient()).matches()) {
            emailService.sendMessage(
                    new EmailMessage(
                            message.title(),
                            message.recipient(),
                            emailCalendarMessageMapper.convert(message)
                    )
            );
        }
    }
}
