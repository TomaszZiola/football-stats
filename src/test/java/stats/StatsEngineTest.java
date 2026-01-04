package stats;

import domain.EventType;
import domain.GetStatisticsEvent;
import domain.ResultEvent;
import org.junit.jupiter.api.Test;
import utils.BaseUnit;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsEngineTest extends BaseUnit {

    @Test
    void shouldHandleResultEvent() {
        // given
        var event = new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "Barcelona", 3, 0));

        // when
        var output = engineImpl.onResult(event);

        // then
        assertEquals(2, output.size());
        assertEquals("Bayern 1 3 3 0", output.get(0));
        assertEquals("Barcelona 1 0 0 3", output.get(1));
    }

    @Test
    void shouldHandleDrawResultEvent() {
        // given
        var event = new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "Barcelona", 1, 1));

        // when
        var output = engineImpl.onResult(event);

        // then
        assertEquals(2, output.size());
        assertEquals("Bayern 1 1 1 1", output.get(0));
        assertEquals("Barcelona 1 1 1 1", output.get(1));
    }

    @Test
    void shouldTrackTotalStatsAcrossMultipleResults() {
        // when
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamA", 1, 0)));
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamB", 1, 0)));
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamC", 1, 0)));
        var output = engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamD", 1, 0)));

        // then
        assertEquals("Bayern 4 12 4 0", output.getFirst());
    }

    @Test
    void shouldHandleGetStatisticsEvent() {
        // given
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamA", 1, 0)));
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamB", 0, 1)));
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamC", 2, 2)));
        engineImpl.onResult(new ResultEvent(EventType.RESULT, new ResultEvent.Result("Bayern", "TeamD", 3, 1)));
        var event = new GetStatisticsEvent(EventType.GET_STATISTICS, new GetStatisticsEvent.GetStatistics(of("Bayern", "Barcelona")));

        // when
        var output = engineImpl.onGetStatistics(event);

        // then
        assertEquals(2, output.size());
        assertEquals("Bayern WDL 3.0 3 4 5 4", output.get(0));
        assertEquals("Barcelona  0.0 0 0 0 0", output.get(1));
    }
}
