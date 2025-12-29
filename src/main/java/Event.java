public sealed interface Event permits ResultEvent, GetStatisticsEvent {
    EventType type();
}
