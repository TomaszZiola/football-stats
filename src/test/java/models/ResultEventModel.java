package models;

import domain.ResultEvent;

import static domain.EventType.RESULT;

public class ResultEventModel {

    public static ResultEvent basic() {
        return new ResultEvent(
                RESULT,
                new ResultEvent.Result("Bayern", "Barcelona", 3, 0)
        );
    }
}
