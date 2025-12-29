package parser;

import domain.EventType;
import domain.GetStatisticsEvent;
import domain.ResultEvent;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BaseUnit;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageParserTest extends BaseUnit {

    @ParameterizedTest
    @MethodSource("provideValidEvents")
    void testParseValidEvents(String json, Class<?> expectedClass, EventType expectedType) throws EventParseException {
        // when
        var event = parser.parseEvent(json);

        // then
        assertInstanceOf(expectedClass, event);
        assertEquals(expectedType, event.type());
    }

    static Stream<Arguments> provideValidEvents() {
        return Stream.of(
                Arguments.of("""
                        {"type": "RESULT", "result": {"home_team": "A", "away_team": "B", "home_score": 1, "away_score": 0}}
                        """, ResultEvent.class, EventType.RESULT),
                Arguments.of("""
                        {"type": "GET_STATISTICS", "get_statistics": {"teams": ["A", "B"]}}
                        """, GetStatisticsEvent.class, EventType.GET_STATISTICS)
        );
    }

    @ParameterizedTest
    @MethodSource("provideErrorCases")
    void testParseErrors(String json, String errorJson, String expectedMessagePart) {
        // when & then
        var exception = assertThrows(EventParseException.class, () -> parser.parseEvent(json));
        assertEquals(errorJson, exception.code());
        if (expectedMessagePart != null) {
            assertTrue(exception.getMessage().contains(expectedMessagePart));
        }
    }

    static Stream<Arguments> provideErrorCases() {
        return Stream.of(
                Arguments.of("{\"result\": {}}", "MISSING_TYPE", null),
                Arguments.of("{\"type\": \"UNKNOWN\"}", "UNKNOWN_TYPE", null),
                Arguments.of("{invalid}", "INVALID_JSON", null),
                Arguments.of("[\"not an object\"]", "INVALID_JSON", "Input must be a JSON object"),
                Arguments.of("""
                        {"type": "RESULT", "result": {"home_team": "A", "away_team": "B", "home_score": -1, "away_score": 0}}
                        """, "INVALID_JSON", null)
        );
    }
}
