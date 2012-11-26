package com.collective.downloader.rss.exceptions;

public class WebResourceExtractorException extends Exception {
	
	private static final long serialVersionUID = -3835068319580102263L;

    public WebResourceExtractorException(String message, Exception e){
		super(message, e);
	}

}