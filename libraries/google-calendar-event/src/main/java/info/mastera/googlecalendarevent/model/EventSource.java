package info.mastera.googlecalendarevent.model;

public enum EventSource {
    WEBSITE("website"),
    NAME("name");

    private final String value;

    EventSource(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
