package info.mastera.mocknotificationconsumer.listener;

import info.mastera.rabbitmq.dto.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationQueueListener.class);

    @RabbitListener(queues = "${queue.inbound.notification}", messageConverter = "jsonMessageConverter")
    public void receivedMessageInQueue(NotificationMessage message) {
        logger.info("Received notification message : {}", message);
    }
}
