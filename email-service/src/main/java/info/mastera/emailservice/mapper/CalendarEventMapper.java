package info.mastera.emailservice.mapper;

import info.mastera.googlecalendarevent.model.GoogleCalendarEvent;
import info.mastera.rabbitmq.dto.CalendarEvent;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventMapper {

    public GoogleCalendarEvent convert(CalendarEvent calendarEvent) {
        return new GoogleCalendarEvent()
                .setTitle(calendarEvent.title())
                .setStartTime(calendarEvent.eventDate().atTime(9, 0))
                .setEndTime(calendarEvent.eventDate().atTime(18, 0))
                .setLocation(calendarEvent.location())
                .addGuests(calendarEvent.guests());
    }
}
