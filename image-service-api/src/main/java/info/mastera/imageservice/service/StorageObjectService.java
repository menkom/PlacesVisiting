package info.mastera.imageservice.service;

import info.mastera.imageservice.model.StorageObject;
import info.mastera.imageservice.repository.StorageObjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageObjectService {

    private final StorageService storageService;
    private final StorageObjectRepository storageObjectRepository;

    public StorageObjectService(StorageService storageService,
                                StorageObjectRepository storageObjectRepository) {
        this.storageService = storageService;
        this.storageObjectRepository = storageObjectRepository;
    }

    public StorageObject save(Long placeId, MultipartFile file) {
        String fileKey = storageService.upload(placeId, file);
        return storageObjectRepository.save(
                new StorageObject()
                        .setPlaceId(placeId)
                        .setFileKey(fileKey)
        );
    }

    public Page<String> getAll(Long placeId, Pageable pageable) {
        Page<StorageObject> storageObjects = storageObjectRepository.findAllByPlaceId(placeId, pageable);
        return storageObjects.map(storageObject -> storageService.getObjectPublicLink(storageObject.getFileKey()));
    }
}
