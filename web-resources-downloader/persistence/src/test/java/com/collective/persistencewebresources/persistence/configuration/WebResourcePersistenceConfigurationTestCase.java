package com.collective.persistencewebresources.persistence.configuration;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class WebResourcePersistenceConfigurationTestCase {

    private final static Logger logger = Logger.getLogger(WebResourcePersistenceConfigurationTestCase.class);
    private WebResourcesPersistenceConfiguration webResourcePersistenceConfiguration;

    private String host = "cibionte.cybion.eu";
    private int port = 3306;
    private String username = "collective";
    private String password = "collective";
    private String db = "collective-resources";

    @BeforeTest
    public void setUp() {
        logger.debug("=========" + System.getProperty("user.dir"));
        webResourcePersistenceConfiguration = new WebResourcesPersistenceConfiguration(host, port, db, username, password);
    }

    @AfterTest
    public void tearDown() {
        webResourcePersistenceConfiguration = null;
    }

    @Test
    public void testGetProperties() {
        Properties dbProperties = webResourcePersistenceConfiguration.getProperties();

        //simple test of setup properties
        Assert.assertEquals("com.mysql.jdbc.Driver", dbProperties.getProperty("driver"));
        Assert.assertTrue(dbProperties.getProperty("url").contains(host));
        Assert.assertEquals("jdbc:mysql://cibionte.cybion.eu:3306/collective-resources", dbProperties.getProperty("url"));
        Assert.assertEquals(username, dbProperties.getProperty("username"));
        Assert.assertEquals(password, dbProperties.getProperty("password"));
    }
}
