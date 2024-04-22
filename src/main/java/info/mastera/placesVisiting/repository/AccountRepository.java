package info.mastera.placesVisiting.repository;

import info.mastera.placesVisiting.model.Account;
import info.mastera.placesVisiting.model.Provider;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByUsernameAndProvider(String username, Provider provider);
}
