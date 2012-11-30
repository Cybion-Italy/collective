package com.collective.recommender.configuration;

import com.collective.profiler.storage.ProfileStoreConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConfigurationManagerTestCase {

    private final static String CONFIG_FILE = "src/test/resources/recommender-configuration.xml";

    private ConfigurationManager configurationManager;

    @BeforeTest
    public void setUp() {
        System.out.println("=========" + System.getProperty("user.dir"));
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);        
    }

    @AfterTest
    public void tearDown() {
        configurationManager = null;
    }

    @Test
    public void testGetConfigurations() throws URISyntaxException {
        // TODO make this more robust
        ProfileStoreConfiguration profileStoreConfiguration =
                configurationManager.getProfileStoreConfiguration();
        RecommenderConfiguration recommenderConfiguration = 
                configurationManager.getRecommenderConfiguration();
        RecommendationsStorageConfiguration recommendationsStorageConfiguration =
                configurationManager.getRecommendationsStorageConfiguration();
        PermanentSearchStorageConfiguration permanentSearchStorageConfiguration =
                configurationManager.getPermanentSearchStorageConfiguration();
        CategoriesMappingStorageConfiguration categoriesMappingStorageStorageConfiguration =
                configurationManager.getCategoriesMappingStorageStorageConfiguration();

        assertNotNull(profileStoreConfiguration);
        assertNotNull(recommenderConfiguration);
        assertNotNull(categoriesMappingStorageStorageConfiguration);


        URI customAnnotationsTemplate = new URI("http://collective.com/annotation/user/");
        Assert.assertEquals(recommenderConfiguration.getIndexes().get("custom-annotations"),
                            customAnnotationsTemplate);

        assertNotNull(recommendationsStorageConfiguration);
        assertNotNull(permanentSearchStorageConfiguration);
        assertTrue(permanentSearchStorageConfiguration.getProperties()
                .getProperty("url")
                .endsWith("collective-permanent-search"));
    }

}
