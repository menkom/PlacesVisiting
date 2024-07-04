package info.mastera.userinfo.dto;

public record AccountStatusRequest(
        Long userId,
        String username
) {

    public AccountStatusRequest(long userId) {
        this(userId, null);
    }

    public AccountStatusRequest(String username) {
        this(null, username);
    }
}
