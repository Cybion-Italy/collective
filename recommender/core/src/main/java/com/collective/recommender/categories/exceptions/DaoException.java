package com.collective.recommender.categories.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DaoException extends Exception {

    public DaoException(String emsg, Exception e) {
        super(emsg, e);
    }
}
