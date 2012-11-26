package com.collective.persistencewebresources.persistence.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    private XMLConfiguration xmlConfiguration;

    private WebResourcesPersistenceConfiguration webResourcePersistenceConfiguration;

    private static final Logger logger = Logger.getLogger(ConfigurationManager.class);

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
        initWebResourcePersistence();
    }

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private void initWebResourcePersistence() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("storage");
        String host = pstore.getString("host");
        int port = Integer.parseInt(pstore.getString("port"));
        String db = pstore.getString("database");
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        webResourcePersistenceConfiguration =
                new WebResourcesPersistenceConfiguration(host, port, db, username, password);
    }

    public WebResourcesPersistenceConfiguration getWebResourcePersistenceConfiguration() {
        return webResourcePersistenceConfiguration;
    }
}
