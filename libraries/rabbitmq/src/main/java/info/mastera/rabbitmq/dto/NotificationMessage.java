package info.mastera.rabbitmq.dto;

@SuppressWarnings("unused")
public record NotificationMessage(
        Long accountId,
        String message
) {
}
