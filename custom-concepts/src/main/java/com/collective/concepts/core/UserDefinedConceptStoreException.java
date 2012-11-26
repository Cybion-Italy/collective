package com.collective.concepts.core;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserDefinedConceptStoreException extends Exception {

    public UserDefinedConceptStoreException(String message, Exception e) {
        super(message, e);
    }

    public UserDefinedConceptStoreException(String message) {
        super(message);
    }
}
