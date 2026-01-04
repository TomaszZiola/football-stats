package app;

import parser.MessageParser;
import stats.StatsEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Main {

    static void main() throws IOException {
        run("messages.txt", out::println);
    }

    public static void run(String resourceName, Consumer<String> outputConsumer) throws IOException {
        var parser = new MessageParser();
        var engine = new StatsEngine();

        var processor = new EventProcessor(parser, engine, outputConsumer);

        try (BufferedReader reader = openResource(resourceName)) {
            String currentLine;
            int lineNumber = 0;
            while ((currentLine = reader.readLine()) != null) {
                lineNumber++;
                processor.processLine(currentLine, lineNumber);
            }
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
