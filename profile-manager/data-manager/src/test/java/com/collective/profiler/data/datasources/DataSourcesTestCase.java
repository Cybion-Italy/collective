package com.collective.profiler.data.datasources;

import com.collective.model.Project;
import com.collective.model.User;
import com.collective.model.profile.SearchProfile;
import com.collective.profiler.data.DataSourceException;
import com.collective.profiler.data.RawDataSet;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DataSourcesTestCase extends AbstractDataSourceTestCase {

    private UserDataSource userDataSource;

    private ProjectDataSource projectDataSource;

    public DataSourcesTestCase() {
        super();
    }

    @BeforeTest
    public void setUp() throws DataSourceException {
        userDataSource = new UserDataSource();
        userDataSource.init(dataManagerConfiguration.getMessagesPersistenceConfiguration().getProperties());

        projectDataSource = new ProjectDataSource();
        projectDataSource.init(dataManagerConfiguration.getMessagesPersistenceConfiguration().getProperties());
    }

    @AfterTest
    public void tearDown() throws DataSourceException {
        userDataSource.dispose();
        userDataSource = null;
        projectDataSource.dispose();
        projectDataSource = null;
    }

    @Test
    public void testGetRawData() throws DataSourceException {
        RawDataSet<User> userRawDataSet = userDataSource.getRawData();
        Assert.assertNotNull(userRawDataSet);

        RawDataSet<Project> projectRawDataSet = projectDataSource.getRawData();
        Assert.assertNotNull(projectRawDataSet);
    }

}
