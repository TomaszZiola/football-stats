import org.junit.jupiter.api.Test;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsEngineTest extends BaseTest {

    @Test
    void testOnResult() {
        // given
        var event = new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "Barcelona", 3, 0));

        // when
        var output = engine.onResult(event);

        // then
        assertEquals(2, output.size());
        assertEquals("Bayern 1 3 3 0", output.get(0));
        assertEquals("Barcelona 1 0 0 3", output.get(1));
    }

    @Test
    void testOnResultDraw() {
        // given
        var event = new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "Barcelona", 1, 1));

        // when
        var output = engine.onResult(event);

        // then
        assertEquals(2, output.size());
        assertEquals("Bayern 1 1 1 1", output.get(0));
        assertEquals("Barcelona 1 1 1 1", output.get(1));
    }

    @Test
    void testOnResultTotalStats() {
        // when
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamA", 1, 0)));
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamB", 1, 0)));
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamC", 1, 0)));
        var output = engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamD", 1, 0)));

        // then
        assertEquals("Bayern 4 12 4 0", output.getFirst());
    }

    @Test
    void testOnGetStatistics() {
        // given
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamA", 1, 0)));
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamB", 0, 1)));
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamC", 2, 2)));
        engine.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamD", 3, 1)));
        var event = new GetStatisticsEvent(EventType.GET_STATISTICS, new GetStatisticsEvent.GetStatistics(of("Bayern", "Barcelona")));

        // when
        var output = engine.onGetStatistics(event);

        // then
        assertEquals(2, output.size());
        assertEquals("Bayern WDL 3.0 3 4 5 4", output.get(0));
        assertEquals("Barcelona  0.0 0 0 0 0", output.get(1));
    }
}
