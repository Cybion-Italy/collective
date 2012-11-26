package com.collective.recommender;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RecommenderException extends Exception {

    public RecommenderException(String message) {
        super(message);
    }

    public RecommenderException(String message, Exception e) {
        super(message, e);
    }
}
