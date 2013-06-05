package com.collective.persistencewebresources.persistence.dao;

import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.collective.persistencewebresources.domain.DomainFixture;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.List;

public class WebResourceDaoTestCase extends AbstractDaoTestCase {

    //TODO manage exceptions better!
    private WebResourceDao webResourceDao;
    private SourceRssDao sourceRssDao;
    private static final Logger LOGGER =
            Logger.getLogger(WebResourceDaoTestCase.class);

    public WebResourceDaoTestCase() {
        super();
    }

    @BeforeClass
    public void startUp() {
        this.webResourceDao =
                new WebResourceDao(webResourcePersistenceConfiguration.getProperties());
        this.sourceRssDao =
                new SourceRssDao(webResourcePersistenceConfiguration.getProperties());
    }

    @AfterClass
    public void shutDown() {
        this.webResourceDao = null;
        this.sourceRssDao = null;
    }

    @Test(enabled = true)
    public void shouldTestCRUD() throws Exception {
        WebResource webResourceInserted = DomainFixture.newTestUrl();
        WebResource webResourceToBeRetrieved = null;

        try {
            // insert a new WebResource
            this.webResourceDao.insert(webResourceInserted);
            Assert.assertNotNull(webResourceInserted.getId());
            // check it has been saved
            webResourceToBeRetrieved =
                    this.webResourceDao.select(webResourceInserted.getId());

            Assert.assertNotNull(webResourceToBeRetrieved);
            Assert.assertEquals(webResourceToBeRetrieved.getId(),
                    webResourceInserted.getId());
            Assert.assertNotNull(webResourceToBeRetrieved.getTitolo());
            Assert.assertNotNull(webResourceToBeRetrieved.getDataEstrazione());
            Assert.assertNotNull(webResourceToBeRetrieved.getDataPubblicazione());
            Assert.assertNotNull(webResourceToBeRetrieved.isAnalyzed());

            //assert equals
            Assert.assertTrue(webResourceToBeRetrieved.getDataEstrazione()
                    .equals(webResourceInserted.getDataEstrazione()));
            Assert.assertTrue(webResourceToBeRetrieved.getDataPubblicazione()
                    .equals(webResourceInserted.getDataPubblicazione()));


            //assert update
            // change something and update it
            boolean originalValue = webResourceToBeRetrieved.isAnalyzed();
            webResourceToBeRetrieved.setAnalyzed(!webResourceToBeRetrieved.isAnalyzed());
            webResourceDao.update(webResourceToBeRetrieved);

            // get it back
            WebResource webResourceShouldBeUpdated =
                    webResourceDao.select(webResourceToBeRetrieved.getId());
            Assert.assertNotNull(webResourceShouldBeUpdated);

            // assert it's changed
            Assert.assertTrue(webResourceShouldBeUpdated.isAnalyzed()
                    != originalValue);

        } catch (Exception e) {
            LOGGER.debug("exception during test: " + e);
        } finally {
            // delete it
            this.webResourceDao.delete(webResourceToBeRetrieved);
            // check that is no longer there
            webResourceToBeRetrieved =
                    this.webResourceDao.select(new Integer(webResourceInserted.getId()));
            Assert.assertTrue(webResourceToBeRetrieved == null);
        }
    }

    @Test (enabled = true)
    public void shouldFindUrlsByExample() throws Exception {
        WebResource webResource = DomainFixture.newTestUrl();
        webResource.setAnalyzed(false);

        int amount = 1;

        try {
            this.webResourceDao.insert(webResource);
            LOGGER.debug("selecting urls by example");
            LOGGER.debug(webResource.toString());
            List<WebResource> notAnalyzedWebResources =
                    this.webResourceDao.selectByExample(webResource, amount);
            Assert.assertTrue(notAnalyzedWebResources.size() >= 0);
            Assert.assertTrue(notAnalyzedWebResources.size() <= 1);
            Assert.assertEquals(notAnalyzedWebResources.get(0), webResource);

        } catch (Exception e) {
            LOGGER.error("error: " + e.getMessage() + " when searching for webResource " +
                         webResource.getId());
            e.printStackTrace();
        } finally {
            this.webResourceDao.delete(webResource);
        }
    }

    @Test(enabled = true)
    public void shouldFindLastExtractedUrl() throws Exception {
        //TODO: Test
        SourceRss sourceRss = DomainFixture.newTestFonteRss();

        //build two web resources of a sourceRss, extracted one right now, the other yesterday
        WebResource expectedWebResource = DomainFixture.newTestUrl();
        expectedWebResource.setDataEstrazione(new DateTime());

        WebResource otherWebResource = DomainFixture.newTestUrl();
        otherWebResource.setDataEstrazione(new DateTime().minusDays(10));

        try {
            //insert the sourceRss and then its associated webResources
            sourceRssDao.insert(sourceRss);
            expectedWebResource.setSourceRss(sourceRss);
            otherWebResource.setSourceRss(sourceRss);
            webResourceDao.insert(expectedWebResource);
            webResourceDao.insert(otherWebResource);

            //retrieve from db the last webResource from the sourceRss
            List<WebResource> lastExtractedWebResources = webResourceDao
                    .selectLastExtractedFrom(sourceRss);
            WebResource lastExtractedWebResource = lastExtractedWebResources.get(0);
            LOGGER.debug("sourceRss: " + sourceRss);
            LOGGER.debug("lastExtractedWeb: " + lastExtractedWebResource);

            //assert that i get the expected one
            Assert.assertTrue(expectedWebResource.getDataEstrazione()
                    .equals(lastExtractedWebResource.getDataEstrazione()));
            Assert.assertTrue(lastExtractedWebResource.getDataEstrazione()
                    .compareTo(otherWebResource.getDataEstrazione()) > 0);
            Assert.assertNotNull(lastExtractedWebResource.getUrl());
        } catch (Exception e) {
            LOGGER.error("exception during tests: " + e);
        } finally {
            //remove sourceRss and its webResources
            webResourceDao.delete(expectedWebResource);
            webResourceDao.delete(otherWebResource);
            sourceRssDao.delete(sourceRss.getId());

            //check they are deleted
            Assert.assertNull(sourceRssDao.selectById(sourceRss.getId()));
            Assert.assertNull(webResourceDao.select(expectedWebResource.getId()));
            Assert.assertNull(webResourceDao.select(otherWebResource.getId()));
        }
    }

    @Test(enabled = true)
    public void shouldFindLastExtractedUrlFromBrandNewSourceRss()
            throws MalformedURLException
    {
        SourceRss sourceRss = DomainFixture.newTestFonteRssComplete();
        try {
            //insert the sourceRss with NO webresources
            sourceRssDao.insert(sourceRss);

            //retrieve from db the last webResource from the sourceRss
            List<WebResource> lastExtractedWebResource =
                    webResourceDao.selectLastExtractedFrom(sourceRss);
            //it should be = 0
            Assert.assertEquals(lastExtractedWebResource.size(), 0);
        } catch (Exception e) {
            LOGGER.error("exception during tests: " + e);
        } finally {
            sourceRssDao.delete(sourceRss.getId());
            //check they are deleted
            Assert.assertNull(sourceRssDao.selectById(sourceRss.getId()));
        }
    }

    @Test(enabled = true)
    public void shouldFindUrlsLimited() throws Exception {
        final int urlCount = 3;
        List<WebResource> urlsToBeAnalyzed = DomainFixture.newUrlsToBeAnalyzed(7);

        try {
            // insert these urls
            for (WebResource webResource : urlsToBeAnalyzed) {
                webResourceDao.insert(webResource);
            }

            WebResource webResource = DomainFixture.newTestUrl();
            webResource.setAnalyzed(false);
            List<WebResource> notAnalyzedWebResources =
                    this.webResourceDao.selectByExample(webResource, urlCount);
            Assert.assertTrue(notAnalyzedWebResources.size() == urlCount);

            for (WebResource singleWebResource : notAnalyzedWebResources) {
//                Assert.assertTrue(singleWebResource.getFonteRss() != null);
                Assert.assertTrue(singleWebResource.getId() != null);
                Assert.assertFalse(singleWebResource.isAnalyzed());
//                Assert.assertFalse(singleWebResource.getFonteRss().getUrl().equals(""));
            }

        } catch (Exception e) {
            LOGGER.error("error in shouldFindUrlsLimited: " + e.getMessage());
        } finally {
            // delete the inserted urls
            for (WebResource webResourceToBeDeleted : urlsToBeAnalyzed) {
                webResourceDao.delete(webResourceToBeDeleted);
            }
        }

    }
}
