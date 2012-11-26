package com.collective.profilingline;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.collective.model.profile.Profile;
import com.collective.model.profile.UserProfile;
import com.collective.profilingline.builder.BuilderProfileLineItem;
import com.collective.profilingline.knowledge.KeywordsProfilingLineItem;
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
import com.collective.model.User;

/**
 * This class act as an integration test for the whole {@link com.collective.profiler.line.DefaultProfilingLine}
 * composed with {@link com.collective.profilingline.knowledge.KeywordsProfilingLineItem} items.
 *
 * @see {@link com.collective.profilingline.knowledge.KeywordsProfilingLineItem}
 *
 * 
 * @author Matteo Moci ( moci@cybion.it )
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilingLineTestCase {
	
	private static Logger logger = Logger.getLogger(DefaultProfilingLineTestCase.class);

    private DefaultProfilingLine defaultProfilingLine;
    
    private ResultTestRepo repo;
    
    @BeforeTest
    public void setUp() {
        repo = new ResultTestRepo();
        defaultProfilingLine = new DefaultProfilingLine(
                "default-profiling-line",
                "default line to test the whole behavior"
        );
    	
    	ProfilingLineItem pli1 = new KeywordsProfilingLineItem("keywords-extraction-item", "extracts the knowledge");
        ProfilingLineItem pli2 = new BuilderProfileLineItem("builder-item", "builds instances of Profile class");

        pli2.setNextProfilingLineItem(new LoggingProfilingLineItem("logging-item", "it just logs", logger, repo));
        pli1.setNextProfilingLineItem(pli2);
    	defaultProfilingLine.setProfilingLineItem(pli1);
    }

    @AfterTest
    public void tearDown() {
    	defaultProfilingLine = null;
        repo = null;
    }
    
    @Test
    public void runProfilingLine() throws ProfilingLineException, URISyntaxException {
    	User user = new User(348563756L);
    	String interests = " basketball, google ";
        String skills = " freebase, dublin ";
        user.setInterestsKeywords(interests);
        user.setSkillsKeywords(skills);

        List<URI> expectedInterests = new LinkedList<URI>();
        expectedInterests.add(new URI("http://dbpedia.org/resource/Basketball"));
        expectedInterests.add(new URI("http://dbpedia.org/resource/Google"));


        List<URI> expectedSkillsUris = new LinkedList<URI>();
        //old non disambiguated uri
        expectedSkillsUris.add(new URI("http://dbpedia.org/resource/Freebase"));
        expectedSkillsUris.add(new URI("http://dbpedia.org/resource/Dublin"));

        ProfilingInput profilingInput = new ProfilingInput(user);
    	defaultProfilingLine.run(profilingInput);

        UserProfile profile = (UserProfile) repo.getValue();
        logger.info("Resulting profile: '" + profile + "'");

        Assert.assertNotNull(profile);
        Assert.assertTrue(profile.getInterests().size() > 0);
        Assert.assertTrue(profile.getSkills().size() > 0);

        Assert.assertTrue(profile.getInterests().equals(expectedInterests));
        Assert.assertTrue(profile.getSkills().equals(expectedSkillsUris));
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
