package info.mastera.authservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void createTokenTest() {
        JwtService jwtService = new JwtService("TEST64CHARKEY5368566D5970337336763979244226452948404D63510000000");
        String resultToTest = jwtService.createToken(
                new UsernamePasswordAuthenticationToken("username", "password"));

        assertThat(resultToTest)
                .isNotNull()
                .startsWith("eyJhbGciOiJIUzM4NCJ9.");
    }
}