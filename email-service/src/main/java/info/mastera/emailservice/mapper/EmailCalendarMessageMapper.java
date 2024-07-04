package info.mastera.emailservice.mapper;

import info.mastera.googlecalendarevent.model.GoogleCalendarEvent;
import info.mastera.googlecalendarevent.service.GoogleCalendarLinkService;
import info.mastera.rabbitmq.dto.CalendarEvent;
import info.mastera.rabbitmq.dto.TripReminder;
import org.springframework.stereotype.Service;

@Service
public class EmailCalendarMessageMapper {

    private final GoogleCalendarLinkService googleCalendarLinkService;
    private final CalendarEventMapper calendarEventMapper;


    public EmailCalendarMessageMapper(GoogleCalendarLinkService googleCalendarLinkService,
                                      CalendarEventMapper calendarEventMapper) {
        this.googleCalendarLinkService = googleCalendarLinkService;
        this.calendarEventMapper = calendarEventMapper;
    }

    public String convert(CalendarEvent calendarEvent) {
        GoogleCalendarEvent googleCalendarEvent = calendarEventMapper.convert(calendarEvent);
        String link = googleCalendarLinkService.generate(googleCalendarEvent);
        return "You can follow this link %s to add event to your google calendar".formatted(link);
    }

    public String convert(TripReminder tripReminder) {
        return "Kind reminder about your trip to %s planned for tomorrow".formatted(tripReminder.location());
    }
}
