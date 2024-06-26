package info.mastera.security.mapper;

import info.mastera.security.dto.AccountDto;
import info.mastera.security.dto.AccountStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountStatusMapper {

    @Mapping(target = "authorities", ignore = true)
    AccountDto convert(AccountStatusResponse userDetails);
}
