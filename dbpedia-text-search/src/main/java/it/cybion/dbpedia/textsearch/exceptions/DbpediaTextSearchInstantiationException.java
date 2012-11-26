package it.cybion.dbpedia.textsearch.exceptions;

import java.net.URISyntaxException;

public class DbpediaTextSearchInstantiationException extends Exception {

	public DbpediaTextSearchInstantiationException(String message, URISyntaxException e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1892243052962267821L;

}
