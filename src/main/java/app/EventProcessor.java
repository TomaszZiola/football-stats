package app;

import domain.GetStatisticsEvent;
import domain.ResultEvent;
import parser.EventParseException;
import parser.MessageParser;
import stats.StatsEngine;

import java.util.function.Consumer;

public class EventProcessor {
    private final MessageParser parser;
    private final StatsEngine engine;
    private final Consumer<String> outputConsumer;

    public EventProcessor(MessageParser parser, StatsEngine engine, Consumer<String> outputConsumer) {
        this.parser = parser;
        this.engine = engine;
        this.outputConsumer = outputConsumer;
    }

    void processLine(String line, int lineNumber) {
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

            outputLines.forEach(outputConsumer);
        } catch (EventParseException exception) {
            throw new EventParseException(
                    exception.code(),
                    String.format("Invalid input at line %d [%s]: %s | input=%s",
                            lineNumber, exception.code(), exception.getMessage(), trimmedLine),
                    exception
            );
        } catch (Exception exception) {
            throw new EventParseException(
                    "PROCESS_ERROR",
                    String.format("Error processing line %d: %s | input=%s",
                            lineNumber, exception.getMessage(), trimmedLine),
                    exception
            );
        }
    }
}