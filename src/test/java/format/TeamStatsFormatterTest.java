package format;

import domain.Outcome;
import domain.TeamStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamStatsFormatterTest {

    @Test
    void testFormatResult() {
        // given
        var stats = new TeamStats();
        stats.applyMatch(2, 1, Outcome.WIN);

        // when
        var result = TeamStatsFormatter.formatResult("Bayern", stats);

        // then
        assertEquals("Bayern 1 3 2 1", result);
    }

    @Test
    void testFormatResultEmpty() {
        // given
        var stats = new TeamStats();

        // when
        var result = TeamStatsFormatter.formatResult("Milan", stats);

        // then
        assertEquals("Milan 0 0 0 0", result);
    }

    @Test
    void testFormatStatistics() {
        // given
        var stats = new TeamStats();
        stats.applyMatch(2, 1, Outcome.WIN);
        stats.applyMatch(1, 1, Outcome.DRAW);
        stats.applyMatch(0, 3, Outcome.LOSS);
        stats.applyMatch(3, 2, Outcome.WIN);

        // when
        var result = TeamStatsFormatter.formatStatistics("Bayern", stats);

        // then
        assertEquals("Bayern WLD 3.33 3 4 4 6", result);
    }

    @Test
    void testFormatStatisticsEmpty() {
        // given
        var stats = new TeamStats();

        // when
        var result = TeamStatsFormatter.formatStatistics("Milan", stats);

        // then
        assertEquals("Milan  0.0 0 0 0 0", result);
    }
}
