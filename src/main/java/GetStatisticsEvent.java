import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetStatisticsEvent(
        EventType type,
        @JsonProperty("get_statistics") GetStatistics getStatistics
) implements Event {
    public record GetStatistics(
            List<String> teams
    ) {
    }
}
