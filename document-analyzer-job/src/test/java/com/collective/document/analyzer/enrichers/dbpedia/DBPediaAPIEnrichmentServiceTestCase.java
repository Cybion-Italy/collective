package com.collective.document.analyzer.enrichers.dbpedia;

import com.collective.analyzer.enrichers.EnrichmentService;
import com.collective.analyzer.enrichers.EnrichmentServiceException;
import com.collective.analyzer.enrichers.dbpedia.DBPediaAPI;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;

import static org.testng.Assert.assertTrue;


/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class DBPediaAPIEnrichmentServiceTestCase
{

    private static final Logger logger = Logger.getLogger(DBPediaAPIEnrichmentServiceTestCase.class);

    EnrichmentService es;

    @BeforeClass
    public void setUp() {
        this.es = new DBPediaAPI();
    }

    @AfterClass
    public void tearDown() {
        this.es = null;
    }

    //TODO refactor using EnrichmentService APIs
    @Test (enabled = false)
    public void shouldAskDbpediaConcepts() throws EnrichmentServiceException
    {
        DBPediaAPI dbPediaAPI = new DBPediaAPI();

        List<URI> dBpediaAPIResponse =
                dbPediaAPI.getConceptsURIs(
                        "From nude pictures of celebrities " +
                                "to politicians caught in compromising positions, " +
                                "verifying the authenticity of images online is often " +
                                "no easy task. To address this problem, a team at Duke " +
                                "University looking has developed software called " +
                                "YouProve that can be integrated into the Android " +
                                "operating system to track changes made to images or " +
                                "audio captured on an Android smartphone. " +
                                "The software then produces a non-forgeable \"fidelity " +
                                "certificate\" that uses a \"heat-map\" to summarize " +
                                "the degree to which various regions of the media have " +
                                "been modified compared to the original image. .. " +
                                "Continue Reading YouProve software verifies the " +
                                "authenticity of online images and audio Section: " +
                                "Research WatchTags: Android, Audio, Duke University, " +
                                "Imaging Related Articles: ZiiLABS targets Android tablet performance with two new mobile processors EVO 2 gaming console to bring Android games to TV SX Pro automatically converts stereo into 5.1 Surround Sound Android apps pass 50,000 mark Reversible watermarking could thwart digital photo tricksters Logitech launches tablet mouse for Android 3.1");

        Assert.assertNotNull(dBpediaAPIResponse);
        Assert.assertEquals(dBpediaAPIResponse.size(), 16);

        for (URI dBpediaResource : dBpediaAPIResponse) {
            logger.info("found uri: " + dBpediaResource.toString());
        }
    }

    @Test (enabled = false)
    public void shouldExtractFromString()
            throws EnrichmentServiceException
    {
        String textToAnalyse = "President Obama called Wednesday on Congress " +
                "to extend a tax break for students included in last year's "  +
                "economic stimulus package, arguing that the policy provides " +
                "more generous assistance";

        List<URI> concepts = this.es.getConceptsURIs(textToAnalyse);

        for (URI concept: concepts) {
            logger.debug("found concept: " + concept.toString());
        }

        //TODO the asserts are not fulfilled: why the concepts size always change? investigate

        assertTrue(concepts.size() > 0);
    }
}
