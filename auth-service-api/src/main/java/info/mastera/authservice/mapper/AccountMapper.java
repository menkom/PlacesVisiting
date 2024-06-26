package info.mastera.authservice.mapper;

import info.mastera.authservice.dto.AccountStatusResponse;
import info.mastera.authservice.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountStatusResponse convert(Account account);
}
