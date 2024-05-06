package info.mastera.userserviceapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.mastera.userserviceapi.dto.ExceptionResponse;
import info.mastera.userserviceapi.service.JwtService;
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

    private final static String HEADER_NAME = "authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwt(request);
        if (jwt == null || jwt.isEmpty() || jwt.split("\\.").length < 2) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            var username = jwtService.getUsername(jwt);
            if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isValid(jwt, userDetails)) {
                    updateContext(request, userDetails);
                }
            }
        } catch (MalformedJwtException | ExpiredJwtException e) {
            handleException(response, e.getMessage());
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