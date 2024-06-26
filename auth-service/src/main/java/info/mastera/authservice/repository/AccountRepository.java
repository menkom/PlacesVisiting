package info.mastera.authservice.repository;

import info.mastera.authservice.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
}
