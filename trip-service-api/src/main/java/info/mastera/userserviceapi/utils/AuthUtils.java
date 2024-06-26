package info.mastera.userserviceapi.utils;

import info.mastera.userserviceapi.model.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AuthUtils {

    public static Account getAccount() {
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
                && principal instanceof Account account
                ? account
                : null;
    }
}
