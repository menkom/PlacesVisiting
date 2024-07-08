package info.mastera.authservice.controller;

import info.mastera.authservice.dto.AccountStatusRequest;
import info.mastera.authservice.dto.LoginRequest;
import info.mastera.authservice.model.Account;
import info.mastera.authservice.repository.AccountRepository;
import info.mastera.authservice.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    private static final String X_INTERNAL_TOKEN = "X-INTERNAL-TOKEN";

    @Autowired
    private MockMvc mvc;
    @SuppressWarnings("unused")
    @Autowired
    private UserService userService;
    @Autowired
    private AccountRepository accountRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16.2-alpine")
            .withDatabaseName("public")
            .withUsername("pUser")
            .withPassword("pass");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    public AccountControllerTest() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
    }

    @BeforeAll
    void setUp() {
        accountRepository.save(
                new Account()
                        .setUsername("usern")
                        .setPassword(encoder.encode("pass+"))
        );
    }

    @Test
    void onlyLoginIsPublicTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/ramdonEndpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void attemptToLoginWithoutBodyFailedTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void attemptToLoginWithInvalidEmptyJsonBodyFailedTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            ",pass",
            "'',pass",
            "user,",
            "user,''",
    })
    void attemptToLoginWithInvalidBodyFailedTest(String username, String password) throws Exception {
        String request = objectMapper.writeValueAsString(new LoginRequest(username, password));

        mvc.perform(MockMvcRequestBuilders
                        .post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void attemptToLoginWithNonExistentUserFailedTest() throws Exception {
        String request = objectMapper.writeValueAsString(new LoginRequest("NonExistentUser", "password"));

        mvc.perform(MockMvcRequestBuilders
                        .post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void loginWithExistingUserTest() throws Exception {
        String request = objectMapper.writeValueAsString(new LoginRequest("usern", "pass+"));

        var resultToTest = mvc.perform(MockMvcRequestBuilders
                        .post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(resultToTest).isNotNull()
                .contains(".")
                .startsWith("eyJhbGciOiJIUzM4NCJ9.");
    }

    @Test
    void loginWithExistingUserAndIncorrectPasswordTest() throws Exception {
        String request = objectMapper.writeValueAsString(new LoginRequest("usern", "pass-"));


        mvc.perform(MockMvcRequestBuilders
                        .post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void createNewUserTest() throws Exception {
        String request = objectMapper.writeValueAsString(
                new LoginRequest("createdUsern", "pass"));

        mvc.perform(MockMvcRequestBuilders
                        .post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void onLoginWithGoogleWeAreRedirectedTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/oauth2/authorization/google"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Nested
    class InternalAccountStatusCheck {

        @Test
        void attemptGetAccountStatusWithoutRequiredTokenTest() throws Exception {
            String request =
                    objectMapper.writeValueAsString(
                            new AccountStatusRequest(null, "anything"));

            mvc.perform(MockMvcRequestBuilders
                            .post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request)
                    )
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        void attemptGetAccountStatusForNonExistentUserTest() throws Exception {
            String request =
                    objectMapper.writeValueAsString(
                            new AccountStatusRequest(null, "not_existing_user"));

            mvc.perform(MockMvcRequestBuilders
                            .post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(X_INTERNAL_TOKEN, "RandomTOKEN")
                            .content(request)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(content().json("{\"id\":null,\"username\":null}"));
        }

        @Test
        void attemptGetAccountStatusForExistingUserTest() throws Exception {
            String request =
                    objectMapper.writeValueAsString(
                            new AccountStatusRequest(null, "usern"));

            var result = mvc.perform(MockMvcRequestBuilders
                            .post("/account")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(X_INTERNAL_TOKEN, "RandomTOKEN")
                            .content(request)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk());


            result.andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.username").exists())
                    .andExpect(jsonPath("$.username").value("usern"));
        }
    }

}