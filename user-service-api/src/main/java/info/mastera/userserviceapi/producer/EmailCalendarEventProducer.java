package info.mastera.userserviceapi.producer;

import info.mastera.rabbitmq.dto.CalendarEvent;
import info.mastera.userserviceapi.mapper.TripMapper;
import info.mastera.userserviceapi.model.Account;
import info.mastera.userserviceapi.model.Trip;
import info.mastera.userserviceapi.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailCalendarEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EmailCalendarEventProducer.class);

    @Value("${queue.outbound.email-calendar-event}")
    private String emailCalendarEventQueue;

    private final RabbitTemplate template;
    private final TripMapper tripMapper;

    public EmailCalendarEventProducer(RabbitTemplate template,
                                      TripMapper tripMapper) {
        this.template = template;
        this.tripMapper = tripMapper;
    }

    public void sendEmailCalendarEvent(Trip trip) {
        Optional<String> userEmail = Optional.ofNullable(AuthUtils.getAccount())
                .map(Account::getUsername);
        if (userEmail.isPresent()) {
            CalendarEvent calendarEvent =
                    new CalendarEvent(userEmail.get(),
                            tripMapper.toTitle(trip),
                            trip.getDate(),
                            tripMapper.toLocation(trip),
                            trip.getCompanions());
            sendMessage(calendarEvent);
        } else {
            logger.error("No account id found on saving {}", trip);
        }
    }

    private void sendMessage(CalendarEvent calendarEvent) {
        logger.info("Sending message {} to {}", calendarEvent, emailCalendarEventQueue);
        template.convertAndSend(emailCalendarEventQueue, calendarEvent);
    }
}
