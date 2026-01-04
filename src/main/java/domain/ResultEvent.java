package domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResultEvent(
        EventType type,
        Result result
) implements Event {
    public record Result(
            @JsonProperty("home_team") String homeTeam,
            @JsonProperty("away_team") String awayTeam,
            @JsonProperty("home_score") int homeScore,
            @JsonProperty("away_score") int awayScore
    ) {
        public Result {
            if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) {
                throw new IllegalArgumentException("Team names cannot be null or empty");
            }
            if (homeScore < 0 || awayScore < 0) {
                throw new IllegalArgumentException("Scores cannot be negative");
            }
        }
    }
}
