package info.mastera.imageservice.repository;

import info.mastera.imageservice.model.StorageObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageObjectRepository extends JpaRepository<StorageObject, Long> {

    Page<StorageObject> findAllByPlaceId(Long placeId, Pageable pageable);
}
