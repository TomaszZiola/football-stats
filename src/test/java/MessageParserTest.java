import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest extends BaseTest {

    @Test
    void testParseResult() throws EventParseException {
        // given
        var json = """
                {"type": "RESULT", "result": {"home_team": "A", "away_team": "B", "home_score": 1, "away_score": 0}}
                """;

        // when
        var event = parser.parseEvent(json);

        // then
        assertInstanceOf(ResultEvent.class, event);
        assertEquals(EventType.RESULT, event.type());
    }

    @Test
    void testParseGetStatistics() throws EventParseException {
        // given
        var json = """
                {"type": "GET_STATISTICS", "get_statistics": {"teams": ["A", "B"]}}
                """;

        // when
        var event = parser.parseEvent(json);

        // then
        assertInstanceOf(GetStatisticsEvent.class, event);
        assertEquals(EventType.GET_STATISTICS, event.type());
    }

    @Test
    void testParseMissingType() {
        // given
        var json = """
                {"result": {}}
                """;

        var exception = assertThrows(EventParseException.class, () -> parser.parseEvent(json));
        assertEquals("MISSING_TYPE", exception.code());
    }

    @Test
    void testParseUnknownType() {
        // given
        var json = """
                {"type": "UNKNOWN"}
                """;

        // when & then
        var exception = assertThrows(EventParseException.class, () -> parser.parseEvent(json));
        assertEquals("UNKNOWN_TYPE", exception.code());
    }

    @Test
    void testParseInvalidJson() {
        // given
        var json = "{invalid}";

        // when & then
        var exception = assertThrows(EventParseException.class, () -> parser.parseEvent(json));
        assertEquals("INVALID_JSON", exception.code());
    }

    @Test
    void testParseNonObject() {
        // given
        var json = "[\"not an object\"]";

        // when & then
        var exception = assertThrows(EventParseException.class, () -> parser.parseEvent(json));
        assertEquals("INVALID_JSON", exception.code());
        assertTrue(exception.getMessage().contains("Input must be a JSON object"));
    }

    @Test
    void testParseNegativeScore() {
        // given
        var json = """
                {"type": "RESULT", "result": {"home_team": "A", "away_team": "B", "home_score": -1, "away_score": 0}}
                """;

        // when & then
        var exception = assertThrows(EventParseException.class, () -> parser.parseEvent(json));
        assertEquals("INVALID_JSON", exception.code());
    }
}
