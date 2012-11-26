package com.collective.profiler.data;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import com.collective.model.Project;
import com.collective.model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ModularDataManagerTest {

    private static Logger logger = Logger.getLogger(ModularDataManagerTest.class);

    private DataManager dataManager;

    @BeforeTest
    public void setUp() throws DataManagerConfigurationException, DataManagerException {
        DataManagerConfiguration dataManagerConfiguration = new DataManagerConfiguration();
        dataManagerConfiguration.registerKey(
                "user",
                "test-profiling-line",
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
    public void testGetUserRawData() throws DataManagerException {
        RawDataSet<User> userRawDataSet =
                dataManager.getRawData("user");
        Assert.assertNotNull(userRawDataSet);
        //TODO: change, it depends on database state
        Assert.assertTrue(userRawDataSet.size() >= 0);
        while(userRawDataSet.hasNext()) {
            User user = userRawDataSet.getNext();
            Assert.assertNotNull(user);
            logger.info("Retrieved user: '" + user + "'");
        }
    }

    @Test
    public void testGetProjectRawData() throws DataManagerException {
        RawDataSet<Project> projectRawDataSet =
                dataManager.getRawData("project");
        Assert.assertNotNull(projectRawDataSet);
        //TODO: change, it depends on database state
        Assert.assertTrue(projectRawDataSet.size() >= 0);
        while(projectRawDataSet.hasNext()) {
            Project project = projectRawDataSet.getNext();
            Assert.assertNotNull(project);
            logger.info("Retrieved project: '" + project + "'");
        }
    }

    @Test
    public void testGetProfilingLinesForAKey() throws DataManagerException {
        List<String> userProfilingLines = dataManager.getRegisteredKeys().get("user");
        List<String> projectProfilingLines = dataManager.getRegisteredKeys().get("project");

        List<String> expectedUserProfilingLines = new ArrayList<String>();
        List<String> expectedProjectProfilingLines = new ArrayList<String>();

        expectedUserProfilingLines.add("test-profiling-line");
        expectedProjectProfilingLines.add("other-test-profiling-line");

        Assert.assertTrue(expectedUserProfilingLines.equals(userProfilingLines));
        Assert.assertTrue(expectedProjectProfilingLines.equals(projectProfilingLines));
    }
}
