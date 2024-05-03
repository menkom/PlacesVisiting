package info.mastera.userserviceapi.repository;

import info.mastera.userserviceapi.model.Account;
import info.mastera.userserviceapi.model.Provider;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByUsernameAndProvider(String username, Provider provider);
}
