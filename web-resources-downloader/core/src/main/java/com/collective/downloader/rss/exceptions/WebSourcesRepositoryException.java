package com.collective.downloader.rss.exceptions;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class WebSourcesRepositoryException extends Exception {

    public WebSourcesRepositoryException(String emsg, Exception e) {
        super(emsg, e);
    }

    public WebSourcesRepositoryException(String emsg) {
        super(emsg);
    }
}
