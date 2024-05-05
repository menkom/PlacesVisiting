package info.mastera.userserviceapi.mapper;

import info.mastera.userserviceapi.dto.PlaceCreateRequest;
import info.mastera.userserviceapi.dto.PlacePatchRequest;
import info.mastera.userserviceapi.dto.PlaceResponse;
import info.mastera.userserviceapi.model.Place;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    Place toEntity(PlaceCreateRequest request);

    Place toEntity(PlacePatchRequest request);

    PlaceResponse fromEntity(Place place);
}
