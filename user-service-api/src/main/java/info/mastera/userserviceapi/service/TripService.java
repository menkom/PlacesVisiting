package info.mastera.userserviceapi.service;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.mapper.PlaceMapper;
import info.mastera.userserviceapi.mapper.TripMapper;
import info.mastera.userserviceapi.model.Account;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import info.mastera.userserviceapi.repository.AccountRepository;
import info.mastera.userserviceapi.repository.PlaceRepository;
import info.mastera.userserviceapi.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final AccountRepository accountRepository;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    public TripService(TripRepository tripRepository,
                       TripMapper tripMapper,
                       AccountRepository accountRepository,
                       PlaceRepository placeRepository,
                       PlaceMapper placeMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.accountRepository = accountRepository;
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    public TripResponse save(TripCreateRequest request, String username) {
        Long ownerId = accountRepository.findByUsername(username)
                .map(Account::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Current username not found."));
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
        return tripMapper.fromEntity(
                tripRepository.save(trip));
    }

//    public PlaceResponse findById(Long id) {
//        return placeMapper.fromEntity(
//                placeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Place not found")));
//    }

}
