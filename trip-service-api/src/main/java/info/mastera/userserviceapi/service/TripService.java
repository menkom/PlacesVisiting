package info.mastera.userserviceapi.service;

import info.mastera.security.dto.AccountDto;
import info.mastera.security.utils.AuthUtils;
import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.exception.UnauthorizedException;
import info.mastera.userserviceapi.mapper.PlaceMapper;
import info.mastera.userserviceapi.mapper.TripMapper;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import info.mastera.userserviceapi.producer.EmailCalendarEventProducer;
import info.mastera.userserviceapi.producer.NotificationProducer;
import info.mastera.userserviceapi.repository.PlaceRepository;
import info.mastera.userserviceapi.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final NotificationProducer notificationProducer;
    private final EmailCalendarEventProducer emailCalendarEventProducer;

    public TripService(TripRepository tripRepository,
                       TripMapper tripMapper,
                       PlaceRepository placeRepository,
                       PlaceMapper placeMapper,
                       NotificationProducer notificationProducer,
                       EmailCalendarEventProducer emailCalendarEventProducer) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
        this.notificationProducer = notificationProducer;
        this.emailCalendarEventProducer = emailCalendarEventProducer;
    }

    public TripResponse save(TripCreateRequest request) {
        Place place = null;
        if (request.placeId() != null) {
            place = placeRepository.findById(request.placeId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    MessageFormat.format(
                                            "Place with id={0} not found.",
                                            request.placeId()
                                    )
                            )
                    );
        } else if (request.place() != null) {
            place = placeRepository.save(placeMapper.toEntity(request.place()));
        }
        Trip trip = tripMapper.toEntity(request)
                .setPlace(place)
                .setOwnerId(
                        Optional.ofNullable(AuthUtils.getAccount())
                                .map(AccountDto::getId)
                                .orElseThrow(() -> new EntityNotFoundException("User ID was not found"))
                );

        TripResponse result = tripMapper.fromEntity(
                tripRepository.save(trip));
        notificationProducer.sendNotificationTripStored(trip);
        emailCalendarEventProducer.sendEmailCalendarEvent(trip);
        return result;
    }

    public void delete(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trip with id=%s not found.".formatted(id)));
        Long accountId = Optional.ofNullable(AuthUtils.getAccount())
                .map(AccountDto::getId)
                .orElseThrow(() -> new EntityNotFoundException("User ID was not found"));

        if (trip.getOwnerId().equals(accountId)) {
            if (LocalDate.now().isBefore(trip.getDate())) {
                tripRepository.deleteById(id);
            } else {
                throw new UnauthorizedException("You are not allowed to delete a past trip.");
            }
        } else {
            throw new UnauthorizedException("You are not authorized to delete this trip.");
        }
    }
}
