package com.collective.recommender.proxy.filtering;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FilterException extends Exception {

    public FilterException(String message, Exception e) {
        super(message, e);
    }
}
