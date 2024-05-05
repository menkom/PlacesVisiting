package info.mastera.userserviceapi.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record PlaceCreateRequest(
        @NotEmpty @Length(max = 500) String name,
        @NotEmpty @Length(max = 250) String country,
        @Length(max = 500) String address,
        Double latitude,
        Double longitude
) {
}
