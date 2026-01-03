package app;

import domain.GetStatisticsEvent;
import domain.ResultEvent;
import parser.EventParseException;
import parser.MessageParser;
import stats.StatsEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Main {

    static void main() throws IOException {
        run("messages.txt");
    }

    public static void run(String resourceName) throws IOException {
        var parser = new MessageParser();
        var engine = new StatsEngine();

        try (BufferedReader reader = openResource(resourceName)) {
            String currentLine;
            int lineNumber = 0;
            while ((currentLine = reader.readLine()) != null) {
                lineNumber++;
                processLine(currentLine, lineNumber, parser, engine);
            }
        }
    }

    private static void processLine(String line, int lineNumber, MessageParser parser, StatsEngine engine) {
        var trimmedLine = line.trim();
        if (trimmedLine.isEmpty()) {
            return;
        }

        try {
            var event = parser.parseEvent(trimmedLine);

            var outputLines = switch (event) {
                case ResultEvent resultEvent -> engine.onResult(resultEvent);
                case GetStatisticsEvent getStatisticsEvent -> engine.onGetStatistics(getStatisticsEvent);
            };

            outputLines.forEach(out::println);
        } catch (EventParseException exception) {
            throw new EventParseException(
                    exception.code(),
                    String.format("Invalid input at line %d [%s]: %s | input=%s",
                            lineNumber, exception.code(), exception.getMessage(), trimmedLine),
                    exception
            );
        }
    }

    private static BufferedReader openResource(String resourceName) {
        var inputStream = Main.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
        return new BufferedReader(new InputStreamReader(inputStream, UTF_8));
    }
}
