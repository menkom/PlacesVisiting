package info.mastera.emailservice.listener;

import info.mastera.email.dto.EmailMessage;
import info.mastera.email.service.EmailService;
import info.mastera.email.util.Constants;
import info.mastera.emailservice.mapper.EmailCalendarMessageMapper;
import info.mastera.rabbitmq.dto.CalendarEvent;
import info.mastera.rabbitmq.dto.TripReminder;
import info.mastera.userinfo.client.InternalAuthClient;
import info.mastera.userinfo.dto.AccountStatusRequest;
import info.mastera.userinfo.dto.AccountStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailQueueListener.class);

    private final EmailService emailService;
    private final EmailCalendarMessageMapper emailCalendarMessageMapper;
    private final InternalAuthClient internalAuthClient;

    public EmailQueueListener(EmailService emailService,
                              EmailCalendarMessageMapper emailCalendarMessageMapper,
                              InternalAuthClient internalAuthClient) {
        this.emailService = emailService;
        this.emailCalendarMessageMapper = emailCalendarMessageMapper;
        this.internalAuthClient = internalAuthClient;
    }

    @RabbitListener(queues = "${queue.inbound.email-calendar-event}", messageConverter = "jsonMessageConverter")
    public void receivedMessageInQueue(CalendarEvent message) {
        logger.debug("Received request to send email message: {}", message);
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

    @RabbitListener(queues = "${queue.inbound.trip-reminder}", messageConverter = "jsonMessageConverter")
    public void receivedTripReminderMessageInQueue(TripReminder reminderInfo) {
        logger.debug("Received email message : {}", reminderInfo);
        AccountStatusResponse accountStatus = internalAuthClient.getAccountStatus(new AccountStatusRequest(reminderInfo.recipient()));
        if (accountStatus.getUsername() != null) {
            if (Constants.EMAIL_PATTERN.matcher(accountStatus.getUsername()).matches()) {
                emailService.sendMessage(
                        new EmailMessage(
                                "Gentle reminder: " + reminderInfo.title(),
                                accountStatus.getUsername(),
                                emailCalendarMessageMapper.convert(reminderInfo)
                        )
                );
            }
        } else {
            logger.error("No account found for recipient. TripReminder: {}", reminderInfo);
        }
    }
}
