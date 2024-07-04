package info.mastera.authservice.dto;

public record AccountStatusRequest(
        Long userId,
        String username
) {
}
