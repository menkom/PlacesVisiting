package info.mastera.rabbitmq.dto;

import java.time.LocalDate;

@SuppressWarnings("unused")
public record CalendarEvent(
        String email,
        LocalDate eventDate,
        String message
) {
}
