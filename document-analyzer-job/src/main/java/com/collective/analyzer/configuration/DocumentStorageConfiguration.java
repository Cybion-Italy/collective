package com.collective.analyzer.configuration;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DocumentStorageConfiguration {

    private String host;
    private String port;
    private String user;
    private String password;

    public DocumentStorageConfiguration(String host, String port, String user, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
