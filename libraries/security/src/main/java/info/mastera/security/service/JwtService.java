package info.mastera.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
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

    private final String sharedKey;

    public JwtService(@Value("${jwt.shared-key}") String sharedKey) {
        this.sharedKey = sharedKey;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(sharedKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isValid(String jwt, UserDetails userDetails) {
        final String userName = getUsername(jwt);
        return userName != null
                && userName.equals(userDetails.getUsername())
                && !isExpired(jwt);
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
