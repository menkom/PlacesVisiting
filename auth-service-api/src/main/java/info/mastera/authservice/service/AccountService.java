package info.mastera.authservice.service;

import info.mastera.authservice.dto.AccountStatusResponse;
import info.mastera.authservice.mapper.AccountMapper;
import info.mastera.authservice.model.Account;
import info.mastera.authservice.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    public AccountService(JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          AccountMapper accountMapper) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
    }

    public String login(String username, String password) {
        Authentication authenticatedUser =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtService.createToken(authenticatedUser);
    }

    public boolean create(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        //if password is null then this mean that user was authenticated by Google account before
        // and he can create password
        if (account.isPresent() && account.get().getPassword() != null) {
            throw new BadCredentialsException("Username is already in use");
        }
        accountRepository.save(
                new Account()
                        .setUsername(username)
                        .setPassword(passwordEncoder.encode(password))
        );
        return true;
    }

    public AccountStatusResponse getAccountStatus(String username) {
        return accountRepository.findByUsername(username)
                .map(accountMapper::convert)
                .orElse(new AccountStatusResponse());
    }
}
