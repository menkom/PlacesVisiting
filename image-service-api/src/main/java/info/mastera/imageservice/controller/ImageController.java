package info.mastera.imageservice.controller;

import info.mastera.imageservice.model.StorageObject;
import info.mastera.imageservice.service.StorageObjectService;
import info.mastera.imageservice.validator.ValidImageType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final StorageObjectService storageObjectService;

    public ImageController(StorageObjectService storageObjectService) {
        this.storageObjectService = storageObjectService;
    }

    @PostMapping(value = "{placeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public StorageObject upload(
            @ValidImageType @NonNull @RequestParam MultipartFile file,
            @PathVariable Long placeId) {
        logger.info("Uploading file. placeId: {}, filename: {}, type: {}, size: {}",
                placeId,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize());
        return storageObjectService.save(placeId, file);
    }

    @GetMapping(value = "{placeId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<String> getImageLinks(@PathVariable long placeId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "3") int size) {
        return storageObjectService.getAll(placeId, PageRequest.of(page, size));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException cve) {
        String errorMessages = cve.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("."));

        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleConstraintViolationException(MissingServletRequestPartException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
