package info.mastera.security.service;

import info.mastera.security.mapper.AccountStatusMapper;
import info.mastera.userinfo.client.InternalAuthClient;
import info.mastera.userinfo.dto.AccountStatusRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final InternalAuthClient internalAuthClient;
    private final AccountStatusMapper accountStatusMapper;

    public UserService(InternalAuthClient internalAuthClient,
                       AccountStatusMapper accountStatusMapper) {
        this.internalAuthClient = internalAuthClient;
        this.accountStatusMapper = accountStatusMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (null == username || username.isEmpty()) {
            throw new BadCredentialsException("Username can't be empty");
        }
        return accountStatusMapper.convert(
                internalAuthClient.getAccountStatus(new AccountStatusRequest(username))
        );
    }
}
