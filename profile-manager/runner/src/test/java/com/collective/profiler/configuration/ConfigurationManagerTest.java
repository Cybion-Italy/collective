package com.collective.profiler.configuration;

import com.collective.profiler.container.ProfilingLineContainer;
import com.collective.profiler.container.ProfilingLineContainerException;
import com.collective.profiler.data.DataManager;
import com.collective.profiler.data.DataManagerException;
import com.collective.profiler.line.ProfilingLine;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.storage.ProfileStore;
import com.collective.profiler.storage.ProfileStoreConfiguration;
import com.collective.profiler.storage.ProfileStoreException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reference test class for {@link com.collective.profiler.configuration.ConfigurationManager}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ConfigurationManagerTest {

    private final static String CONFIG_FILE = "src/test/resources/configuration.xml";

    private final static String PROFILING_LINE_NAME = "fake-test-profilingline";

    private final static String FIRST_PROFILING_LINE__ITEM_NAME = "first";

    private final static String SECOND_PROFILING_LINE__ITEM_NAME = "second";    

    private ConfigurationManager configurationManager;

    @BeforeTest
    public void setUp() {
        configurationManager = ConfigurationManager.getInstance(CONFIG_FILE);
    }

    @AfterTest
    public void tearDown() {
        configurationManager = null;
    }

    @Test
    public void testGetProfilingLineContainer() throws ProfilingLineContainerException {
        ProfilingLineContainer profilingLineContainer
                = configurationManager.getProfilingLineContainer();
        Assert.assertNotNull(profilingLineContainer);
        Assert.assertEquals(profilingLineContainer.getNumberOfProfilingLines(), 1);
        ProfilingLine profilingLine = profilingLineContainer.getProfilingLine(PROFILING_LINE_NAME);
        Assert.assertNotNull(profilingLine);
        Assert.assertEquals(profilingLine.getName(), PROFILING_LINE_NAME);
        ProfilingLineItem firstProfilingLineItem = profilingLine.getProfilingLineItem();
        Assert.assertNotNull(firstProfilingLineItem);
        Assert.assertEquals(firstProfilingLineItem.getName(), FIRST_PROFILING_LINE__ITEM_NAME);
        ProfilingLineItem secondProfilingLineItem = firstProfilingLineItem.getNextProfilingLineItem();
        Assert.assertNotNull(secondProfilingLineItem);
        Assert.assertEquals(secondProfilingLineItem.getName(), SECOND_PROFILING_LINE__ITEM_NAME);
    }

    @Test
    public void testGetProfileStore() throws ProfileStoreException {
        ProfileStore profileStore = configurationManager.getProfileStore();
        Assert.assertNotNull(profileStore);
        ProfileStoreConfiguration profileStoreConfiguration = profileStore.getConfiguration();
        Assert.assertNotNull(profileStoreConfiguration);
        profileStore.close();
    }

    @Test
    public void testGetDataManager() throws DataManagerException {
        DataManager dataManager = configurationManager.getDataManager();
        Assert.assertNotNull(dataManager);
        Assert.assertEquals(dataManager.getRegisteredKeys().keySet().size(), 1);
        Assert.assertTrue(dataManager.getRegisteredKeys().containsKey("project"));
        Assert.assertTrue(dataManager.getRegisteredKeys().get("project").contains(PROFILING_LINE_NAME));
    }

    @Test
    public void testGetRicherProfilingLineContainer() throws ProfileStoreException, URISyntaxException {
        ProfileStore profileStore = configurationManager.getProfileStore();
        Assert.assertNotNull(profileStore);
        ProfileStoreConfiguration profileStoreConfiguration = profileStore.getConfiguration();
        Assert.assertNotNull(profileStoreConfiguration);

        Map<String, URI> expectedNamespacesMap = new HashMap<String, URI>();
        expectedNamespacesMap.put("project", new URI("http://cybion.com/profiles/project/"));

        Map<String, URI> actualNameSpacesMap = profileStoreConfiguration.getNameSpacesConfiguration();
        for (String key : expectedNamespacesMap.keySet()) {
            Assert.assertTrue(expectedNamespacesMap.get(key).equals(actualNameSpacesMap.get(key)));
        }
        profileStore.close();
    }
}
