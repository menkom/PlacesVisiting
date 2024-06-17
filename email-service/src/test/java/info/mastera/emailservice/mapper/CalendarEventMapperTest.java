package info.mastera.emailservice.mapper;


import info.mastera.rabbitmq.dto.CalendarEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class CalendarEventMapperTest {

    private final CalendarEventMapper calendarEventMapper = new CalendarEventMapper();

    @Test
    void calendarEventConvertToGoogleCalendarEventTest() {
        var eventDate = LocalDate.of(2020, 1, 2);
        var calendarEvent = new CalendarEvent("recipient", "title", eventDate, "location", List.of("strange@mail"));

        var resultToTest = calendarEventMapper.convert(calendarEvent);

        Assertions.assertNotNull(resultToTest);
        Assertions.assertEquals("title", resultToTest.getTitle());
        Assertions.assertEquals(eventDate.atTime(9, 0), resultToTest.getStartTime());
        Assertions.assertEquals(eventDate.atTime(18, 0), resultToTest.getEndTime());
        Assertions.assertEquals("location", resultToTest.getLocation());
        Assertions.assertNotNull(resultToTest.getGuests());
        Assertions.assertEquals(1, resultToTest.getGuests().size());
        Assertions.assertEquals(List.of("strange@mail"), resultToTest.getGuests());
    }
}