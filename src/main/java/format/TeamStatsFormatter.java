package format;

import domain.TeamStats;

public final class TeamStatsFormatter {

    private TeamStatsFormatter() {}

    public static String formatResult(String team, TeamStats stats) {
        return String.join(" ",
                team,
                String.valueOf(stats.getTotalPlayed()),
                String.valueOf(stats.getTotalPoints()),
                String.valueOf(stats.getTotalScored()),
                String.valueOf(stats.getTotalConceded())
        );
    }

    public static String formatStatistics(String team, TeamStats stats) {
        return String.join(" ",
                team,
                stats.getRecentForm(),
                AverageFormatter.format(stats.getAverageGoals()),
                String.valueOf(stats.getLast3Played()),
                String.valueOf(stats.getLast3Points()),
                String.valueOf(stats.getLast3GoalsScored()),
                String.valueOf(stats.getLast3GoalsConceded())
        );
    }
}
