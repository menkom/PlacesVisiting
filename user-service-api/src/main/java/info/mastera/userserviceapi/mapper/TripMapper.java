package info.mastera.userserviceapi.mapper;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.model.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TripMapper {

    Trip toEntity(TripCreateRequest request);

    TripResponse fromEntity(Trip trip);
}
