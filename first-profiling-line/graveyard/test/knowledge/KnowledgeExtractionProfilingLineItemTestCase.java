package com.collective.profilingline.knowledge;

import com.collective.model.User;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.profilingline.DefaultProfilingLineTestCase;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Reference test case for {@link com.collective.profilingline.knowledge.KnowledgeExtractionProfilingLineItem}.
 * 
 * @author Matteo Moci ( moci@cybion.it )
 */
public class KnowledgeExtractionProfilingLineItemTestCase {

    private ProfilingLineItem profilingLineItem;

    private DefaultProfilingLineTestCase.ResultTestRepo repo;

    private static Logger logger = Logger.getLogger(KnowledgeExtractionProfilingLineItemTestCase.class);

    @BeforeTest
    public void setUp() {
        repo = new DefaultProfilingLineTestCase.ResultTestRepo<Map<String, Object>>();
        profilingLineItem = new KnowledgeExtractionProfilingLineItem("extraction-item", "just a test item");
        profilingLineItem.setNextProfilingLineItem(new DefaultProfilingLineTestCase.LoggingProfilingLineItem(
                "logging-item", "it just logs", logger, repo));
    }

    @AfterTest
    public void tearDown() {
        profilingLineItem = null;
    }

    @Test
    public void testExecute() throws ProfilingLineItemException, URISyntaxException {
        User user = new User(new Long(34762334L));
        user.setInterests("google, basketball");
        List<URI> expectedUris = new ArrayList<URI>();
        expectedUris.add(new URI("http://dbpedia.org/resource/Google"));
        expectedUris.add(new URI("http://dbpedia.org/resource/Basketball"));

        profilingLineItem.execute(user);
        logger.debug("logger value: " + repo.getValue());

        Map<String, Object> result = (Map<String, Object>) repo.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.get(KnowledgeExtractionProfilingLineItem.DBPEDIA_URL_KEY).equals(expectedUris));
    }

}
