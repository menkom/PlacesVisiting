package info.mastera.userserviceapi.mapper;

import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationMessageMapper {

    public String convert(Trip trip) {
        String placeName = Optional.ofNullable(trip.getPlace())
                .map(Place::getName)
                .map(" to %s"::formatted)
                .orElse("");
        return "Trip on %s%s stored".formatted(trip.getDate(), placeName);
    }

    public String convert(Place place) {
        String country = Optional.ofNullable(place.getCountry())
                .map(" in %s"::formatted)
                .orElse("");
        return "Place with name %s%s stored"
                .formatted(place.getName(), country);
    }
}
