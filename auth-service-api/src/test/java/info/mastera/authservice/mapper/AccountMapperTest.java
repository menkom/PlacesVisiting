package info.mastera.authservice.mapper;

import info.mastera.authservice.model.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AccountMapperTest {

    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void mapperWorksTest() {
        Account account = new Account()
                .setId(12L)
                .setUsername("User@");

        var resultToTest = mapper.convert(account);

        Assertions.assertThat(resultToTest).isNotNull();
        Assertions.assertThat(resultToTest.id()).isEqualTo(12L);
        Assertions.assertThat(resultToTest.username()).isEqualTo("User@");
    }
}