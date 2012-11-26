package com.collective.consumer.configuration;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.ConfigurationException;

import java.io.File;

/**
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ConfigurationManager {


    private static ConfigurationManager instance;

    private XMLConfiguration xmlConfiguration;

    private QueueGatewayConfiguration queueGatewayConfiguration;

    private MessagesPersistenceConfiguration messagesPersistenceConfiguration;

    private final static Logger logger = Logger.getLogger(ConfigurationManager.class);

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private ConfigurationManager(String configurationFilePath) {
        if(configurationFilePath == null)
            throw new IllegalArgumentException("Configuration file path cannot be null");

        File configurationFile = new File(configurationFilePath);
        if(!configurationFile.exists()) {
            logger.error("Configuration file: '" +
                    configurationFilePath + "' does not exists");
            throw new IllegalArgumentException("Configuration file: '" +
                    configurationFilePath + "' does not exists");
        }
        try {
            xmlConfiguration = new XMLConfiguration(configurationFile.getAbsolutePath());
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error while loading XMLConfiguration", e);
        }

        /* setup the configuration of message queue consumer and of the persistence */
        initQueueGateway();
        initMessagesPersistenceConfiguration();
    }

    private void initQueueGateway() {
        HierarchicalConfiguration messageConsumer = xmlConfiguration.configurationAt("message-queue");
        String host = messageConsumer.getString("host");
        String port = messageConsumer.getString("port");
        String queueName = messageConsumer.getString("queue-name");
        queueGatewayConfiguration = new QueueGatewayConfiguration(host, port, queueName);
    }

    private void initMessagesPersistenceConfiguration() {
        HierarchicalConfiguration messageConsumer = xmlConfiguration.configurationAt("storage");
        String host = messageConsumer.getString("host");
        int port = Integer.parseInt(messageConsumer.getString("port"));
        String database = messageConsumer.getString("database");
        String username = messageConsumer.getString("username");
        String password = messageConsumer.getString("password");
        messagesPersistenceConfiguration = new MessagesPersistenceConfiguration(host, port, database, username, password);
    }

    public QueueGatewayConfiguration getQueueGatewayConfiguration() {
        if (queueGatewayConfiguration == null)
            throw new IllegalStateException("Error: queueGatewayConfiguration is null");
        return queueGatewayConfiguration;
    }

    public MessagesPersistenceConfiguration getMessagesPersistenceConfiguration() {
        if (messagesPersistenceConfiguration == null)
            throw new IllegalStateException("Error: messagesPersistenceConfiguration is null");
        return messagesPersistenceConfiguration;
    }
}
