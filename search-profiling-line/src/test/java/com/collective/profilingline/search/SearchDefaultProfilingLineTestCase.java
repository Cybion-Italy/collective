package com.collective.profilingline.search;

import java.net.URI;
import java.net.URISyntaxException;
import com.collective.model.profile.SearchProfile;
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
 * composed with a single {@link SearchProfileBuilderLineItem} item.
 * 
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchDefaultProfilingLineTestCase {
	
	private static Logger logger = Logger.getLogger(SearchDefaultProfilingLineTestCase.class);

    private DefaultProfilingLine defaultProfilingLine;
    
    private ResultTestRepo repo;
    
    @BeforeTest
    public void setUp() {
        repo = new ResultTestRepo();
        defaultProfilingLine = new DefaultProfilingLine(
                "search-default-profiling-line",
                "default line to test the whole behavior of search-profiling-line"
        );
    	
        ProfilingLineItem pli1 = new SearchProfileBuilderLineItem("searchprofile-builder-item", "builds instances of SearchProfile class");
        pli1.setNextProfilingLineItem(new LoggingProfilingLineItem("logging-item", "it just logs", logger, repo));
    	defaultProfilingLine.setProfilingLineItem(pli1);
    }

    @AfterTest
    public void tearDown() {
    	defaultProfilingLine = null;
        repo = null;
    }
    
    @Test
    public void runProfilingLine() throws ProfilingLineException, URISyntaxException {

        SearchProfile searchProfile = new SearchProfile();
        searchProfile.setId(5L);
        searchProfile.setTitle("test search");
        searchProfile.addConcept(new URI("http://firstUri.com"));

        ProfilingInput profilingInput = new ProfilingInput(searchProfile);
    	defaultProfilingLine.run(profilingInput);

        SearchProfile searchProfileEnd = (SearchProfile) repo.getValue();
        logger.info("Resulting searchProfileEnd: '" + searchProfileEnd.toString() + "'");

        Assert.assertEquals(searchProfileEnd.getId(), searchProfile.getId());
        Assert.assertEquals(searchProfileEnd, searchProfile);
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
