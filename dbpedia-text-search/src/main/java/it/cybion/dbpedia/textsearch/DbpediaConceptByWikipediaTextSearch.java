package it.cybion.dbpedia.textsearch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import it.cybion.dbpedia.textsearch.exceptions.DbpediaConceptSearchInstantiationException;
import it.cybion.dbpedia.textsearch.rest.wikipediaResponse.WikiResult;
import it.cybion.dbpedia.textsearch.rest.wikipediaResponse.WikipediaSearchResult;

public class DbpediaConceptByWikipediaTextSearch implements
		DbpediaConceptSearch {

	private static DbpediaConceptByWikipediaTextSearch instance;
	private static Logger logger = Logger
			.getLogger(DbpediaConceptByWikipediaTextSearch.class);
	private URI keywordSearchURI;

	private DbpediaConceptByWikipediaTextSearch()
			throws DbpediaConceptSearchInstantiationException {
		String urlKeywordSearchQuery = "http://en.wikipedia.org/w/api.php?";

		try {
			this.keywordSearchURI = new URI(urlKeywordSearchQuery);
		} catch (URISyntaxException e) {
			logger.error("uri syntax error" + urlKeywordSearchQuery);
			throw new DbpediaConceptSearchInstantiationException(
					"uri syntax error: ", e);
		}
	}

	public static DbpediaConceptByWikipediaTextSearch getInstance()
			throws DbpediaConceptSearchInstantiationException {
		if (instance == null) {
			instance = new DbpediaConceptByWikipediaTextSearch();
		}
		return instance;
	}

	public List<URI> sendRequest(String queryString) {
		List<URI> uris = new LinkedList<URI>();
		logger.info("sending wikipedia api request");
		Client client = Client.create();

		WebResource webResource = client.resource(keywordSearchURI);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("list", "search");
		queryParams.add("action", "query");
		queryParams.add("format", "xml");
		
		queryParams.add("srsearch", queryString);
		logger.info("request uri "
				+ webResource.queryParams(queryParams).toString());

		//TODO: no exceptions thrown? strange... 
		
		
		WikipediaSearchResult searchResult= webResource.queryParams(queryParams).accept(MediaType.APPLICATION_XML).get(WikipediaSearchResult.class);
		logger.info("searchResult: " + searchResult);
		//System.out.println(searchResult);
			List<WikiResult> results = searchResult.getQuery().getSearch().getPResults();
			logger.info("results: " + (results));
			if (results!=null){
				for (WikiResult wikiResult : results) {
					try {
					String conceptURI = wikiResult.getTitle().replace(' ', '_');
					uris.add(new URI("http://dbpedia.org/resource/" + conceptURI ));
					String s = new String();
					
					} catch (URISyntaxException e){
						logger.error("URI bad definition");
					} catch (NullPointerException e) {
						logger.error("No title for results");
					}
					
				}
			}
		
		
		//logger.info("results: " + (searchResult));
		logger.info("URIs: " + uris);
		return uris;
	}
}
