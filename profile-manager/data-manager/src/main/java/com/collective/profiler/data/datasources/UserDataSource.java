package com.collective.profiler.data.datasources;

import com.collective.model.User;
import com.collective.profiler.data.DataSource;
import com.collective.profiler.data.DataSourceException;
import com.collective.profiler.data.RawDataSet;
import com.collective.profiler.data.datasources.messaging.MessagesRepository;
import com.collective.profiler.data.datasources.messaging.MessagesRepositoryException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class UserDataSource implements DataSource {

    private static Logger logger = Logger.getLogger(UserDataSource.class);

    private MessagesRepository messagesRepository;


    public void init(Properties properties) throws DataSourceException {
        messagesRepository = MessagesRepository.getInstance(properties);
    }

    public void dispose() throws DataSourceException {
        logger.info("disposing");
        messagesRepository = null;
    }

    public RawDataSet getRawData() throws DataSourceException {
        List<User> users;
        try {
            users = messagesRepository.getUsersToBeProfiled();
        } catch (MessagesRepositoryException e) {
            final String errMsg = "Error while getting users from the Messages repository";
            logger.error(errMsg, e);
            throw new DataSourceException(errMsg, e);
        }
        return new RawDataSet<User>(users);
    }
}
