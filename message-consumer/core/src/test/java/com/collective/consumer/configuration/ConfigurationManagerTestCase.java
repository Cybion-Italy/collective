package com.collective.consumer.configuration;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ConfigurationManagerTestCase {

    /* TODO: to complete testing, we should fire up a queue container with some messages */

    private ConfigurationManager configurationManager;
    private final static String CONFIG_FILE = "message-consumer-configuration-tests.xml";
    private final static Logger logger = Logger.getLogger(ConfigurationManagerTestCase.class);

    @BeforeTest
    public void setUp() {
        logger.debug("=========" + System.getProperty("user.dir"));
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
    }

    @AfterTest
    public void tearDown() {
        configurationManager = null;
    }

    @Test
    public void testMessageConsumerConfiguration() {
        MessagesPersistenceConfiguration mpc = configurationManager.getMessagesPersistenceConfiguration();
        QueueGatewayConfiguration qgc = configurationManager.getQueueGatewayConfiguration();
        Assert.assertNotNull(mpc);
        Assert.assertNotNull(qgc);
        Assert.assertTrue(mpc.getProperties().getProperty("url").endsWith("collective-messages"));
        Assert.assertEquals(qgc.getQueueName(), "foo.bar");
    }
}
