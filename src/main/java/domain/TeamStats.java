package domain;

import java.util.ArrayDeque;
import java.util.Deque;

public class TeamStats {

    private record Match(int scored, int conceded, Outcome outcome) {}
    private final Deque<Match> lastMatches = new ArrayDeque<>(3);

    private int totalPlayed;
    private int totalPoints;
    private int totalScored;
    private int totalConceded;

    public void applyMatch(int scored, int conceded, Outcome outcome) {
        totalPlayed++;
        totalScored += scored;
        totalConceded += conceded;
        totalPoints += outcome.points();

        lastMatches.addFirst(new Match(scored, conceded, outcome));
        while (lastMatches.size() > 3) {
            lastMatches.removeLast();
        }
    }

    public int getTotalPlayed() {
        return totalPlayed;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalScored() {
        return totalScored;
    }

    public int getTotalConceded() {
        return totalConceded;
    }

    public int getLast3Played() {
        return lastMatches.size();
    }

    public int getLast3Points() {
        return lastMatches.stream().mapToInt(m -> m.outcome.points()).sum();
    }

    public int getLast3GoalsScored() {
        return lastMatches.stream().mapToInt(m -> m.scored).sum();
    }

    public int getLast3GoalsConceded() {
        return lastMatches.stream().mapToInt(m -> m.conceded).sum();
    }

    public String getRecentForm() {
        var stringBuilder = new StringBuilder(lastMatches.size());
        for (var m : lastMatches) {
            stringBuilder.append(m.outcome.letter());
        }
        return stringBuilder.toString();
    }

    public double getAverageGoals() {
        int playedLast3 = getLast3Played();
        if (playedLast3 == 0) return 0.0;
        return (getLast3GoalsScored() + getLast3GoalsConceded()) / (double) playedLast3;
    }
}