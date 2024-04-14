package info.mastera.placesVisiting.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AccountService(JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String login(String username, String password) {
        Authentication authenticatedUser =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtService.createToken(authenticatedUser);
    }
}
