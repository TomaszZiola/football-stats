package stats;

import domain.GetStatisticsEvent;
import domain.Outcome;
import domain.ResultEvent;
import domain.TeamStats;
import format.TeamStatsFormatter;

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
                TeamStatsFormatter.formatResult(home, homeStats),
                TeamStatsFormatter.formatResult(away, awayStats)
        );
    }

    public List<String> onGetStatistics(GetStatisticsEvent event) {
        return event.getStatistics().teams().stream()
                .map(team -> TeamStatsFormatter.formatStatistics(team, getTeamStats(team)))
                .toList();
    }

    private TeamStats getTeamStats(String team) {
        return statsByTeam.computeIfAbsent(team, _ -> new TeamStats());
    }
}
