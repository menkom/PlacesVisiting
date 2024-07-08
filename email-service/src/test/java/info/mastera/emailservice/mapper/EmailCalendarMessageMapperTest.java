package info.mastera.emailservice.mapper;

import info.mastera.googlecalendarevent.service.GoogleCalendarLinkService;
import info.mastera.rabbitmq.dto.CalendarEvent;
import info.mastera.rabbitmq.dto.TripReminder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class EmailCalendarMessageMapperTest {

    private final CalendarEventMapper calendarEventMapper = new CalendarEventMapper();
    private final GoogleCalendarLinkService googleCalendarLinkService = new GoogleCalendarLinkService();
    private final EmailCalendarMessageMapper emailCalendarMessageMapper =
            new EmailCalendarMessageMapper(googleCalendarLinkService, calendarEventMapper);

    @Test
    void convertFromCalendarEventTest() {
        var eventDate = LocalDate.of(2020, 1, 2);
        var calendarEvent = new CalendarEvent("recipient", "title", eventDate, "location", List.of("strange@mail"));

        var resultToTest = emailCalendarMessageMapper.convert(calendarEvent);

        Assertions.assertThat(resultToTest)
                .isNotNull()
                .isEqualTo("You can follow this link https://calendar.google.com/calendar/render?add=strange%40mail&trp=true&action=TEMPLATE&dates=20200102T090000%2F20200102T180000&location=location&text=title&crm=AVAILABLE to add event to your google calendar");
    }

    @Test
    void convertFromTripReminderTest() {
        var eventDate = LocalDate.of(2020, 1, 2);
        var tripReminder = new TripReminder(123L, "title", eventDate, "location", List.of("strange@mail"));

        var resultToTest = emailCalendarMessageMapper.convert(tripReminder);

        Assertions.assertThat(resultToTest)
                .isNotNull()
                .isEqualTo("Kind reminder about your trip to location planned for tomorrow");
    }
}