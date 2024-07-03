package info.mastera.security.utils;

import info.mastera.security.dto.AccountDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public class AuthUtils {

    public static AccountDto getAccount() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (Objects.isNull(securityContext)) {
            return null;
        }

        Authentication authentication = securityContext.getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        return Objects.nonNull(principal)
                && principal instanceof AccountDto account
                ? account
                : null;
    }
}
