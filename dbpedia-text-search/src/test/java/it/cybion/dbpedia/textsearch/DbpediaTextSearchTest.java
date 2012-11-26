package it.cybion.dbpedia.textsearch;

import it.cybion.dbpedia.textsearch.exceptions.DbpediaTextSearchInstantiationException;
import it.cybion.dbpedia.textsearch.rest.response.ArrayOfResult;

import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DbpediaTextSearchTest {

	private static Logger logger = Logger.getLogger(DbpediaTextSearchTest.class);

	private DbpediaTextSearch dbpediaTextSearch;

	@BeforeClass
	public void setUp() throws URISyntaxException, DbpediaTextSearchInstantiationException {
		this.dbpediaTextSearch = DbpediaTextSearch.getInstance();
		logger.info("built dbpediaTextSearch");
	}

	@AfterClass
	public void tearDown() {
		this.dbpediaTextSearch = null;
		logger.info("dbpediaTextSearch to null");
	}

	@Test
	public void shouldSendRequestToLookup() {
		String queryString = "galway";
		int maxHits = 3;
		ArrayOfResult arrayOfResults = dbpediaTextSearch.sendRequest(queryString, maxHits);
		Assert.assertTrue(arrayOfResults.getResults().size() <= maxHits);
	}
	
	@Test
	public void shouldParseLocalResponseWithAnnotatedModel() throws JAXBException {
		//read local response example
		InputStream exampleResponseFile = getClass().getClassLoader()
				.getResourceAsStream("response-example.xml");
		
		JAXBContext context;
		context = JAXBContext.newInstance(ArrayOfResult.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller
				.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		ArrayOfResult arrayOfResult = null;
		arrayOfResult = (ArrayOfResult) unmarshaller
				.unmarshal(exampleResponseFile);
			
		Assert.assertNotNull(exampleResponseFile);
		String expectedLabel = "County Galway";
		Assert.assertTrue(arrayOfResult.getResults().get(0).getLabel().equals(expectedLabel));
		Assert.assertEquals(3, arrayOfResult.getResults().size());
		
	}
	
	
	@Test
	public void shouldSendRequestToLookupAndParseEmptyResponse() {
		String queryString = "Cybion srl";
		int maxHits = 3;
		ArrayOfResult arrayOfResults = dbpediaTextSearch.sendRequest(queryString, maxHits);
		Assert.assertTrue(arrayOfResults.getResults().size() <= maxHits);
		Assert.assertTrue(arrayOfResults.getResults().size() == 0);
	}

}
