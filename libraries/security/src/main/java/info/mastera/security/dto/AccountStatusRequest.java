package info.mastera.security.dto;

import org.springframework.lang.NonNull;

public record AccountStatusRequest(
        @NonNull String username
) {
}
