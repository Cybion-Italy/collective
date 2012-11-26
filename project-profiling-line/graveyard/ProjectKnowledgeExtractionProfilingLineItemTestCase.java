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
 * Reference test case for {@link ProjectKnowledgeExtractionProfilingLineItem}.
 * 
 * @author Matteo Moci ( moci@cybion.it )
 */
public class ProjectKnowledgeExtractionProfilingLineItemTestCase {

    private ProfilingLineItem profilingLineItem;

    private ProjectDefaultProfilingLineTestCase.ResultTestRepo repo;

    private static Logger logger = Logger.getLogger(ProjectKnowledgeExtractionProfilingLineItemTestCase.class);

    @BeforeTest
    public void setUp() {
        repo = new ProjectDefaultProfilingLineTestCase.ResultTestRepo<Map<String, Object>>();
        profilingLineItem = new ProjectKnowledgeExtractionProfilingLineItem("project-extraction-item", "just a test item");
        profilingLineItem.setNextProfilingLineItem(new ProjectDefaultProfilingLineTestCase.LoggingProfilingLineItem(
                "logging-item", "it just logs", logger, repo));
    }

    @AfterTest
    public void tearDown() {
        profilingLineItem = null;
    }

    @Test
    public void testExecute() throws ProfilingLineItemException, URISyntaxException {
        Project project = new Project(326542634L);
        project.setManifesto("google, basketball");

        List<URI> expectedUris = new ArrayList<URI>();
        expectedUris.add(new URI("http://dbpedia.org/resource/Google"));
        expectedUris.add(new URI("http://dbpedia.org/resource/Basketball"));

        profilingLineItem.execute(project);
        logger.debug("logger value: " + repo.getValue());

        Map<String, Object> result = (Map<String, Object>) repo.getValue();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.get(ProjectKnowledgeExtractionProfilingLineItem.PROJECT_DBPEDIA_URL_KEY).equals(expectedUris));
    }

}
