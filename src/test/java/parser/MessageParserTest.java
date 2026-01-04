package parser;

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
    void shouldParseValidEvents(String json, Class<?> expectedClass) throws EventParseException {
        // when
        var event = parserImpl.parseEvent(json);

        // then
        assertInstanceOf(expectedClass, event);
    }

    static Stream<Arguments> provideValidEvents() {
        return Stream.of(
                Arguments.of("""
                        {"type": "RESULT", "result": {"home_team": "A", "away_team": "B", "home_score": 1, "away_score": 0}}
                        """, ResultEvent.class),
                Arguments.of("""
                        {"type": "GET_STATISTICS", "get_statistics": {"teams": ["A", "B"]}}
                        """, GetStatisticsEvent.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provideErrorCases")
    void shouldHandleParseErrors(String json, String errorJson, String expectedMessagePart) {
        // when & then
        var exception = assertThrows(EventParseException.class, () -> parserImpl.parseEvent(json));
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
                        """, "INVALID_JSON", "Scores cannot be negative"),
                Arguments.of("""
                        {"type": "RESULT", "result": {"home_team": "", "away_team": "B", "home_score": 1, "away_score": 0}}
                        """, "INVALID_JSON", "Team names cannot be null or empty"),
                Arguments.of("""
                        {"type": "RESULT", "result": {"home_team": "A", "away_team": null, "home_score": 1, "away_score": 0}}
                        """, "INVALID_JSON", "Team names cannot be null or empty"),
                Arguments.of("""
                        {"type": "GET_STATISTICS", "get_statistics": {"teams": []}}
                        """, "INVALID_JSON", "Teams list cannot be null or empty")
        );
    }
}
