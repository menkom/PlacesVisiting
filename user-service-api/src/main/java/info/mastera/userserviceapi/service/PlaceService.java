package info.mastera.userserviceapi.service;

import info.mastera.userserviceapi.dto.PlaceCreateRequest;
import info.mastera.userserviceapi.dto.PlacePatchRequest;
import info.mastera.userserviceapi.dto.PlaceResponse;
import info.mastera.userserviceapi.mapper.PlaceMapper;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final Patcher patcher;

    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper, Patcher patcher) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
        this.patcher = patcher;
    }

    public PlaceResponse save(PlaceCreateRequest request) {
        return placeMapper.fromEntity(
                placeRepository.save(placeMapper.toEntity(request)));
    }

    public PlaceResponse findById(Long id) {
        return placeMapper.fromEntity(
                placeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Place not found")));
    }

    public PlaceResponse update(long id, PlacePatchRequest request) throws IllegalAccessException {
        Place existingPlace = placeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Place not found"));
        patcher.patch(existingPlace, placeMapper.toEntity(request));
        return placeMapper.fromEntity(placeRepository.save(existingPlace));
    }
}
