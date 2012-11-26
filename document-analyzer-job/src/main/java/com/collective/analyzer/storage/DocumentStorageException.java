package com.collective.analyzer.storage;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DocumentStorageException extends Exception {
    public DocumentStorageException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public DocumentStorageException(String message) {
        super(message);
    }
}
