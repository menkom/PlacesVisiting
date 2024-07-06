package info.mastera.userserviceapi.controller;

import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.dto.TripPublicResponse;
import info.mastera.userserviceapi.dto.TripResponse;
import info.mastera.userserviceapi.dto.TripSearchRequest;
import info.mastera.userserviceapi.service.TripService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/{id}")
    public TripResponse get(@PathVariable Long id) {
        return tripService.getOwned(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tripService.delete(id);
    }

    @GetMapping
    public Page<TripResponse> find(
            @PageableDefault(size = 5, sort = "id") Pageable pageable,
            @ModelAttribute TripSearchRequest filter) {
        return tripService.findAll(filter, pageable);
    }

    @GetMapping("/public/{id}")
    public TripPublicResponse getPublicTrip(@PathVariable String id) {
        return tripService.getPublic(1L);
    }
}
