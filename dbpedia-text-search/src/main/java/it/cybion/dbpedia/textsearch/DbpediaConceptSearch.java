package it.cybion.dbpedia.textsearch;

import java.net.URI;
import java.util.List;

public interface DbpediaConceptSearch {
	

	public List<URI> sendRequest(String queryString);
}
