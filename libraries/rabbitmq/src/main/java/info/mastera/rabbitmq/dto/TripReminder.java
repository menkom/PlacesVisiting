package info.mastera.rabbitmq.dto;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
public record TripReminder(
        long recipient,
        String title,
        LocalDate eventDate,
        String location,
        List<String> guests
) {
}
