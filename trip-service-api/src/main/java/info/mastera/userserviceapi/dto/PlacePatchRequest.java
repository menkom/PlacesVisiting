package info.mastera.userserviceapi.dto;

public record PlacePatchRequest(
        String name,
        String country,
        String address,
        Double latitude,
        Double longitude
) {
}
