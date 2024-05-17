package info.mastera.userserviceapi.dto;

import java.time.LocalDate;
import java.util.List;

public record TripResponse(
        long id,
        LocalDate date,
        String publicId,
        PlaceResponse place,
        List<String> companions
) {
}
