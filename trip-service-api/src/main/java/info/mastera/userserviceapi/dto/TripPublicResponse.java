package info.mastera.userserviceapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public record TripPublicResponse(
        long id,
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate date,
        PlaceResponse place
) {
}
