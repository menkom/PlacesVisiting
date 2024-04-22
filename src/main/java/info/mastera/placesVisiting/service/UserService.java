package info.mastera.placesVisiting.service;

import info.mastera.placesVisiting.model.Provider;
import info.mastera.placesVisiting.repository.AccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (null == username || username.isEmpty()) {
            throw new BadCredentialsException("Username can't be empty");
        }
        return accountRepository.findByUsernameAndProvider(username, Provider.BASIC)
                .map(account -> new User(account.getUsername(), account.getPassword(), new ArrayList<>()))
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("Username {0} not found.", username)));
    }
}
