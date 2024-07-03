package info.mastera.userserviceapi.dto;

import java.time.LocalDate;

public record TripSearchRequest(
        LocalDate from,
        LocalDate to,
        String country,
        String companion
) {
}
