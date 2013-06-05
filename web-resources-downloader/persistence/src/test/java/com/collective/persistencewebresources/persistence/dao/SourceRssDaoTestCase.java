package com.collective.persistencewebresources.persistence.dao;



import com.collective.model.persistence.SourceRss;
import com.collective.persistencewebresources.domain.DomainFixture;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class SourceRssDaoTestCase extends AbstractDaoTestCase {

    private static final Logger LOGGER = Logger.getLogger(SourceRssDaoTestCase.class);
    private SourceRssDao sourceRssDao;

    public SourceRssDaoTestCase() {
        super();
    }

	@BeforeClass
	public void setUpBefore() throws Exception {
        sourceRssDao = new SourceRssDao(webResourcePersistenceConfiguration.getProperties());
	}

	@AfterClass
	public void tearDownAfter() throws Exception {
        sourceRssDao = null;
	}

	@Test
	public void testShouldSelectAllFonteRss() throws MalformedURLException {
        //get the actual size
        int actualSize = sourceRssDao.selectAll().size();
        int id = -1;
        try {
            // insert a new one
            SourceRss sourceRss = DomainFixture.newTestFonteRssComplete();
            sourceRssDao.insert(sourceRss);
            id = sourceRss.getId();

            // check is there
            Assert.assertNotNull(sourceRssDao.selectById(id));

            // get all the Source
            Assert.assertTrue(sourceRssDao.selectAll().size() == actualSize + 1);

            //update it
            URL updatedURL = new URL("http://newest.sourcerss.url.com/test");
            sourceRss.setUrl(updatedURL);
            sourceRssDao.update(sourceRss);

            //check it was well changed
            Assert.assertTrue(sourceRssDao.selectById(id).getUrl().toString().equals(
                                updatedURL.toString()));
        } finally {
            // ok, delete it and check the deletion
            sourceRssDao.delete(id);
            Assert.assertTrue(sourceRssDao.selectById(id) == null);
            }
	}

    /*
    // TODO: refactor and re-activate this test
    @Test
	public void testShouldSelectFonteRssByFonte() {
		SqlSession session = sqlSessionFactory.openSession();
		List<FonteRss> fontiRss = null;
		Source fonte = null;

		//get a Source
		try {
			SourceMapper fonteDao = session.getMapper(SourceMapper.class);
			fonte = fonteDao.selectFonte(new Integer(1));
		} finally {
			session.close();
		}

		assertNotNull(fonte);
		assertNotNull(fonte.getUrl());

		//get all its FonteRss associated
		try {
			SourceRssMapper fonteRssMapper = session.getMapper(SourceRssMapper.class);
			fontiRss = fonteRssMapper.selectFonteRssByFonte(fonte);
		} finally {
			session.close();
		}

		for (FonteRss fonteRss : fontiRss) {
			assertTrue(fonteRss.getSource().getId().equals(fonte.getId()));
			assertNotNull(fonteRss.getUrl());
			System.out.println("url fonterss: " + fonteRss.getUrl());
		}

	} */

    @Test
    public void testShouldSelectFonteRssByIdAndGetDateTime() {
        int id = -1;
        // insert a new Source
        SourceRss sourceRss = DomainFixture.newTestFonteRssComplete();
        DateTime fakeDateTime = new DateTime();
        sourceRss.setDataUltimaEstrazione(fakeDateTime);
        sourceRssDao.insert(sourceRss);
        id = sourceRss.getId();

        try {
            // check is there
            SourceRss sourceRssToBeChecked = sourceRssDao.selectById(id);
            Assert.assertNotNull(sourceRssToBeChecked);
            Assert.assertNotNull(sourceRss.getDataUltimaEstrazione());
            Assert.assertTrue(sourceRss.getDataUltimaEstrazione().isEqual(fakeDateTime));
        } finally {
            // ok, delete it and check the deletion
            sourceRssDao.delete(id);
            Assert.assertTrue(sourceRssDao.selectById(id) == null);
        }
    }
	
	@Test
	public void testShouldUpdateFonteIfNecessary() {

        // insert a new Source and retrieve it
        SourceRss sourceRss = DomainFixture.newTestFonteRssComplete();
        sourceRssDao.insert(sourceRss);
        int id = sourceRss.getId();
        SourceRss retrievedSourceRss = sourceRssDao.selectById(id);
        Assert.assertNotNull(retrievedSourceRss);

        // update it
        retrievedSourceRss.setLingua("nuovo valore della lingua");
        sourceRssDao.update(retrievedSourceRss);

        // get it back and check the update
        SourceRss shouldBeUpdatedSourceRss = sourceRssDao.selectById(id);
        Assert.assertNotNull(shouldBeUpdatedSourceRss);
        Assert.assertEquals(retrievedSourceRss.getLingua(), shouldBeUpdatedSourceRss.getLingua());

        // ok, delete it and check the deletion
        sourceRssDao.delete(id);
        Assert.assertTrue(sourceRssDao.selectById(id) == null);
	}

    @Test
    public void shouldTestCRUD() {
        //create a fake sourceRss
        SourceRss expectedSourceRss = DomainFixture.newTestFonteRssComplete();
        //save it on db
        try {
            sourceRssDao.insert(expectedSourceRss);
            Assert.assertNotNull(expectedSourceRss.getId());
            LOGGER.debug("saved sourceRss with id: " + expectedSourceRss.getId());
            //retrieve it
            SourceRss retrievedSourceRss = sourceRssDao.selectById(expectedSourceRss.getId());
            LOGGER.debug("expected:  " + expectedSourceRss.toString());
            LOGGER.debug("retrieved: " + retrievedSourceRss.toString());
            //check if equals
            Assert.assertEquals(expectedSourceRss.getLingua(), retrievedSourceRss.getLingua());
            Assert.assertEquals(expectedSourceRss, retrievedSourceRss);
            Assert.assertEquals(expectedSourceRss.getUrl(), retrievedSourceRss.getUrl());
            Assert.assertTrue(expectedSourceRss.getDataUltimaEstrazione().equals(retrievedSourceRss.getDataUltimaEstrazione()));
        } catch (Exception e) {

        } finally {
            //delete it
            sourceRssDao.delete(expectedSourceRss.getId());
            //check it was deleted
            SourceRss shouldBeNullSourceRss = sourceRssDao.selectById(expectedSourceRss.getId());
            Assert.assertNull(shouldBeNullSourceRss);
        }
    }

	
}
