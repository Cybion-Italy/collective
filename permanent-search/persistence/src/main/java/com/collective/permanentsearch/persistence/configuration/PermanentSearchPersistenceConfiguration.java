package com.collective.permanentsearch.persistence.configuration;

import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class PermanentSearchPersistenceConfiguration {

    private String host;

    private int port;

    private String db;

    private String username;

    private String password;

    public PermanentSearchPersistenceConfiguration(String host, int port,
                                                   String db,
                                                   String username,
                                                   String password) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.username = username;
        this.password = password;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://"
                + host + ":" + port + "/" + db);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        return properties;
    }
}
