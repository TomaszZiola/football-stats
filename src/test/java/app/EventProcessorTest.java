package app;

import org.junit.jupiter.api.Test;
import parser.EventParseException;
import utils.BaseUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventProcessorTest extends BaseUnit {

    @Test
    void shouldRouteResultEventToEngine() {
        // when
        processorImpl.processLine(line, 1);

        // then
        assertEquals(1, capturedOutput.size());
        assertEquals("Bayern 1 3 3 0", capturedOutput.getFirst());
        verify(engine).onResult(resultEvent);
    }

    @Test
    void shouldWrapParserExceptionWithLineInfo() {
        // when
        EventParseException exception = assertThrows(EventParseException.class,
                () -> processorImpl.processLine(invalidline, 5));

        // then
        assertEquals("ERR_CODE", exception.code());
        assertEquals("Invalid input at line 5 [ERR_CODE]: Error message | input=invalid json", exception.getMessage());
    }

    @Test
    void shouldIgnoreEmptyLines() {
        // when
        processorImpl.processLine("   ", 1);

        // then
        verify(parser, never()).parseEvent(any());
    }

    @Test
    void shouldHandleGenericException() {
        // given
        when(parser.parseEvent(any())).thenThrow(new RuntimeException("Boom"));

        // when
        EventParseException exception = assertThrows(EventParseException.class,
                () -> processorImpl.processLine(line, 1));

        // then
        assertEquals("PROCESS_ERROR", exception.code());
        assertTrue(exception.getMessage().contains("Error processing line 1: Boom"));
    }
}
