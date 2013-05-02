package com.collective.recommender.proxy;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SparqlProxyException extends Exception {

    public SparqlProxyException(String message, Exception e) {
        super(message, e);
    }

    public SparqlProxyException(String message) {
        super(message);
    }
}
