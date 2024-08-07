package info.mastera.userserviceapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

public record TripResponse(
        long id,
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate date,
        PlaceResponse place,
        List<String> companions
) {
}
