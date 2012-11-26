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
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class KeywordsProfilingLineItemTestCase {

    private ProfilingLineItem profilingLineItem;

    private DefaultProfilingLineTestCase.ResultTestRepo repo;

    private static Logger logger = Logger.getLogger(KeywordsProfilingLineItemTestCase.class);

    @BeforeTest
    public void setUp() {
        //TODO: should be isolated from the network and from the alchemy APIs calls
        repo = new DefaultProfilingLineTestCase.ResultTestRepo<Map<String, Object>>();
        profilingLineItem = new KeywordsProfilingLineItem("skills-extraction-item", "just a test item");
        profilingLineItem.setNextProfilingLineItem(new DefaultProfilingLineTestCase.LoggingProfilingLineItem(
                "logging-item", "it just logs", logger, repo));
    }

    @AfterTest
    public void tearDown() {
        profilingLineItem = null;
    }

    @Test
    public void testExecute() throws ProfilingLineItemException, URISyntaxException {
        User user = new User(new Long(4387367L));

        user.setSkillsKeywords(" google , basketball ");
        List<URI> expectedSkillsUris = new ArrayList<URI>();
        expectedSkillsUris.add(new URI("http://dbpedia.org/resource/Google"));
        expectedSkillsUris.add(new URI("http://dbpedia.org/resource/Basketball"));

        user.setInterestsKeywords(" freebase , dublin ");
        List<URI> expectedInterestsUris = new ArrayList<URI>();
        //old, non disambiguated uri with old dbpedia text search
        expectedInterestsUris.add(new URI("http://dbpedia.org/resource/Freebase"));
        expectedInterestsUris.add(new URI("http://dbpedia.org/resource/Dublin"));

        profilingLineItem.execute(user);
        logger.debug("logged value: " + repo.getValue());

        Map<String, Object> result = (Map<String, Object>) repo.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.get(KeywordsProfilingLineItem.SKILLS_DBPEDIA_KEY).equals(expectedSkillsUris));
        List<URI> actualInterests =
                (List<URI>) result.get(KeywordsProfilingLineItem.INTERESTS_DBPEDIA_KEY);
        Assert.assertEqualsNoOrder(actualInterests.toArray(), expectedInterestsUris.toArray());
    }

}
