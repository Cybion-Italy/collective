package com.collective.activity.storage;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ActivityStoreException extends Exception {
    
    public ActivityStoreException(String emsg, Exception e) {
        super(emsg, e);
    }
}
