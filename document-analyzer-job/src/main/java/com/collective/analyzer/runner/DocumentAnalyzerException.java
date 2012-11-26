package com.collective.analyzer.runner;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DocumentAnalyzerException extends Exception {
    public DocumentAnalyzerException(String message, Exception pstoreException) {
        super(message, pstoreException);
    }
}
