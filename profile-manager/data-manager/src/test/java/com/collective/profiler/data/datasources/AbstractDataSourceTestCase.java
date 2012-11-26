package com.collective.profiler.data.datasources;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import com.collective.profiler.data.DataManagerConfiguration;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * This class should be extended by all test classes that want to test {@link com.collective.profiler.data.DataSource}s
 * implementations. It provides a dataManagerConfiguration that is used by extending classes.
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class AbstractDataSourceTestCase {

    private final static Logger logger = Logger.getLogger(AbstractDataSourceTestCase.class);

    //a shared configuration to be accessed from all extending classes
    protected DataManagerConfiguration dataManagerConfiguration;

    //TODO ask davide
    protected AbstractDataSourceTestCase() {
        dataManagerConfiguration = new DataManagerConfiguration();

        //TODO: should point to testing database
        dataManagerConfiguration.setMessagesPersistenceConfiguration(
                new MessagesPersistenceConfiguration(
                        "cibionte.cybion.eu",
                        3306,
                        "collective-messages",
                        "collective",
                        "collective"
                )
        );
    }

    @BeforeClass
    public void setUpBefore(){
        logger.debug("starting datasource tests");


    }

    @AfterClass
    public void tearDownAfter() {
        logger.debug("ended datasource tests");
    }
}
