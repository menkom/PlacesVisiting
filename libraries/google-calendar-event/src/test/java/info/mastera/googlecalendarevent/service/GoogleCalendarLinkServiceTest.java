package info.mastera.googlecalendarevent.service;

import info.mastera.googlecalendarevent.model.Availability;
import info.mastera.googlecalendarevent.model.EventSource;
import info.mastera.googlecalendarevent.model.GoogleCalendarEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoogleCalendarLinkServiceTest {

    GoogleCalendarLinkService calendarLinkService = new GoogleCalendarLinkService();

    @Test
    void genericTest() {
        final String draftLink = "https://calendar.google.com/calendar/render?add=guest1%40gmail.com&trp=true&action=TEMPLATE&dates={startTime}%2F{endTime}&details=Discuss+current+state+of+the+project&location=Warsaw%2C+Poland&text=Weekly+Stand+Up&crm=BUSY&sprop=website:www.santa.org";
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0);
        LocalDateTime endTime = startTime.withMinute(30).withSecond(0);
        String expectedLink = draftLink
                .replace("{startTime}", GoogleCalendarLinkService.DATE_TIME_FORMATTER.format(startTime))
                .replace("{endTime}", GoogleCalendarLinkService.DATE_TIME_FORMATTER.format(endTime));
        GoogleCalendarEvent calendarEvent = new GoogleCalendarEvent()
                .setTitle("Weekly Stand Up")
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setLocation("Warsaw, Poland")
                .addGuest("guest1@gmail.com")
                .setDescription("Discuss current state of the project")
                .setAvailability(Availability.BUSY)
                .addEventSource(EventSource.WEBSITE, "www.santa.org");

        var resultToTest = calendarLinkService.generate(calendarEvent);

        assertEquals(expectedLink, resultToTest);
    }

    @Test
    void emptyTitleTest() {
        GoogleCalendarEvent calendarEvent = new GoogleCalendarEvent();

        IllegalArgumentException resultToTest = assertThrows(IllegalArgumentException.class, () -> calendarLinkService.generate(calendarEvent));

        assertEquals("Title can't be null", resultToTest.getMessage());
    }

    @Test
    void emptyActionTest() {
        GoogleCalendarEvent calendarEvent = new GoogleCalendarEvent().setAction(null);

        IllegalArgumentException resultToTest = assertThrows(IllegalArgumentException.class, () -> calendarLinkService.generate(calendarEvent));

        assertEquals("Parameter `action` can't be null", resultToTest.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            ",",
            "1986-04-08T12:30,",
            ",1986-04-08T12:30",
            })
    void emptyDateTest(LocalDateTime start, LocalDateTime end) {
        GoogleCalendarEvent calendarEvent = new GoogleCalendarEvent()
                .setTitle("Title")
                .setStartTime(start)
                .setEndTime(end);

        IllegalArgumentException resultToTest = assertThrows(IllegalArgumentException.class, () -> calendarLinkService.generate(calendarEvent));

        assertEquals("Start and end dates are required parameters", resultToTest.getMessage());
    }
}