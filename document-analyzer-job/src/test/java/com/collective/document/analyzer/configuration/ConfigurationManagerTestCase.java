package com.collective.document.analyzer.configuration;

import com.collective.analyzer.configuration.ConfigurationManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ConfigurationManagerTestCase {

    private static String CONFIGURATION = "src/test/resources/configuration.xml";
    private static final Logger logger = Logger.getLogger(ConfigurationManagerTestCase.class);
    protected ConfigurationManager configurationManager;


    @BeforeClass
    public void setUpConfig() {
        configurationManager =
                ConfigurationManager.getInstance(CONFIGURATION);
        logger.info("Loaded configuration from: '" + CONFIGURATION + "'");

    }

    @AfterClass
    public void tearDownConfig() {
        configurationManager = null;
        logger.info("ended test");
    }

    @Test
    public void shouldLoadExampleConfiguration() {
        Assert.assertNotNull(configurationManager.getDocumentsToAnalyze());
        Assert.assertTrue(configurationManager.getDocumentsToAnalyze() >= 0);
        Assert.assertNotNull(configurationManager.getDocumentStorage());
        Assert.assertNotNull(configurationManager.getWebResourcesPersistenceConfiguration());
        Assert.assertNotNull(configurationManager.getActivityLogConfiguration());
    }
}
