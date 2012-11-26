package com.collective.persistencewebresources.persistence.dao;

import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class ConfigurableDao {

    protected Properties properties;

    public ConfigurableDao(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}
