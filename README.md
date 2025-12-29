# Football Stats Engine

A Java 25 application that processes football match results and calculates team performance metrics from a JSON event stream.

## Quick Start

- **Run**: `./gradlew run`
- **Test**: `./gradlew test`

*Prerequisite: Java 25 (uses preview features).*

## Event Formats

Events are read line-by-line from `src/main/resources/messages.txt`.

### Match Results
**Input:** `{"type": "RESULT", "result": {"home_team": "Bayern", "away_team": "Milan", "home_score": 2, "away_score": 1}}`  
**Output:** `Bayern 1 3 2 1`  
*Format: `[Team] [Played] [Points] [Scored] [Conceded]`*

### Team Statistics
Returns detailed data for the last 3 matches.  
**Input:** `{"type": "GET_STATISTICS", "get_statistics": {"teams": ["Bayern"]}}`  
**Output:** `Bayern W 3.0 1 3 2 1`  
*Format: `[Team] [Form] [AvgGoals] [L3Played] [L3Points] [L3Scored] [L3Conceded]`*
