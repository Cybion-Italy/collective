package com.collective.recommender.configuration;

import com.collective.profiler.storage.ProfileStoreConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
/* TODO (high): change test run configuration, since it doesn't run when called from parent module */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private XMLConfiguration xmlConfiguration;

    private RecommenderConfiguration recommenderConfiguration;

    private RecommendationsStorageConfiguration recommendationsStorageConfiguration;

    private ProfileStoreConfiguration profileStoreConfiguration;

    private PermanentSearchStorageConfiguration permanentSearchStorageConfiguration;

    private Properties activityLogConfiguration;

    private CategoriesMappingStorageConfiguration categoriesMappingStorageStorageConfiguration;

    private static Logger logger = Logger.getLogger(ConfigurationManager.class);

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
        try {
            initRecommenderConfiguration();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error while reading configuration", e);
        }
        initRecommenderStorageConfiguration();
        initProfileStore();
        initPermanentSearchStorageConfiguration();
        initActivityLoggerConfiguration();
        initCategoriesMappingStorageConfiguration();
    }

    private void initCategoriesMappingStorageConfiguration() {
        //TODO
        HierarchicalConfiguration permanentSearchConfiguration =
                xmlConfiguration.configurationAt("categories-mapping");
        String host = permanentSearchConfiguration.getString("host");
        int port = permanentSearchConfiguration.getInt("port");
        String db = permanentSearchConfiguration.getString("db");
        String username = permanentSearchConfiguration.getString("username");
        String password = permanentSearchConfiguration.getString("password");
        CategoriesMappingStorageConfiguration categoriesMappingStorageStorageConfiguration =
                new CategoriesMappingStorageConfiguration(host, port, db, username, password);

        this.categoriesMappingStorageStorageConfiguration = categoriesMappingStorageStorageConfiguration;
    }

    private void initPermanentSearchStorageConfiguration() {
        HierarchicalConfiguration permanentSearchConfiguration =
                xmlConfiguration.configurationAt("permanent-search");
        String host = permanentSearchConfiguration.getString("host");
        int port = permanentSearchConfiguration.getInt("port");
        String db = permanentSearchConfiguration.getString("db");
        String username = permanentSearchConfiguration.getString("username");
        String password = permanentSearchConfiguration.getString("password");
        permanentSearchStorageConfiguration =
                new PermanentSearchStorageConfiguration(host, port, db, username, password);
    }

    private void initRecommenderConfiguration() throws URISyntaxException {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("recommender");
        String host = pstore.getString("host");
        int port = pstore.getInt("port");
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        String graphData = pstore.getString("graph");
        int limit = pstore.getInt("limit");
        HierarchicalConfiguration indexes = pstore.configurationAt("indexes");
        List<HierarchicalConfiguration> indexesList = indexes.configurationsAt("index");
        Map<String, URI> indexesMap = new HashMap<String, URI>();
        for (HierarchicalConfiguration index : indexesList) {
            indexesMap.put(index.getString("name"), new URI(index.getString("graph")));
        }
        recommenderConfiguration =
                new RecommenderConfiguration(username, password, host, port, new URI(graphData), limit, indexesMap);
    }

    private void initRecommenderStorageConfiguration() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("storage");
        String host = pstore.getString("host");
        int port = pstore.getInt("port");
        String db = pstore.getString("db");        
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        recommendationsStorageConfiguration =
                new RecommendationsStorageConfiguration(host, port, db, username, password);
    }

    private void initProfileStore() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("profile-store");
        String host = pstore.getString("host");
        String port = pstore.getString("port");
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        profileStoreConfiguration =
                new ProfileStoreConfiguration(host, port, username, password);
    }

    private void initActivityLoggerConfiguration(){
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("logger");
        String host = pstore.getString("host");
        int port = pstore.getInt("port");
        String db = pstore.getString("db");
        String username = pstore.getString("username");
        String password = pstore.getString("password");

        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://" + host + ":" + port + "/" + db);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        activityLogConfiguration = properties;
    }


    public RecommenderConfiguration getRecommenderConfiguration() {
        return recommenderConfiguration;
    }

    public RecommendationsStorageConfiguration getRecommendationsStorageConfiguration() {
        return recommendationsStorageConfiguration;
    }

    public ProfileStoreConfiguration getProfileStoreConfiguration() {
        return profileStoreConfiguration;
    }

    public PermanentSearchStorageConfiguration getPermanentSearchStorageConfiguration() {
        return permanentSearchStorageConfiguration;
    }

    public Properties getActivityLogConfiguration() {
        return activityLogConfiguration;
    }

    public CategoriesMappingStorageConfiguration getCategoriesMappingStorageStorageConfiguration() {
        return categoriesMappingStorageStorageConfiguration;
    }
}
