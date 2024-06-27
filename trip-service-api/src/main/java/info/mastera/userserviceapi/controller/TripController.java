package info.mastera.userserviceapi.controller;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.service.TripService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public TripResponse create(@RequestBody @Valid TripCreateRequest request) {
        return tripService.save(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tripService.delete(id);
    }
}
