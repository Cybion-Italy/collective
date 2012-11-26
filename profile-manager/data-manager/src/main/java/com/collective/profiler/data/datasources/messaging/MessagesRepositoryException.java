package com.collective.profiler.data.datasources.messaging;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MessagesRepositoryException extends Exception {
    
    public MessagesRepositoryException(String message, Exception e) {
        super(message, e);
    }
}
