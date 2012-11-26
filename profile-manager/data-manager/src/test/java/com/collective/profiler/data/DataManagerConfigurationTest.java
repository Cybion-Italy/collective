package com.collective.profiler.data;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import com.collective.model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Reference test case for {@link com.collective.profiler.data.DataManagerConfiguration}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
/* TODO should it be extended by all other tests on dataSources? */
public class DataManagerConfigurationTest {

    private static Logger logger = 
            Logger.getLogger(DataManagerConfigurationTest.class);

    private DataManagerConfiguration dataManagerConfiguration;

    @BeforeTest
    public void setUp() throws DataManagerConfigurationException {
        dataManagerConfiguration = new DataManagerConfiguration();
        dataManagerConfiguration.registerKey(
                "user",
                "test-profiling-line",
                "com.collective.profiler.data.UserTestDataSource"
        );
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

    @AfterTest
    public void tearDown() {
        dataManagerConfiguration = null;
    }

    @Test
    public void testConfiguration() throws IllegalAccessException,
            InstantiationException, DataSourceException {
        Class dataSourceClass = dataManagerConfiguration.getDataSource("user");
        DataSource dataSource = (DataSource) dataSourceClass.newInstance();
        dataSource.init(dataManagerConfiguration.getMessagesPersistenceConfiguration().getProperties());
        RawDataSet rawDataSet = dataSource.getRawData();
        Assert.assertNotNull(rawDataSet);
        Assert.assertEquals(rawDataSet.size(), 1);
        while(rawDataSet.hasNext()) {
            User user = (User) rawDataSet.getNext();
            Assert.assertNotNull(user);
            Assert.assertEquals(user.getInterests(), "google");
            Assert.assertEquals(user.getSkills(), "music");            
        }
        dataSource.dispose();
    }

}
