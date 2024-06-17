package info.mastera.googlecalendarevent.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.StringJoiner;

@SuppressWarnings("unused")
public class GoogleCalendarEvent {
    private String title;
    private String action = "TEMPLATE";
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String timezone;
    private String description;
    private String location;
    private Availability availability = Availability.AVAILABLE;
    private boolean isTransparent = true;
    private Map<EventSource, String> eventSources = new EnumMap<>(EventSource.class);
    private final Collection<String> guests = new ArrayList<>();
    private String alternativeEmail;
    private String recurrence;

    public String getTitle() {
        return title;
    }

    public GoogleCalendarEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAction() {
        return action;
    }

    public GoogleCalendarEvent setAction(String action) {
        this.action = action;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public GoogleCalendarEvent setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public GoogleCalendarEvent setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getTimezone() {
        return timezone;
    }

    public GoogleCalendarEvent setTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GoogleCalendarEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public GoogleCalendarEvent setLocation(String location) {
        this.location = location;
        return this;
    }

    public Availability getAvailability() {
        return availability;
    }

    public GoogleCalendarEvent setAvailability(Availability availability) {
        this.availability = availability;
        return this;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public GoogleCalendarEvent setTransparent(boolean transparent) {
        isTransparent = transparent;
        return this;
    }

    public Map<EventSource, String> getEventSources() {
        return eventSources;
    }

    public GoogleCalendarEvent setEventSources(Map<EventSource, String> eventSources) {
        this.eventSources = eventSources;
        return this;
    }

    public GoogleCalendarEvent addEventSource(EventSource event, String source) {
        this.eventSources.put(event, source);
        return this;
    }

    public Collection<String> getGuests() {
        return guests;
    }

    public GoogleCalendarEvent addGuests(Collection<String> guests) {
        if(guests!=null) {
            this.guests.addAll(guests);
        }
        return this;
    }

    public GoogleCalendarEvent addGuest(String guest) {
        this.guests.add(guest);
        return this;
    }

    public String getAlternativeEmail() {
        return alternativeEmail;
    }

    public GoogleCalendarEvent setAlternativeEmail(String alternativeEmail) {
        this.alternativeEmail = alternativeEmail;
        return this;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public GoogleCalendarEvent setRecurrence(String recurrence) {
        this.recurrence = recurrence;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GoogleCalendarEvent.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("action='" + action + "'")
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .add("timezone='" + timezone + "'")
                .add("description='" + description + "'")
                .add("location='" + location + "'")
                .add("availability=" + availability)
                .add("isTransparent=" + isTransparent)
                .add("eventSources=" + eventSources)
                .add("guests=" + guests)
                .add("alternativeEmail='" + alternativeEmail + "'")
                .add("recurrence='" + recurrence + "'")
                .toString();
    }
}
