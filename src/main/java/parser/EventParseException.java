package parser;

public final class EventParseException extends RuntimeException {
    private final String code;

    public EventParseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public EventParseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String code() {
        return code;
    }
}
