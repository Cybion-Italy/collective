package com.collective.usertests.persistence.configuration;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ConfigurationManagerTestCase {
    private static final String CONFIG_FILE = "user-feedback-persistence-configuration-tests.xml";
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
        UserFeedbackPersistenceConfiguration
                userFeedbackPersistenceConfiguration =
                configurationManager.getUserFeedbackPersistenceConfiguration();
        Assert.assertNotNull(userFeedbackPersistenceConfiguration);
        Assert.assertTrue(userFeedbackPersistenceConfiguration.getProperties()
                .getProperty("url").endsWith("collective-test"));
    }
}
