import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class MessageParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Event parseEvent(String jsonLine) throws EventParseException {
        try {
            var root = objectMapper.readTree(jsonLine);
            if (!root.isObject()) {
                throw new EventParseException("INVALID_JSON", "Input must be a JSON object");
            }
            var typeNode = root.get("type");
            if (typeNode == null || typeNode.isNull()) {
                throw new EventParseException("MISSING_TYPE", "Missing 'type' field in message");
            }
            final EventType eventType;

            eventType = getEventType(typeNode);

            return switch (eventType) {
                case RESULT -> objectMapper.treeToValue(root, ResultEvent.class);
                case GET_STATISTICS -> objectMapper.treeToValue(root, GetStatisticsEvent.class);
            };
        } catch (JsonProcessingException error) {
            throw new EventParseException("INVALID_JSON", "Invalid JSON", error);
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
