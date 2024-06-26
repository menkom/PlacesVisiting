package info.mastera.userserviceapi.controller;

import info.mastera.userserviceapi.dto.PlaceCreateRequest;
import info.mastera.userserviceapi.dto.PlacePatchRequest;
import info.mastera.userserviceapi.dto.PlaceResponse;
import info.mastera.userserviceapi.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping
    public PlaceResponse create(@RequestBody @Valid PlaceCreateRequest request) {
        return placeService.save(request);
    }

    @GetMapping("/{id}")
    public PlaceResponse getPlace(@PathVariable Long id) {
        return placeService.findById(id);
    }

    @PatchMapping("/{id}")
    public PlaceResponse update(@PathVariable Long id, @RequestBody @Valid PlacePatchRequest request) throws IllegalAccessException {
        return placeService.update(id, request);
    }
}
