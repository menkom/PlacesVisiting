package info.mastera.userserviceapi.service;

import info.mastera.security.dto.AccountDto;
import info.mastera.security.utils.AuthUtils;
import info.mastera.userserviceapi.dto.PlaceCreateRequest;
import info.mastera.userserviceapi.dto.TripCreateRequest;
import info.mastera.userserviceapi.mapper.PlaceMapper;
import info.mastera.userserviceapi.mapper.TripMapper;
import info.mastera.userserviceapi.model.Place;
import info.mastera.userserviceapi.model.Trip;
import info.mastera.userserviceapi.producer.EmailProducer;
import info.mastera.userserviceapi.producer.NotificationProducer;
import info.mastera.userserviceapi.repository.PlaceRepository;
import info.mastera.userserviceapi.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

class TripServiceTest {
    private final TripRepository tripRepository = Mockito.mock(TripRepository.class);
    private final PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
    private final TripMapper tripMapper = Mappers.getMapper(TripMapper.class);
    private final PlaceMapper placeMapper = Mappers.getMapper(PlaceMapper.class);
    private final NotificationProducer notificationProducer = Mockito.mock(NotificationProducer.class);
    private final EmailProducer emailCalendarEventProducer = Mockito.mock(EmailProducer.class);
    private final TripService tripService =
            new TripService(tripRepository, tripMapper, placeRepository, placeMapper, notificationProducer, emailCalendarEventProducer);

    private static MockedStatic<AuthUtils> authUtilsMocked;

    @BeforeAll
    static void setUp() {
        authUtilsMocked = Mockito.mockStatic(AuthUtils.class);

        AccountDto accountDto = new AccountDto();
        accountDto.setId(2L);
        authUtilsMocked.when(AuthUtils::getAccount)
                .thenReturn(accountDto);
    }

    @AfterAll
    static void clean() {
        if (!authUtilsMocked.isClosed()) {
            authUtilsMocked.close();
        }
    }

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    @Nested
    class SaveTest {
        private PlaceCreateRequest placeCreateRequest =
                new PlaceCreateRequest("placeName", "countryName", "address",
                        12d, 45d);
        private TripCreateRequest tripCreateRequest =
                new TripCreateRequest(LocalDate.of(2020, 4, 5),
                        1L, placeCreateRequest, List.of());

        @Test
        void save() {
            tripCreateRequest = new TripCreateRequest(LocalDate.of(2020, 4, 5),
                    1L, null, List.of());
            Mockito.doReturn(Optional.of(new Place()))
                    .when(placeRepository)
                    .findById(anyLong());

            tripService.save(tripCreateRequest);

            verify(tripRepository).save(any(Trip.class));
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void noTripFoundToDeleteTest() {
            Mockito.doThrow(EntityNotFoundException.class)
                    .when(tripRepository)
                    .findById(anyLong());

            Assertions.assertThrows(EntityNotFoundException.class, () -> tripService.delete(1L));
        }

        @Test
        void tripIsDeletedTest() {
            Mockito.doReturn(Optional.of(
                            new Trip()
                                    .setId(1L)
                                    .setDate(LocalDate.now().plusDays(1))
                                    .setOwnerId(2L)
                    ))
                    .when(tripRepository)
                    .findById(anyLong());

            tripService.delete(1L);

            verify(tripRepository).deleteById(anyLong());
        }
    }
}