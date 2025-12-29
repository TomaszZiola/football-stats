package stats;

import domain.Outcome;
import org.junit.jupiter.api.Test;
import utils.BaseUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamStatsTest extends BaseUnit {

    @Test
    void testForm() {
        stats.applyMatch(1, 0, Outcome.WIN);
        stats.applyMatch(1, 1, Outcome.DRAW);
        assertEquals("DW", stats.getRecentForm());

        stats.applyMatch(0, 1, Outcome.LOSS);
        stats.applyMatch(2, 0, Outcome.WIN);
        assertEquals("WLD", stats.getRecentForm());
    }

    @Test
    void testLast3Aggregates() {
        stats.applyMatch(3, 0, Outcome.WIN);
        stats.applyMatch(1, 1, Outcome.DRAW);
        stats.applyMatch(0, 2, Outcome.LOSS);
        stats.applyMatch(2, 1, Outcome.WIN);

        assertEquals(4, stats.getTotalPlayed());
        assertEquals(7, stats.getTotalPoints());
        assertEquals(6, stats.getTotalScored());
        assertEquals(4, stats.getTotalConceded());


        assertEquals(3, stats.getLast3Played());
        assertEquals(4, stats.getLast3Points());
        assertEquals(3, stats.getLast3GoalsScored());
        assertEquals(4, stats.getLast3GoalsConceded());
        assertEquals((3 + 4) / 3.0, stats.getAverageGoals(), 0.001);
    }
}
