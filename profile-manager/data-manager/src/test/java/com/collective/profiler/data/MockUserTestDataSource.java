package com.collective.profiler.data;

import com.collective.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * used for tests
 */
public class MockUserTestDataSource implements DataSource {

    private User user;

    public void init(Properties properties) throws DataSourceException {
        user = new User();
        user.setInterests("google");
        user.setSkills("music");
    }

    public void dispose() throws DataSourceException {
        user = null;
    }

    public void registerDataBaseProperties(Properties properties) {
        throw new UnsupportedOperationException();
    }

    public RawDataSet getRawData() throws DataSourceException {
        List<User> users = new ArrayList<User>();
        users.add(user);
        RawDataSet<User> rds = new RawDataSet<User>(users);
        return rds;
    }
}