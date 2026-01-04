package domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetStatisticsEvent(
        EventType type,
        @JsonProperty("get_statistics") GetStatistics getStatistics
) implements Event {
    public record GetStatistics(
            List<String> teams
    ) {
        public GetStatistics {
            if (teams == null || teams.isEmpty()) {
                throw new IllegalArgumentException("Teams list cannot be null or empty");
            }
        }
    }
}
