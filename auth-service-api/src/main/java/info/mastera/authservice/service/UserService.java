package info.mastera.authservice.service;

import info.mastera.authservice.model.Account;
import info.mastera.authservice.repository.AccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class UserService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        if (null == username || username.isEmpty()) {
            throw new BadCredentialsException("Username can't be empty");
        }
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("Username {0} not found.", username)));
    }
}
