package com.collective.profiler.configuration;

import com.collective.profiler.Profiler;
import com.collective.profiler.ProfilerException;
import com.collective.profiler.container.ProfilingLineContainer;
import com.collective.profiler.data.DataManager;
import com.collective.profiler.data.DataManagerException;
import com.collective.profiler.storage.ProfileStore;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ProfilerTest {

    private final static String CONFIG_FILE = "profiler-runner-configuration.xml";

    private static Logger logger = Logger.getLogger(ProfilerTest.class);

    private ConfigurationManager configurationManager;

    @BeforeTest
    public void setUp() {
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
    }

    @AfterTest
    public void tearDown() {
        configurationManager = null;
    }

    @Test(enabled =  false)
    public void testRunner() throws DataManagerException, ProfilerException {
        ProfilingLineContainer profilingLineContainer = configurationManager.getProfilingLineContainer();
        logger.info("ProfilingLineContainer properly instantiated with the following "
                + profilingLineContainer.getNumberOfProfilingLines() +  " lines: '"
                + profilingLineContainer.getProfilingLineNames() + "'");

        DataManager dataManager = configurationManager.getDataManager();
        logger.info("DataManager properly instantiated.");
        ProfileStore profileStore = configurationManager.getProfileStore();
        logger.info("DataManager properly instantiated.");

        Profiler profiler = new Profiler(dataManager, profilingLineContainer, profileStore, null);
        profiler.run();
    }

}
