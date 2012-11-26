package com.collective.usertests.persistence.dao;

import com.collective.usertests.persistence.configuration.ConfigurationManager;
import com.collective.usertests.persistence.configuration.UserFeedbackPersistenceConfiguration;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class AbstractDaoTestCase {

    private final static Logger logger = Logger.getLogger(AbstractDaoTestCase.class);

    //a shared configuration to be accessed from all extending classes
    protected UserFeedbackPersistenceConfiguration userFeedbackPersistenceConfiguration;

    private ConfigurationManager configurationManager;

    private static final String CONFIG_FILE =
            "user-feedback-persistence-configuration-tests.xml";

    protected AbstractDaoTestCase() {
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
        userFeedbackPersistenceConfiguration =
                configurationManager.getUserFeedbackPersistenceConfiguration();
    }

    @BeforeClass
    public void setUp(){
        logger.debug("starting daos tests");
    }

    @AfterClass
    public void tearDown() {
        logger.debug("ended daos tests");
    }
}
