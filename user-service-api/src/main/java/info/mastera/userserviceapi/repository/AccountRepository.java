package info.mastera.userserviceapi.repository;

import info.mastera.userserviceapi.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
}
