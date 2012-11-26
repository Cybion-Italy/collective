package com.collective.profilingline.skos;

import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.profiler.line.model.Interest;
import com.collective.profilingline.DefaultProfilingLineTestCase;
import com.collective.profilingline.knowledge.KnowledgeExtractionProfilingLineItem;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference test case for {@link com.collective.profilingline.skos.SkosLinkerProfilingLineItem}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosLinkerProfilingLineItemTestCase {

    private final static Logger logger = Logger.getLogger(SkosLinkerProfilingLineItemTestCase.class);

    private ProfilingLineItem profilingLineItem;

    private DefaultProfilingLineTestCase.ResultTestRepo repo;    

    @BeforeTest
    public void setUp() {
        profilingLineItem = new SkosLinkerProfilingLineItem("skos-item", "just a test item");
        repo = new DefaultProfilingLineTestCase.ResultTestRepo<Map<String, Object>>();
        profilingLineItem.setNextProfilingLineItem(new DefaultProfilingLineTestCase.LoggingProfilingLineItem(
                "logging-item", "it just logs", logger, repo));
    }

    @AfterTest
    public void tearDown() {
        profilingLineItem = null;
    }

    @Test
    public void testExecute() throws URISyntaxException, ProfilingLineItemException {
        Map<String, Object> input = new HashMap<String, Object>();
        List<URI> uris = new ArrayList<URI>();
        uris.add(new URI("http://dbpedia.org/resource/Foursome_of_Nine_Dragon_Island"));
        uris.add(new URI("http://dbpedia.org/resource/Welkin_Lords"));
        uris.add(new URI("http://dbpedia.org/resource/Solaria"));
        input.put(KnowledgeExtractionProfilingLineItem.DBPEDIA_URL_KEY,  uris);

        profilingLineItem.execute(input);
        Map<String, Object> result = (Map<String, Object>) repo.getValue();
        Assert.assertNotNull(result);
        List<Interest> interests = (List<Interest>) result.get(SkosLinkerProfilingLineItem.SKOS_URIS_KEY);
        Assert.assertTrue(interests.size() > 0);
        logger.info("Interests: '" + interests + "'");
    }

}
