package utils;

import domain.TeamStats;
import org.junit.jupiter.api.BeforeEach;
import parser.MessageParser;
import stats.StatsEngine;

public class BaseUnit {

    protected MessageParser parser;
    protected StatsEngine engine;
    protected TeamStats stats;

    @BeforeEach
    void setUp(){
        engine = new StatsEngine();
        parser = new MessageParser();
        stats = new TeamStats();
    }
}
