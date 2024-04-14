package info.mastera.placesVisiting.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * JwtService it implements useful functions:
 * <p>
 * generate a JWT token
 * validate a JWT token
 * parse username from JWT token
 */
@Service
public class JwtService {

    /**
     * Generates token for granted user and password
     */
    public String createToken(Authentication authentication) {
        return "";
    }
}