package info.mastera.userserviceapi.service;

import info.mastera.userserviceapi.model.Account;
import info.mastera.userserviceapi.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserService.class);

    private final AccountRepository accountRepository;

    public OAuth2UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        logger.info("Loading {} user", oAuth2UserRequest.getClientRegistration().getRegistrationId());
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        String userIdentifier = oAuth2User.getAttribute("email");

        if (userIdentifier == null || userIdentifier.isEmpty()) {
            throw new IllegalArgumentException("Missing required attribute email");
        }
        Account account = accountRepository.findByUsername(userIdentifier)
                .orElseGet(() -> registerNewUser(userIdentifier));
        return formUser(account);
    }

    private Account registerNewUser(String userIdentifier) {
        Account user = new Account().setUsername(userIdentifier);
        return accountRepository.save(user);
    }

    private OAuth2User formUser(Account account) {
        return new DefaultOAuth2User(
                Collections.emptyList(),
                Map.of("username", account.getUsername()),
                "username");
    }
}
