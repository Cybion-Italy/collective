package com.collective.messages.persistence.dao;

import com.collective.messages.persistence.configuration.ConfigurationManager;
import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class AbstractDaoTestCase extends BaseDataTestCase {

    private final static Logger LOGGER = Logger.getLogger(AbstractDaoTestCase.class);

    //a shared configuration to be accessed from all extending classes
    protected MessagesPersistenceConfiguration messagesPersistenceConfiguration;

    private ConfigurationManager configurationManager;

    private static final String CONFIG_FILE = "/messages-persistence-configuration-tests.xml";

    private static final String MESSAGES_PERSISTENCE_DDL  = "messages-persistence-schema.sql";

    private static final String MESSAGES_PERSISTENCE_DATA = "messages-persistence-data.sql";

    protected AbstractDaoTestCase() {
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
        messagesPersistenceConfiguration = configurationManager.getMessagesPersistenceConfiguration();
    }

    public static final String DATABASE_USER = "user";

    public static final String DATABASE_PASSWORD = "password";

    public static final String MYSQL_AUTO_RECONNECT = "autoReconnect";

    public static final String MYSQL_MAX_RECONNECTS = "maxReconnects";

    @BeforeClass
    public void setUpBase() throws ClassNotFoundException, SQLException, IOException {
        LOGGER.debug("starting messages daos tests");
        Connection connection = getConnectionFromProperties();
        LOGGER.debug("executing sql on db instance");
        ScriptRunner runner = new ScriptRunner(connection);
        runScript(runner, MESSAGES_PERSISTENCE_DDL);
        LOGGER.debug("created schema, populating with test data");
        runScript(runner, MESSAGES_PERSISTENCE_DATA);
        LOGGER.debug("finished executing sql - test data is on db");
    }

    @AfterClass
    public void tearDownBase() {
        LOGGER.debug("ended messages daos tests");
    }

    public Connection getConnectionFromProperties() throws ClassNotFoundException, SQLException {

        Properties mpc = this.messagesPersistenceConfiguration.getProperties();

        String driver = mpc.getProperty(MessagesPersistenceConfiguration.DRIVER);
        Class.forName(driver);
        String dbURL      = mpc.getProperty(MessagesPersistenceConfiguration.URL);
        String dbUsername = mpc.getProperty(MessagesPersistenceConfiguration.USERNAME);
        String dbPassword = mpc.getProperty(MessagesPersistenceConfiguration.PASSWORD);

        Properties connProperties = new java.util.Properties();
        connProperties.put(DATABASE_USER, dbUsername);
        connProperties.put(DATABASE_PASSWORD, dbPassword);

        // set additional connection properties:
        // if connection stales, then make automatically
        // reconnect; make it alive again;
        // if connection stales, then try for reconnection;
        connProperties.put(MYSQL_AUTO_RECONNECT, "true");
        connProperties.put(MYSQL_MAX_RECONNECTS, "4");
        Connection connection = DriverManager.getConnection(dbURL, connProperties);
        return connection;
    }
}
