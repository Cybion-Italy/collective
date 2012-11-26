package it.cybion.dbpedia.textsearch.exceptions;

import java.net.URISyntaxException;

public class DbpediaConceptSearchInstantiationException extends Exception {
	
	public DbpediaConceptSearchInstantiationException(String message, URISyntaxException e) {
		super(message, e);
	}

}
