package integration;

import app.Main;
import org.junit.jupiter.api.Test;
import parser.EventParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    @Test
    void testFullPipeline() throws IOException {
        // given
        var expected = """
                Bayern 1 3 3 0
                Barcelona 1 0 0 3
                PSG 1 1 3 3
                Bayern 2 4 6 3
                Bayern DW 4.5 2 4 6 3
                Bayern 3 4 6 4
                Real 1 3 1 0
                Milan 1 0 1 3
                Bayern 4 7 9 5
                Bayern WLD 3.67 3 4 6 5
                Milan L 4.0 1 0 1 3
                """;
        // when
        var output = runAndCapture("messagesValid.txt");

        // then
        assertEquals(expected.replace("\r\n", "\n"), output.replace("\r\n", "\n"));
    }

    @Test
    void testWithEmptyLines() throws IOException {
        // given
        var expected = """
                Bayern 1 3 3 0
                Barcelona 1 0 0 3
                PSG 1 1 3 3
                Bayern 2 4 6 3
                Bayern DW 4.5 2 4 6 3
                Bayern 3 4 6 4
                Real 1 3 1 0
                Milan 1 0 1 3
                Bayern 4 7 9 5
                Bayern WLD 3.67 3 4 6 5
                Milan L 4.0 1 0 1 3
                """;
        // when
        var output = runAndCapture("messagesWithEmptyLines.txt");

        // then
        assertEquals(expected.replace("\r\n", "\n"), output.replace("\r\n", "\n"));
    }

    @Test
    void testWithInvalidLines() {
        // when
        var exception = assertThrows(EventParseException.class, () -> Main.run("messagesWithInvalidLines.txt"));

        // then
        assertEquals("INVALID_JSON", exception.code());
        assertTrue(exception.getMessage().contains("Invalid input at line 4 [INVALID_JSON]"));
    }

    @Test
    void testWithNullType() {
        // when
        var exception = assertThrows(EventParseException.class, () -> Main.run("messagesWithNullType.txt"));

        // then
        assertEquals("UNKNOWN_TYPE", exception.code());
        assertTrue(exception.getMessage().contains("Invalid input at line 3 [UNKNOWN_TYPE]"));
    }

    @Test
    void testWithInvalidType() {
        // when
        var exception = assertThrows(EventParseException.class, () -> Main.run("messagesWithInvalidType.txt"));

        // then
        assertEquals("UNKNOWN_TYPE", exception.code());
        assertTrue(exception.getMessage().contains("Invalid input at line 2 [UNKNOWN_TYPE]"));
    }

    private String runAndCapture(String resourceName) throws IOException {
        var outputByteStream = new ByteArrayOutputStream();
        var consoleOutput = new PrintStream(outputByteStream, true, UTF_8);
        var outputStream = System.out;
        System.setOut(consoleOutput);

        try {
            Main.run(resourceName);
        } finally {
            System.setOut(outputStream);
        }

        return outputByteStream.toString(UTF_8);
    }
}
