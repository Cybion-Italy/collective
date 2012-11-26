package com.collective.profiler.storage;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This class wraps a configuration for a
 * {@link com.collective.profiler.storage.ProfileStore}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public final class ProfileStoreConfiguration {

    private String host;

    private String port;

    private String user;

    private String password;

    private Map<String, URI> nameSpacesConfiguration = new HashMap<String, URI>();

    @Deprecated
    public ProfileStoreConfiguration(String host, String port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public ProfileStoreConfiguration(String host, String port, String user, String password,
                                     Map<String, URI> nameSpacesConfiguration) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
         if(nameSpacesConfiguration == null || nameSpacesConfiguration.size() == 0) {
            throw new IllegalArgumentException("Namespace configurations, could not be null or missing");
        }
        this.setNameSpacesConfiguration(nameSpacesConfiguration);
    }


    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, URI> getNameSpacesConfiguration() {
        return nameSpacesConfiguration;
    }

    public void setNameSpacesConfiguration(Map<String, URI> nameSpacesConfiguration) {
        if(nameSpacesConfiguration == null || nameSpacesConfiguration.size() == 0) {
            throw new IllegalArgumentException("Namespace configurations, could not be null or missing");
        }
        this.nameSpacesConfiguration = nameSpacesConfiguration;
    }
}
