package info.mastera.userserviceapi.producer;

import info.mastera.rabbitmq.dto.NotificationMessage;
import info.mastera.userserviceapi.model.Account;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import info.mastera.userserviceapi.utils.AuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);

    @Value("${queue.outbound.notification}")
    private String notificationQueue;

    private final RabbitTemplate template;

    public NotificationProducer(RabbitTemplate template) {
        this.template = template;
    }

    public void sendNotificationTripStored(Trip trip) {
        String placeName = Optional.ofNullable(trip.getPlace())
                .map(Place::getName)
                .map(" to %s"::formatted)
                .orElse("");
        String message = "Trip on %s%s stored".formatted(trip.getDate(), placeName);
        sendMessage(new NotificationMessage(trip.getOwnerId(), message));
    }

    public void sendNotificationPlaceStored(Place place) {
        Optional<Long> accountId = Optional.ofNullable(AuthUtils.getAccount())
                .map(Account::getId);
        if (accountId.isPresent()) {
            String country = Optional.ofNullable(place.getCountry())
                    .map(" in %s"::formatted)
                    .orElse("");
            String message = "Place with name %s%s stored"
                    .formatted(place.getName(), country);

            sendMessage(new NotificationMessage(accountId.get(), message));
        } else {
            logger.error("No account id found on saving {}", place);
        }
    }

    private void sendMessage(NotificationMessage message) {
        logger.info("Sending message {} to {}", message, notificationQueue);
        template.convertAndSend(notificationQueue, message);
    }
}
