package com.collective.projectprofilingline.knowledge;

import com.collective.model.Project;
import com.collective.model.User;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.projectprofilingline.ProjectDefaultProfilingLineTestCase;
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
public class ProjectManifestoKeywordsProfilingLineItemTestCase {

    private ProfilingLineItem profilingLineItem;

    private ProjectDefaultProfilingLineTestCase.ResultTestRepo repo;

    private static Logger logger = Logger.getLogger(ProjectManifestoKeywordsProfilingLineItemTestCase.class);

    @BeforeTest
    public void setUp() {
        repo = new ProjectDefaultProfilingLineTestCase.ResultTestRepo<Map<String, Object>>();
        profilingLineItem = new ProjectManifestoKeywordsProfilingLineItem("project-extraction-item", "just a test item");
        profilingLineItem.setNextProfilingLineItem(new ProjectDefaultProfilingLineTestCase.LoggingProfilingLineItem(
                "logging-item", "it just logs", logger, repo));
    }

    @AfterTest
    public void tearDown() {
        profilingLineItem = null;
    }

    @Test
    public void testExecute() throws ProfilingLineItemException, URISyntaxException {

        Project project = new Project(3476233L);
        project.setManifestoKeywords(" google , basketball ");

        List<URI> expectedManifestoConceptsUris = new ArrayList<URI>();
        expectedManifestoConceptsUris.add(new URI("http://dbpedia.org/resource/Google"));
        expectedManifestoConceptsUris.add(new URI("http://dbpedia.org/resource/Basketball"));


        profilingLineItem.execute(project);
        logger.debug("logged value: " + repo.getValue());

        Map<String, Object> result = (Map<String, Object>) repo.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.get(ProjectManifestoKeywordsProfilingLineItem.MANIFESTO_CONCEPTS_DBPEDIA_KEY).equals(expectedManifestoConceptsUris));
        List<URI> actualManifestoURIs =
                (List<URI>) result.get(ProjectManifestoKeywordsProfilingLineItem.MANIFESTO_CONCEPTS_DBPEDIA_KEY);
        Assert.assertEqualsNoOrder(actualManifestoURIs.toArray(), expectedManifestoConceptsUris.toArray());
    }


}
