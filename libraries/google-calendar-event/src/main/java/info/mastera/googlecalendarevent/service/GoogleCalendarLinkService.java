package info.mastera.googlecalendarevent.service;

import info.mastera.googlecalendarevent.model.GoogleCalendarEvent;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GoogleCalendarLinkService {

    private static final String CALENDAR_ROOT_URL = "https://calendar.google.com/calendar/render?";
    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'T'HHmmss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
    private static final String PARAM_DELIMITER = "&";
    private static final String COLON = ":";
    private static final String COMMA = ",";
    private static final String SLASH = "/";

    public String generate(GoogleCalendarEvent event) {
        validate(event);

        StringBuilder linkBuilder = new StringBuilder(CALENDAR_ROOT_URL);
        String params = getParams(event);
        linkBuilder.append(params);
        String eventSources = getEventSources(event);
        if (!eventSources.isEmpty()) {
            linkBuilder
                    .append(params.isEmpty() ? "" : PARAM_DELIMITER)
                    .append(eventSources);
        }

        return linkBuilder.toString();
    }

    private void validate(GoogleCalendarEvent event) {
        if (event.getAction() == null) {
            throw new IllegalArgumentException("Parameter `action` can't be null");
        }
        if (event.getTitle() == null) {
            throw new IllegalArgumentException("Title can't be null");
        }
        if (event.getStartTime() == null || event.getEndTime() == null) {
            throw new IllegalArgumentException("Start and end dates are required parameters");
        }
    }

    private String getEventSources(GoogleCalendarEvent event) {
        return Optional.ofNullable(event.getEventSources())
                .map(sources -> sources.entrySet().stream()
                        .map(entry -> "sprop=" + entry.getKey().getValue() + COLON + entry.getValue())
                        .collect(Collectors.joining(PARAM_DELIMITER)))
                .orElse("");
    }

    private String getParams(GoogleCalendarEvent event) {
        Map<String, String> params = new HashMap<>();
        params.put("action", event.getAction());
        params.put("text", event.getTitle());
        params.put("dates", buildDates(event));
        params.put("ctz", event.getTimezone());
        params.put("details", event.getDescription());
        params.put("location", event.getLocation());
        params.put("crm", event.getAvailability().name());
        params.put("trp", String.valueOf(event.isTransparent()));
        params.put("add", buildGuests(event.getGuests()));
        params.put("src", event.getAlternativeEmail());
        params.put("recur", event.getRecurrence());
        return params.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    private String buildGuests(Collection<String> guests) {
        return Optional.ofNullable(guests)
                .map(g -> String.join(COMMA, g))
                .orElse(null);
    }

    private String buildDates(GoogleCalendarEvent event) {
        return DATE_TIME_FORMATTER.format(event.getStartTime())
                + SLASH
                + DATE_TIME_FORMATTER.format(event.getEndTime());
    }
}
