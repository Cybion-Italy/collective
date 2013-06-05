package com.collective.persistencewebresources.persistence.dao;

import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SourceDaoTestCase extends AbstractDaoTestCase {

    private SourceDao sourceDao;
    private static final Logger LOGGER = Logger.getLogger(SourceDaoTestCase.class);


    public SourceDaoTestCase() {
        super();
    }

	@BeforeClass
	public void setUpBefore() throws Exception {
        sourceDao = new SourceDao(webResourcePersistenceConfiguration.getProperties());
	}

	@AfterClass
	public void tearDownAfter() throws Exception {
        sourceDao = null;
	}

	@Test
	public void testShouldSelectAllFonteAndValidFontiRss() {
        List<Source> sources = sourceDao.selectAllFonteAndValidFontiRss();
        for (Source source : sources) {
            Assert.assertNotNull(source);
            Assert.assertNotNull(source.getId());
            Assert.assertNotNull(source.getNome());
            Assert.assertNotNull(source.getUrl());
            Assert.assertNotNull(source.getSourcesRss());
            for (SourceRss sourceRss : source.getSourcesRss()) {
                Assert.assertNotNull(sourceRss);
                Assert.assertTrue(sourceRss.isValid());
            }
            LOGGER.debug("src: " + source.toString());
        }
    }
}
