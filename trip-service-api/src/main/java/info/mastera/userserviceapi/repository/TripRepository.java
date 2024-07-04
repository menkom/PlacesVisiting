package info.mastera.userserviceapi.repository;

import info.mastera.userserviceapi.model.Trip;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TripRepository extends CrudRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {

    List<Trip> findByDate(LocalDate dateToFilter);
}
