package it.cybion.dbpedia.textsearch.exceptions;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;

public class DbpediaLookupQueryException extends UniformInterfaceException {
	
	public DbpediaLookupQueryException(String message, ClientResponse r) {
		super(message, r);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
