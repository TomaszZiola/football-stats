package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Event;
import domain.EventType;
import domain.GetStatisticsEvent;
import domain.ResultEvent;

public final class MessageParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Event parseEvent(String jsonLine) {
        try {
            var root = OBJECT_MAPPER.readTree(jsonLine);
            if (!root.isObject()) {
                throw new EventParseException("INVALID_JSON", "Input must be a JSON object");
            }
            var typeNode = root.get("type");
            if (typeNode == null || typeNode.isNull()) {
                throw new EventParseException("MISSING_TYPE", "Missing 'type' field in message");
            }
            final var eventType = getEventType(typeNode);

            return switch (eventType) {
                case RESULT -> OBJECT_MAPPER.treeToValue(root, ResultEvent.class);
                case GET_STATISTICS -> OBJECT_MAPPER.treeToValue(root, GetStatisticsEvent.class);
            };
        } catch (JsonProcessingException error) {
            var message = error.getCause() != null ? error.getCause().getMessage() : error.getMessage();
            throw new EventParseException("INVALID_JSON", message, error);
        }
    }

    private static EventType getEventType(JsonNode typeNode) {
        final EventType eventType;
        try {
            eventType = EventType.valueOf(typeNode.asText());
        } catch (IllegalArgumentException ex) {
            throw new EventParseException("UNKNOWN_TYPE", "Unknown message type: " + typeNode.asText(), ex);
        }
        return eventType;
    }
}
