package info.mastera.userserviceapi.mapper;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "place.id", ignore = true)
    Trip toEntity(TripCreateRequest request);

    TripResponse fromEntity(Trip trip);
}
