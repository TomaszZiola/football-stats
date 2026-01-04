package utils;

import app.EventProcessor;
import domain.ResultEvent;
import domain.TeamStats;
import models.ResultEventModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import parser.EventParseException;
import parser.MessageParser;
import stats.StatsEngine;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
public class BaseUnit {

    @Mock
    protected MessageParser parser;
    @Mock
    protected StatsEngine engine;


    protected List<String> capturedOutput;
    protected StatsEngine engineImpl;
    protected EventProcessor processorImpl;
    protected MessageParser parserImpl;
    protected TeamStats stats;

    protected ResultEvent resultEvent;

    protected String line = "{ \"type\": \"RESULT\", \"result\": { \"home_team\": \"Bayern\", \"away_team\": " +
            "\"Barcelona\", \"home_score\": 3, \"away_score\": 0 } }";
    protected String invalidline = "invalid json";

    @BeforeEach
    void setUp() {
        capturedOutput = new ArrayList<>();
        engineImpl = new StatsEngine();
        parserImpl = new MessageParser();
        processorImpl = new EventProcessor(parser, engine, capturedOutput::add);
        stats = new TeamStats();

        resultEvent = ResultEventModel.basic();

        when(engine.onResult(resultEvent)).thenReturn(List.of("Bayern 1 3 3 0"));
        when(parser.parseEvent(line)).thenReturn(resultEvent);
        when(parser.parseEvent(invalidline)).thenThrow(new EventParseException("ERR_CODE", "Error message"));
    }
}
