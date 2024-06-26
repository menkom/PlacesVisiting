package info.mastera.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.mastera.security.dto.ExceptionResponse;
import info.mastera.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This is a filter base class that is used to guarantee a single execution per request dispatch.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtService jwtService,
                     UserDetailsService userDetailsService,
                     ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwt(request);
        if (jwt == null || jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        if (jwt.split("\\.").length < 2) {
            handleException(response, INVALID_TOKEN_MESSAGE);
            return;
        }
        try {
            var username = jwtService.getUsername(jwt);
            if (username == null || username.isEmpty()) {
                handleException(response, INVALID_TOKEN_MESSAGE);
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isValid(jwt, userDetails)) {
                updateContext(request, userDetails);
            } else {
                handleException(response, INVALID_TOKEN_MESSAGE);
                return;
            }
        } catch (MalformedJwtException e) {
            handleException(response, e.getMessage());
            return;
        } catch (ExpiredJwtException e) {
            handleException(response, "Token expired. Re-login needed.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static void updateContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_NAME);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.replace(BEARER_PREFIX, "");
        }
        return null;
    }

    private void handleException(HttpServletResponse response, String errorMessage)
            throws IOException {
        final ExceptionResponse errorResponseDto = new ExceptionResponse(errorMessage);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
    }
}