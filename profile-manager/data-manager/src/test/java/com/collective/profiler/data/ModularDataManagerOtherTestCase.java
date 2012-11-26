package com.collective.profiler.data;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ModularDataManagerOtherTestCase {


    private static Logger logger = Logger.getLogger(ModularDataManagerTest.class);

    private DataManager dataManager;

    @BeforeTest
    public void setUp() throws DataManagerConfigurationException, DataManagerException {
        DataManagerConfiguration dataManagerConfiguration = new DataManagerConfiguration();
        /* users must go to 2 profiling lines */
        dataManagerConfiguration.registerKey(
                "user",
                "test-profiling-line",
                "com.collective.profiler.data.datasources.UserDataSource"
        );
        dataManagerConfiguration.registerKey(
                "user",
                "other-test-profiling-line",
                "com.collective.profiler.data.datasources.UserDataSource"
        );
        dataManagerConfiguration.registerKey(
                "project",
                "other-test-profiling-line",
                "com.collective.profiler.data.datasources.ProjectDataSource"
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
        dataManager = new ModularDataManager(dataManagerConfiguration);
    }

    @AfterTest
    public void tearDown() {
        dataManager = null;
    }

    @Test
    public void testGetProfilingLinesForAKey() throws DataManagerException {
        List<String> userProfilingLines = dataManager.getRegisteredKeys().get("user");
        List<String> projectProfilingLines = dataManager.getRegisteredKeys().get("project");

        List<String> expectedUserProfilingLines = new ArrayList<String>();
        List<String> expectedProjectProfilingLines = new ArrayList<String>();

        expectedUserProfilingLines.add("test-profiling-line");
        expectedUserProfilingLines.add("other-test-profiling-line");
        expectedProjectProfilingLines.add("other-test-profiling-line");

        Assert.assertTrue(expectedUserProfilingLines.equals(userProfilingLines));
        Assert.assertTrue(expectedProjectProfilingLines.equals(projectProfilingLines));
    }
}
