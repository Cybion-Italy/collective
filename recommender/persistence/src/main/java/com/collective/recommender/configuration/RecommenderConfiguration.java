package com.collective.recommender.configuration;

import java.net.URI;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class RecommenderConfiguration {

    private String password;
    
    private String user;

    private String host;
    
    private int port;

    // TODO put even the datagraph where to get projects: see SesameVirtuosoRecommender
    private URI dataGraph;

    private int recommendationsLimit;

    private Map<String, URI> indexes;

    public RecommenderConfiguration(
            String user,
            String password,
            String host,
            int port,
            URI dataGraph,
            int recommendationsLimit,
            Map<String, URI> indexesMap) {
        this.user = user;
        this.password = password;        
        this.host = host;
        this.port = port;
        this.dataGraph = dataGraph;
        this.recommendationsLimit = recommendationsLimit;
        this.indexes = indexesMap;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public URI getDataGraph() {
        return dataGraph;
    }

    public int getRecommendationsLimit() {
        return recommendationsLimit;
    }

    public Map<String, URI> getIndexes() {
        return indexes;
    }

    public URI getIndexGraphName(String name) {
        return indexes.get(name);
    }

}
