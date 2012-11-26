package com.collective.analyzer.storage;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class WebResourceRepositoryException extends Exception {

    public WebResourceRepositoryException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public WebResourceRepositoryException(String message) {
        super(message);
    }
}
