package info.mastera.email.dto;

import org.springframework.lang.NonNull;

public record EmailMessage(
        @NonNull String title,
        @NonNull String recipient,
        @NonNull String content
) {
}
