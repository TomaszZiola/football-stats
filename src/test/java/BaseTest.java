import org.junit.jupiter.api.BeforeEach;

class BaseTest {

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
