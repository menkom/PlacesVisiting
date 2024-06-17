package info.mastera.userserviceapi.mapper;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface TripMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "place.id", ignore = true)
    Trip toEntity(TripCreateRequest request);

    TripResponse fromEntity(Trip trip);

    default String toTitle(Trip trip) {
        String place = Optional.ofNullable(trip.getPlace())
                .map(Place::getName)
                .map(name -> "to " + name)
                .orElse("");
        String visitDate = Optional.ofNullable(trip.getDate())
                .map(LocalDate::toString)
                .map(date -> "on " + date)
                .orElse("");
        return "Trip %s %s".formatted(place, visitDate);
    }

    default String toLocation(Trip trip) {
        Optional<Place> placeOptional = Optional.ofNullable(trip.getPlace());

        if (placeOptional.isPresent()) {
            Place place = placeOptional.get();
            if (place.getLatitude() != null && place.getLongitude() != null) {
                return place.getLatitude() + " " + place.getLongitude();
            } else {
                return place.getAddress();
            }
        }
        return "";
    }
}
