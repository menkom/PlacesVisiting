package info.mastera.userserviceapi.mapper;

import info.mastera.userserviceapi.dto.PlaceCreateRequest;
import info.mastera.userserviceapi.dto.PlacePatchRequest;
import info.mastera.userserviceapi.dto.PlaceResponse;
import info.mastera.userserviceapi.model.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    
    @Mapping(target = "id", ignore = true)
    Place toEntity(PlaceCreateRequest request);

    @Mapping(target = "id", ignore = true)
    Place toEntity(PlacePatchRequest request);

    PlaceResponse fromEntity(Place place);
}
