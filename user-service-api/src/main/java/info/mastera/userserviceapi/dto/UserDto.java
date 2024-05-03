package info.mastera.userserviceapi.dto;

import info.mastera.userserviceapi.model.Provider;

public record UserDto(String username, Provider provider) {
}
