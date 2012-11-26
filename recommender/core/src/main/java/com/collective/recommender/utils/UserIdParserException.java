package com.collective.recommender.utils;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class UserIdParserException extends Exception {

    public UserIdParserException(String message, Exception e) {
        super(message, e);
    }

    public UserIdParserException(String message) {
        super(message);
    }

}
