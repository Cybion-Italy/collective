package com.collective.profiler.data.datasources.messaging;

import com.collective.model.Project;
import com.collective.model.User;
import com.collective.model.profile.SearchProfile;
import com.collective.profiler.data.datasources.AbstractDataSourceTestCase;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MessagesRepositoryTestCase extends AbstractDataSourceTestCase {

    private MessagesRepository messagesRepository;
    private final static Logger logger = Logger.getLogger(MessagesRepositoryTestCase.class);

    public MessagesRepositoryTestCase() {
        super();
    }


    @BeforeClass
    public void setUp(){
        messagesRepository = MessagesRepository.getInstance(dataManagerConfiguration.getMessagesPersistenceConfiguration().getProperties());
    }

    @AfterClass
    public void tearDown() {
        messagesRepository = null;
    }

    @Test
    public void shouldGetUsers() throws MessagesRepositoryException {
        List<User> users = messagesRepository.getUsersToBeProfiled();
        Assert.assertNotNull(users);
    }

    @Test
    public void shouldGetProjects() throws MessagesRepositoryException {
        List<Project> projects = messagesRepository.getProjectsToBeProfiled();
        Assert.assertNotNull(projects);
    }
}
