package it.cybion.dbpedia.textsearch;

import java.util.List;

import java.net.URISyntaxException;
import java.net.URI;
import org.apache.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DbpediaConceptByWikipediaTextSearchTest {

	private static Logger logger = Logger.getLogger(DbpediaConceptByWikipediaTextSearchTest.class);

	private DbpediaConceptByWikipediaTextSearch dbpediaSearch;

    @BeforeClass
	public void setUp() throws Exception {
		this.dbpediaSearch = DbpediaConceptByWikipediaTextSearch.getInstance();
		logger.info("built dbpediaTextSearch");
	}

	@AfterClass
	public void tearDown() throws Exception {
	}

	@Test
	public void testSendRequest() {
		String queryString = "galway";
		List<URI> wikiResults = dbpediaSearch.sendRequest(queryString);
		Assert.assertTrue(wikiResults.size() <= 10);
		try{
            Assert.assertNotNull(wikiResults.get(0));
            Assert.assertEquals(wikiResults.get(0), new URI("http://dbpedia.org/resource/Galway"));
//            Assert.assertEquals(wikiResults.get(7), new URI("http://dbpedia.org/resource/Galway_(Dáil_Éireann_constituency)"));
		} catch(URISyntaxException e){
			logger.error("test error ");
			Assert.fail("fail");
            e.printStackTrace();
		}
	}

	@Test
	public void testSendRequestNoResponse(){
		String queryString = "xsa cdfoih iou hn cdes";
		List<URI> wikiResults = dbpediaSearch.sendRequest(queryString);
		Assert.assertTrue(wikiResults.size() == 0);
	}
}
