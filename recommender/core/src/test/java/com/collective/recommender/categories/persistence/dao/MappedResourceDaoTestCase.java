package com.collective.recommender.categories.persistence.dao;

import com.collective.recommender.categories.exceptions.DaoException;
import com.collective.recommender.categories.model.MappedResource;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class MappedResourceDaoTestCase {

    private MappedResourceDao mappedResourceDao;

    @BeforeClass
    public void setUp() {

        Properties testDbConnection = new Properties();
        String host = "gaia.cybion.eu";
        String port = "3306";
        String db = "collective-categories";
        String username= "collective-cat";
        String password = "categories";

        testDbConnection.setProperty("driver", "com.mysql.jdbc.Driver");
        testDbConnection.setProperty("url", "jdbc:mysql://" + host + ":" + port + "/" + db);
        testDbConnection.setProperty("username", username);
        testDbConnection.setProperty("password", password);

        mappedResourceDao = new MappedResourceDao(testDbConnection);
    }

    @AfterClass
    public void tearDown() {
        mappedResourceDao = null;
    }

    @Test
    public void shouldSelectLatestMappedResources() throws DaoException {
        Long userId = 20L;
        int limit = 10;
        List<MappedResource> mappedResources = this.mappedResourceDao.getLatestMappedResources(userId, limit);
        assertNotNull(mappedResources);
        assertTrue(mappedResources.size() > 0);
        assertTrue(mappedResources.size() <= 10);
    }
}
