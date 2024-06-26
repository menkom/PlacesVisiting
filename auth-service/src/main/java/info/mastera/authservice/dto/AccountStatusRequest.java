package info.mastera.authservice.dto;

import org.springframework.lang.NonNull;

public record AccountStatusRequest(
        @NonNull String username
) {
}
