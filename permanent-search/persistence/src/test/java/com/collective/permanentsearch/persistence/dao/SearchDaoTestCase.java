package com.collective.permanentsearch.persistence.dao;

import com.collective.permanentsearch.model.LabelledURI;
import com.collective.permanentsearch.model.Search;
import com.collective.permanentsearch.persistence.fixtures.SearchFixture;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SearchDaoTestCase extends AbstractDaoTestCase {

    private SearchDao searchDao;
    private static final Logger logger = Logger.getLogger(SearchDao.class);

    public SearchDaoTestCase() {
        super();
    }

    @BeforeClass
    public void setUp() {
        this.searchDao = new SearchDao(messagesPersistenceConfiguration.getProperties());
        logger.info("setup");
    }

    @AfterClass
    public void tearDown() {
        this.searchDao = null;
        logger.info("tore down");
    }

    @Test(enabled = true)
    public void shouldTestBasicCRUD() {

        List<Search> initialDb = searchDao.selectAllSearches();

        Search search = SearchFixture.getSearch(new DateTime());
        try {
            searchDao.insert(search);
            Assert.assertNotNull(search.getId());
            Search retrievedSearch = searchDao.select(search.getId());
            Assert.assertNotNull(retrievedSearch);
            logger.debug("retrieved: " + retrievedSearch.toString());
            logger.debug("collection class: " + retrievedSearch.getCommonUris().getClass());
            logger.debug("inserted:  " + search.toString());
            Assert.assertEquals(retrievedSearch, search);
            List<Search> afterOneInsert = searchDao.selectAllSearches();
            Assert.assertEquals(afterOneInsert.size(), initialDb.size() + 1);
        } finally {
            searchDao.delete(search.getId());
        }
        Assert.assertNull(searchDao.select(search.getId()));
    }

    @Test (enabled = true)
    public void shouldTestSelectByUserId() {
        Long userId = 18648649969L;

        //two new searches
        Search searchToSaveOne = SearchFixture.getSearch(new DateTime(0L));
        Search searchToSaveTwo = SearchFixture.getSearch(new DateTime().plusDays(1));

        List<Search> searchesToSave = new ArrayList<Search>();

        try {
            searchesToSave.add(searchToSaveOne);
            searchesToSave.add(searchToSaveTwo);

            //save the association to a user
            for (Search searchToSave : searchesToSave) {
                searchDao.insert(searchToSave);
                searchDao.insertUserSearch(userId, searchToSave.getId());
            }

            //get only the single one oldest project search
            List<Search> oldestUserSearches = searchDao.selectOldestUserSearches(1L);
            Assert.assertTrue(oldestUserSearches.size() == 1);
            Assert.assertEquals(oldestUserSearches.get(0).getLastProfilationDate(),
                    searchToSaveOne.getLastProfilationDate());

            //select them
            List<Search> userSearches = searchDao.selectByUserId(userId);

            //assert these are not null
            Assert.assertNotNull(userSearches);
            Assert.assertTrue(userSearches.size() == searchesToSave.size());

            for (Search search : userSearches) {
                logger.debug("found userSearches: " + search);
            }

            Assert.assertEquals (userSearches, searchesToSave);

        } finally {

            for (Search searchToSave : searchesToSave) {
                searchDao.deleteUserSearch(userId, searchToSave.getId());
                searchDao.delete(searchToSave.getId());
            }
        }
        //assert they were deleted
        List<Search> deletedSearches = searchDao.selectByUserId(userId);
        Assert.assertTrue(deletedSearches.size() == 0);
    }

    @Test (enabled = true)
    public void shouldTestSelectByProjectId() {
        Long projectId = 1468539636L;

        //two new searches
        Search searchToSaveOne = SearchFixture.getSearch(new DateTime(0L));
        Search searchToSaveTwo = SearchFixture.getSearch(new DateTime().plusDays(1));

        List<Search> searchesToSave = new ArrayList<Search>();

        try {
            searchesToSave.add(searchToSaveOne);
            searchesToSave.add(searchToSaveTwo);

            //save the association to a user
            for (Search searchToSave : searchesToSave) {
                searchDao.insert(searchToSave);
                searchDao.insertProjectSearch(projectId, searchToSave.getId());
            }

            //select them
            List<Search> projectSearches = searchDao.selectByProjectId(projectId);

            //assert these are not null
            Assert.assertNotNull(projectSearches);
            Assert.assertTrue(projectSearches.size() == searchesToSave.size());

            for (Search search : projectSearches) {
                logger.debug("found projectSearches: " + search);
            }

            Assert.assertEquals (projectSearches, searchesToSave);

            //get only the single one oldest project search
            List<Search> oldestProjectSearches = searchDao.selectOldestProjectSearches(1L);
            Assert.assertTrue(oldestProjectSearches.size() == 1);
            Assert.assertEquals(oldestProjectSearches.get(0).getLastProfilationDate(),
                    searchToSaveOne.getLastProfilationDate());

             //select a list with only the oldest one
            List<Search> oldestSearches = searchDao.selectOldest(1L);
            Assert.assertTrue(oldestSearches.size() == 1);
            //the first one should be the search created first
            Assert.assertEquals(oldestSearches.get(0).getLastProfilationDate(),
                    searchToSaveOne.getLastProfilationDate());

            //get the first old search
            Search oldestSearch = oldestSearches.get(0);

            String title = "new updated Title";
            ArrayList<LabelledURI> conceptsUpdated = new ArrayList<LabelledURI>();
            LabelledURI labelledURI = new LabelledURI();
            labelledURI.setLabel("updated label");
            try {
                labelledURI.setUrl(new URI("http://updatedURI.com"));
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            conceptsUpdated.add(labelledURI);

            DateTime date = new DateTime().minusDays(3);

            logger.debug("object before update: " + oldestSearch.toString());

            oldestSearch.setTitle(title);
            oldestSearch.setCommonUris(conceptsUpdated);
            oldestSearch.setLastProfilationDate(date);

            //update on database
            searchDao.update(oldestSearch);

            //re-select it
            Search updatedSearch = searchDao.select(oldestSearch.getId());

            logger.debug("expected: " + oldestSearch.toString());
            logger.debug("actual: " + updatedSearch.toString());

            //check if all fields have been updated
            Assert.assertEquals(updatedSearch, oldestSearch);
            Assert.assertEquals(updatedSearch.getCommonUris(), oldestSearch.getCommonUris());
            Assert.assertEquals(updatedSearch.getLastProfilationDate(),
                    oldestSearch.getLastProfilationDate());
            Assert.assertEquals(updatedSearch.getTitle(), oldestSearch.getTitle());

        } finally {

            for (Search searchToSave : searchesToSave) {
                searchDao.deleteUserSearch(projectId, searchToSave.getId());
                searchDao.delete(searchToSave.getId());
            }
        }
        //assert they were deleted
        List<Search> deletedSearches = searchDao.selectByUserId(projectId);
        Assert.assertTrue(deletedSearches.size() == 0);
    }
}
