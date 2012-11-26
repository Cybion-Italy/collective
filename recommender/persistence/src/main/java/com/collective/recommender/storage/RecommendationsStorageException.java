package com.collective.recommender.storage;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RecommendationsStorageException extends Exception {

    public RecommendationsStorageException(String message, Exception e) {
        super(message, e);
    }
}
