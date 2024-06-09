package info.mastera.userserviceapi.service;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.mapper.PlaceMapper;
import info.mastera.userserviceapi.mapper.TripMapper;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import info.mastera.userserviceapi.producer.NotificationProducer;
import info.mastera.userserviceapi.repository.PlaceRepository;
import info.mastera.userserviceapi.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final NotificationProducer notificationProducer;

    public TripService(TripRepository tripRepository,
                       TripMapper tripMapper,
                       PlaceRepository placeRepository,
                       PlaceMapper placeMapper,
                       NotificationProducer notificationProducer) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
        this.notificationProducer = notificationProducer;
    }

    public TripResponse save(TripCreateRequest request, Long ownerId) {
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
                .setOwnerId(ownerId)
                .setPlace(place);


        TripResponse result = tripMapper.fromEntity(
                tripRepository.save(trip));
        notificationProducer.sendNotificationTripStored(trip);
        return result;
    }
}
