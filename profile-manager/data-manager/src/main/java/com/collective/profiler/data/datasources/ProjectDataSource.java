package com.collective.profiler.data.datasources;

import com.collective.model.Project;
import com.collective.profiler.data.DataSource;
import com.collective.profiler.data.DataSourceException;
import com.collective.profiler.data.RawDataSet;
import com.collective.profiler.data.datasources.messaging.MessagesRepository;
import com.collective.profiler.data.datasources.messaging.MessagesRepositoryException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class ProjectDataSource implements DataSource {

    private static Logger logger = Logger.getLogger(ProjectDataSource.class);

    private MessagesRepository messagesRepository;

    public void init(Properties properties) throws DataSourceException {
        messagesRepository = MessagesRepository.getInstance(properties);
    }

    public void dispose() throws DataSourceException {
        messagesRepository = null;
    }

    public RawDataSet getRawData() throws DataSourceException {
        List<Project> projects;
        try {
            projects = messagesRepository.getProjectsToBeProfiled();
        } catch (MessagesRepositoryException e) {
            final String errMsg = "Error while getting projects from the db";
            logger.error(errMsg, e);
            throw new DataSourceException(errMsg, e);
        }
        return new RawDataSet<Project>(projects);
    }
}
