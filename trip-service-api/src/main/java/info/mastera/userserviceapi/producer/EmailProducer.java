package info.mastera.userserviceapi.producer;

import info.mastera.rabbitmq.dto.CalendarEvent;
import info.mastera.rabbitmq.dto.TripReminder;
import info.mastera.security.dto.AccountDto;
import info.mastera.security.utils.AuthUtils;
import info.mastera.userserviceapi.mapper.TripMapper;
import info.mastera.userserviceapi.model.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailProducer {

    private static final Logger logger = LoggerFactory.getLogger(EmailProducer.class);

    @Value("${queue.outbound.email-calendar-event}")
    private String emailCalendarEventQueue;
    @Value("${queue.outbound.trip-reminder}")
    private String tripReminderQueue;

    private final RabbitTemplate template;
    private final TripMapper tripMapper;

    public EmailProducer(RabbitTemplate template,
                         TripMapper tripMapper) {
        this.template = template;
        this.tripMapper = tripMapper;
    }

    public void sendEmailCalendarEvent(Trip trip) {
        Optional<String> userEmail = Optional.ofNullable(AuthUtils.getAccount())
                .map(AccountDto::getUsername);
        if (userEmail.isPresent()) {
            CalendarEvent calendarEvent =
                    new CalendarEvent(userEmail.get(),
                            tripMapper.toTitle(trip),
                            trip.getDate(),
                            tripMapper.toLocation(trip),
                            trip.getCompanions());
            sendMessage(emailCalendarEventQueue, calendarEvent);
        } else {
            logger.error("No account id found on saving {}", trip);
        }
    }

    public void sendTripReminder(Trip trip) {
        logger.info("Sending trip reminder for trip {}", trip);
        TripReminder tripReminder =
                new TripReminder(trip.getOwnerId(),
                        tripMapper.toTitle(trip),
                        trip.getDate(),
                        tripMapper.toLocation(trip),
                        trip.getCompanions());
        sendMessage(tripReminderQueue, tripReminder);
    }

    private void sendMessage(String queueName, Object event) {
        logger.info("Sending message {} to {}", event, queueName);
        template.convertAndSend(queueName, event);
    }
}
