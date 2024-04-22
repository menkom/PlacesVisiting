package info.mastera.placesVisiting.service;

import info.mastera.placesVisiting.dto.UserDto;
import info.mastera.placesVisiting.model.Account;
import info.mastera.placesVisiting.model.Provider;
import info.mastera.placesVisiting.repository.AccountRepository;
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
        UserDto user = new UserDto(oAuth2User.getAttribute("email"), Provider.GOOGLE);

        if (user.username() == null || user.username().isEmpty()) {
            throw new IllegalArgumentException("Missing required attribute email");
        }
        Account account = accountRepository.findByUsernameAndProvider(user.username(), user.provider())
                .map(existingUser -> updateExistingUser(existingUser, user))
                .orElseGet(() -> registerNewUser(user));
        return formUser(account);
    }

    private Account registerNewUser(UserDto userDto) {
        Account user = new Account()
                .setUsername(userDto.username())
                .setProvider(userDto.provider());
        return accountRepository.save(user);
    }

    private Account updateExistingUser(Account existingAccount, UserDto userInfoDto) {
        existingAccount.setUsername(userInfoDto.username());
        return accountRepository.save(existingAccount);
    }

    private OAuth2User formUser(Account account) {
        return new DefaultOAuth2User(
                Collections.emptyList(),
                Map.of("username", account.getUsername()),
                "username");
    }
}
