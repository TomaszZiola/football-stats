package domain;

public sealed interface Event permits ResultEvent, GetStatisticsEvent {
}
