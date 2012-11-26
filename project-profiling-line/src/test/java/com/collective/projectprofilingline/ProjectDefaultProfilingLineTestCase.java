package com.collective.projectprofilingline;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.collective.model.Project;
import com.collective.model.profile.ProjectProfile;
import com.collective.projectprofilingline.builder.ProjectProfileBuilderLineItem;
import com.collective.projectprofilingline.knowledge.ProjectManifestoKeywordsProfilingLineItem;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.collective.profiler.line.DefaultProfilingLine;
import com.collective.profiler.line.ProfilingInput;
import com.collective.profiler.line.ProfilingLineException;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;

/**
 * This class act as an integration test for the whole {@link com.collective.profiler.line.DefaultProfilingLine}
 * composed with {@link com.collective.projectprofilingline.knowledge.ProjectManifestoKeywordsProfilingLineItem} items.
 *
 * @see {@link com.collective.projectprofilingline.knowledge.ProjectManifestoKeywordsProfilingLineItem}
 *
 * 
 * @author Matteo Moci ( moci@cybion.it )
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProjectDefaultProfilingLineTestCase {
	
	private static Logger logger = Logger.getLogger(ProjectDefaultProfilingLineTestCase.class);

    private DefaultProfilingLine defaultProfilingLine;
    
    private ResultTestRepo repo;
    
    @BeforeTest
    public void setUp() {
        repo = new ResultTestRepo();
        defaultProfilingLine = new DefaultProfilingLine(
                "project-default-profiling-line",
                "default line to test the whole behavior of project-profiling-line"
        );
    	
    	ProfilingLineItem pli1 = new ProjectManifestoKeywordsProfilingLineItem("keywords-extraction-item", "extracts the knowledge");
        ProfilingLineItem pli2 = new ProjectProfileBuilderLineItem("project-builder-item", "builds instances of ProjectProfile class");

        pli1.setNextProfilingLineItem(pli2);
        pli2.setNextProfilingLineItem(new LoggingProfilingLineItem("logging-item", "it just logs", logger, repo));

    	defaultProfilingLine.setProfilingLineItem(pli1);
    }

    @AfterTest
    public void tearDown() {
    	defaultProfilingLine = null;
        repo = null;
    }
    
    @Test
    public void runProfilingLine() throws ProfilingLineException, URISyntaxException {

        Project project = new Project(94385738L);
        String manifestoKeywords = " basketball, google ";
        project.setManifestoKeywords(manifestoKeywords);
        List<String> membersId = new ArrayList<String>();
        membersId.add("43");
        membersId.add("51");
        project.setMembersIds(membersId);

        List<URI> manifestoConcepts = new LinkedList<URI>();
        manifestoConcepts.add(new URI("http://dbpedia.org/resource/Basketball"));
        manifestoConcepts.add(new URI("http://dbpedia.org/resource/Google"));

        ProfilingInput profilingInput = new ProfilingInput(project);
    	defaultProfilingLine.run(profilingInput);

        ProjectProfile projectProfile = (ProjectProfile) repo.getValue();
        logger.info("Resulting project projectProfile: '" + projectProfile + "'");

        Assert.assertNotNull(projectProfile);
        Assert.assertTrue(projectProfile.getManifestoConcepts().size() > 0);
        Assert.assertTrue(projectProfile.getManifestoConcepts().equals(manifestoConcepts));
        Assert.assertTrue(projectProfile.getProjectInvolvements().size() == 2);
    }
    

    public static class LoggingProfilingLineItem extends ProfilingLineItem {

        private Logger logger;

        private ResultTestRepo repo;

        public LoggingProfilingLineItem(String name, String description, Logger logger, ResultTestRepo repo) {
            super(name, description);
            this.logger = logger;
            this.repo = repo;
        }

        public void execute(Object input) throws ProfilingLineItemException {
            logger.info(input.toString());
            repo.setValue(input);
        }
    }

    public static class ResultTestRepo<T> {

        private T value;

        public void setValue(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

    }
    
}
