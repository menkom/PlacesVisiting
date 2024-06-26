package info.mastera.authservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String username, @NotEmpty String password) {
}
