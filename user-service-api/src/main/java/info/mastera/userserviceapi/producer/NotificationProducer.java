package info.mastera.userserviceapi.producer;

import info.mastera.rabbitmq.dto.NotificationMessage;
import info.mastera.userserviceapi.mapper.NotificationMessageMapper;
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
    private final NotificationMessageMapper notificationMessageMapper;

    public NotificationProducer(RabbitTemplate template,
                                NotificationMessageMapper notificationMessageMapper) {
        this.template = template;
        this.notificationMessageMapper = notificationMessageMapper;
    }

    public void sendNotificationTripStored(Trip trip) {
        NotificationMessage notificationMessage =
                new NotificationMessage(trip.getOwnerId(), notificationMessageMapper.convert(trip));
        sendMessage(notificationMessage);
    }

    public void sendNotificationPlaceStored(Place place) {
        Optional<Long> accountId = Optional.ofNullable(AuthUtils.getAccount())
                .map(Account::getId);
        if (accountId.isPresent()) {
            NotificationMessage notificationMessage =
                    new NotificationMessage(accountId.get(), notificationMessageMapper.convert(place));
            sendMessage(notificationMessage);
        } else {
            logger.error("No account id found on saving {}", place);
        }
    }

    private void sendMessage(NotificationMessage message) {
        logger.info("Sending message {} to {}", message, notificationQueue);
        template.convertAndSend(notificationQueue, message);
    }
}
