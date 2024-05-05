package info.mastera.userserviceapi.repository;

import info.mastera.userserviceapi.model.Place;
import org.springframework.data.repository.CrudRepository;

public interface PlaceRepository extends CrudRepository<Place, Long> {
}
