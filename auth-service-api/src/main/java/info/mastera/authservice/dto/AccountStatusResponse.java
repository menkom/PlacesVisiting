package info.mastera.authservice.dto;

public record AccountStatusResponse(
        Long id,
        String username
) {

    public AccountStatusResponse(){
        this(null, null);
    }
}
