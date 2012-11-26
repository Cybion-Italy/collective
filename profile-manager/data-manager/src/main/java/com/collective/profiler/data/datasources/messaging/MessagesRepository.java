package com.collective.profiler.data.datasources.messaging;

import com.collective.messages.persistence.dao.MessageDao;
import com.collective.messages.persistence.model.Message;
import com.collective.model.Project;
import com.collective.model.User;
import com.collective.model.profile.SearchProfile;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MessagesRepository {

    // TODO (med) should be included in the messages persistence configuration
    private static int MAX_MESSAGES_AMOUNT = 10;

    private static MessagesRepository instance;

    public static MessagesRepository getInstance(Properties properties) {
        if(instance == null) {
            instance = new MessagesRepository(properties);
        }
        return instance;
    }

    private MessageDao messageDao;

    //TODO check if gson deserializer should be configured
    private Gson gson = new Gson();

    private MessagesRepository(Properties properties) {
        messageDao = new MessageDao(properties);
    }

    public List<User> getUsersToBeProfiled() 
            throws MessagesRepositoryException {
        Message query = new Message();
        query.setType("user");
        query.setAnalyzed(false);
        List<Message> messages = messageDao.selectLastMessagesByExample(query, MAX_MESSAGES_AMOUNT);
        List<User> users = new ArrayList<User>();
        for(Message message : messages) {
            String json = message.getObject();
            users.add(gson.fromJson(json, User.class));
            message.setAnalyzed(true);
            // message has been processed, hence mark it as analyzed
            messageDao.updateMessage(message);
        }
        return users;
    }

    public List<Project> getProjectsToBeProfiled()
            throws MessagesRepositoryException {
        Message query = new Message();
        query.setType("project");
        query.setAnalyzed(false);        
        List<Message> messages = messageDao.selectLastMessagesByExample(query, MAX_MESSAGES_AMOUNT);
        List<Project> projects = new ArrayList<Project>();
        for(Message message : messages) {
            String json = message.getObject();
            projects.add(gson.fromJson(json, Project.class));
            message.setAnalyzed(true);
            // message has been processed, hence mark it as analyzed
            messageDao.updateMessage(message);
        }
        return projects;
    }
}
