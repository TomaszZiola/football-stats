import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StatsEngine {

    private final Map<String, TeamStats> statsByTeam = new HashMap<>();

    public List<String> onResult(ResultEvent event) {
        ResultEvent.Result matchResult = event.result();

        var home = matchResult.homeTeam();
        var away = matchResult.awayTeam();
        var homeScore = matchResult.homeScore();
        var awayScore = matchResult.awayScore();

        var homeStats = getTeamStats(home);
        var awayStats = getTeamStats(away);

        homeStats.applyMatch(homeScore, awayScore, Outcome.forScore(homeScore, awayScore));
        awayStats.applyMatch(awayScore, homeScore, Outcome.forScore(awayScore, homeScore));

        return List.of(
                formatResultLine(home, homeStats),
                formatResultLine(away, awayStats)
        );
    }

    public List<String> onGetStatistics(GetStatisticsEvent event) {
        return event.getStatistics().teams().stream()
                .map(team -> formatStatisticsLine(team, getTeamStats(team)))
                .toList();
    }

    private TeamStats getTeamStats(String team) {
        return statsByTeam.computeIfAbsent(team, _ -> new TeamStats());
    }

    private static String formatResultLine(String team, TeamStats stats) {
        return String.join(" ",
                team,
                String.valueOf(stats.getTotalPlayed()),
                String.valueOf(stats.getTotalPoints()),
                String.valueOf(stats.getTotalScored()),
                String.valueOf(stats.getTotalConceded())
        );
    }

    private static String formatStatisticsLine(String team, TeamStats stats) {
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
