package it.cybion.dbpedia.textsearch;

import java.net.URI;
import java.net.URISyntaxException;

import it.cybion.dbpedia.textsearch.exceptions.DbpediaTextSearchInstantiationException;
import it.cybion.dbpedia.textsearch.rest.response.ArrayOfResult;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class DbpediaTextSearch {

	private static DbpediaTextSearch instance;
	private static Logger logger = Logger.getLogger(DbpediaTextSearch.class);
	private URI keywordSearchURI;

	private DbpediaTextSearch() throws DbpediaTextSearchInstantiationException {
		String urlKeywordSearchQuery = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch";

		try {
			this.keywordSearchURI = new URI(urlKeywordSearchQuery);
		} catch (URISyntaxException e) {
			logger.error("uri syntax error" + urlKeywordSearchQuery);
			throw new DbpediaTextSearchInstantiationException("uri syntax error: ", e);
		}
	}

	public static DbpediaTextSearch getInstance() throws DbpediaTextSearchInstantiationException {
		if (instance == null) {
			instance = new DbpediaTextSearch();
		}
		return instance;
	}

	public ArrayOfResult sendRequest(String queryString, int maxHits) {
		logger.info("sending dbpedia lookup request");
		Client client = Client.create();

		WebResource webResource = client.resource(keywordSearchURI);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("MaxHits", Integer.toString(maxHits));
		queryParams.add("QueryString", queryString);

		logger.info("request uri "
				+ webResource.queryParams(queryParams).toString());

		//TODO: no exceptions thrown? strange... 
		ArrayOfResult arrayOfResult = webResource.queryParams(queryParams)
					.accept(MediaType.APPLICATION_XML).get(ArrayOfResult.class);
			
		return arrayOfResult;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
