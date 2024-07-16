package info.mastera.authservice.config;

import info.mastera.authservice.filter.JwtFilter;
import info.mastera.authservice.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtFilter jwtFilter,
                                                   OAuth2UserService oAuth2UserService) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/",
                                        "/favicon.ico",
                                        "/account",
                                        "/account/*",
                                        "/login**",
                                        "/actuator/**",
                                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                        "/error").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(IF_REQUIRED))
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(infoEndpoint -> infoEndpoint.userService(oAuth2UserService)))
                .formLogin(formLogin -> formLogin
                        .loginPage("/account/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                )
                .oidcLogout(logout -> logout
                        .backChannel(Customizer.withDefaults())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                return 401 instead of redirecting to login page
                .exceptionHandling(exceptionConfigurer ->
                        exceptionConfigurer.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .build();
    }
}
