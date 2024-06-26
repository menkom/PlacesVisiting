package info.mastera.security.mapper;

import info.mastera.security.dto.AccountStatusResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AccountStatusMapperTest {

    private final AccountStatusMapper mapper = Mappers.getMapper(AccountStatusMapper.class);

    @Test
    void convertAccountStatusResponseTest() {
        AccountStatusResponse accountStatusResponse =
                new AccountStatusResponse(23L, "UserFromResponse@");

        var resultToTest = mapper.convert(accountStatusResponse);

        Assertions.assertThat(resultToTest).isNotNull();
        Assertions.assertThat(resultToTest.getId()).isEqualTo(23L);
        Assertions.assertThat(resultToTest.getUsername()).isEqualTo("UserFromResponse@");
    }
}