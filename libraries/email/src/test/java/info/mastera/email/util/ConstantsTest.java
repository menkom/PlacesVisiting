package info.mastera.email.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstantsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "username@domain.com",
            "user.name@domain.com",
            "user-name@domain.com",
            "username@domain.co.in",
            "user_name@domain.com",
    })
    void passEmailRegexTest(String email) {
        assertTrue(Constants.EMAIL_PATTERN.matcher(email).matches());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "user",
            "username.@domain.com",
            ".user.name@domain.com",
            "user-name@domain.com.",
            "username@.com",
    })
    void failedEmailRegexTest(String email) {
        assertFalse(Constants.EMAIL_PATTERN.matcher(email).matches());
    }
}