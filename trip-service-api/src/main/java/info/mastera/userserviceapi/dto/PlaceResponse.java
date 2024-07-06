package info.mastera.userserviceapi.dto;

public record PlaceResponse(
        long id,
        String name,
        String country,
        String address,
        Double latitude,
        Double longitude
) {
}
