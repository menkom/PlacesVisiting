package info.mastera.userserviceapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record TripCreateRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate date,
        Long placeId,
        PlaceCreateRequest place,
        List<String> companions
) {
}
