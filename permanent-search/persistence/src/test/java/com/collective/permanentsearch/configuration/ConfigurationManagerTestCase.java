package com.collective.permanentsearch.configuration;

import com.collective.permanentsearch.persistence.configuration.ConfigurationManager;
import com.collective.permanentsearch.persistence.configuration.PermanentSearchPersistenceConfiguration;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ConfigurationManagerTestCase {

    private static final String CONFIG_FILE = "permanent-search-persistence-configuration-tests.xml";
    private ConfigurationManager configurationManager;
    private Logger logger = Logger.getLogger(ConfigurationManagerTestCase.class);

    @BeforeClass
    public void setUp() {
        logger.debug("=========" + System.getProperty("user.dir"));
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
    }

    @AfterClass
    public void tearDown() {
        configurationManager = null;
    }

    @Test(enabled = true)
    public void testConfiguration() {
        PermanentSearchPersistenceConfiguration permanentSearchPersistenceConfiguration =
                configurationManager.getPermanentSearchPersistenceConfiguration();
        Assert.assertNotNull(permanentSearchPersistenceConfiguration);
        //check database name is correct
        Assert.assertTrue(permanentSearchPersistenceConfiguration.getProperties()
                .getProperty("url").endsWith("collective-permanent-search"));
    }
}
