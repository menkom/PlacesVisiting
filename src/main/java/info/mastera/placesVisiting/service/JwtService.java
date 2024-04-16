package info.mastera.placesVisiting.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Date;

/**
 * JwtService it implements useful functions:
 * <p>
 * generate a JWT token
 * validate a JWT token
 * parse username from JWT token
 */
@Service
public class JwtService {

    private static final String ISSUER = "info.mastera";
    private static final TemporalAmount TOKEN_VALIDITY = Duration.ofMinutes(600L);

    private final String sharedKey;

    public JwtService(@Value("${jwt.shared-key}") String sharedKey) {
        this.sharedKey = sharedKey;
    }

    /**
     * Generates token for granted user and password
     */
    public String createToken(Authentication authentication) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .subject(authentication.getName())
                .issuer(ISSUER)
                .issuedAt(now)
                .expiration(Date.from(now.toInstant().plus(TOKEN_VALIDITY)))
                .signWith(getSigningKey())
// According to JWS Compact Serialization compaction of the JWT to a URL-safe string
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(sharedKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isValid(String jwt, UserDetails userDetails) {
        final String userName = getUsername(jwt);
        return (userName.equals(userDetails.getUsername())) && !isExpired(jwt);
    }

    public String getUsername(String jwt) {
        return getClaims(jwt).getSubject();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private boolean isExpired(String jwt) {
        return getClaims(jwt)
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }
}