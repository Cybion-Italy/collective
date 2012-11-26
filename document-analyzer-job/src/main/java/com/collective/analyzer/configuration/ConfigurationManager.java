package com.collective.analyzer.configuration;

import com.collective.persistencewebresources.persistence.configuration.WebResourcesPersistenceConfiguration;
import com.collective.rdfizer.RDFizer;
import com.collective.rdfizer.TypedRDFizer;
import com.collective.rdfizer.typehandler.*;
import com.collective.analyzer.storage.DocumentStorage;
import com.collective.analyzer.storage.SesameVirtuosoDocumentStorage;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.openrdf.model.vocabulary.XMLSchema;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;
    private DocumentStorage documentStorage;
    private static Logger logger = Logger.getLogger(ConfigurationManager.class);
    private XMLConfiguration xmlConfiguration;
    private Long documentsToAnalyse;
    private WebResourcesPersistenceConfiguration webResourcePersistenceConfiguration;
    private Properties activityLogConfiguration;

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private ConfigurationManager(String filePath) {
        if(filePath == null)
            throw new IllegalArgumentException("Configuration file path cannot be null");

        logger.info("loading configuration from: " + filePath);

        File configurationFile = new File(filePath);
        if(!configurationFile.exists()) {
            logger.error("Configuration file: '" +
                    filePath + "' does not exists");
            throw new IllegalArgumentException("Configuration file: '" +
                    filePath + "' does not exists");
        }

        try {
            xmlConfiguration = new XMLConfiguration(configurationFile.getAbsolutePath());
        } catch (ConfigurationException e) {
            throw new RuntimeException("Error while loading XMLConfiguration", e);
        }

        initWebResourcePersistence();

        initDocumentsToAnalyze();

        try {
            initDocumentStorage();
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while initialising a ProfileStore for WebResources", e);
        }

        initActivityLoggerConfiguration();
    }

    private void initWebResourcePersistence() {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("webresources-source");
        String host = pstore.getString("host");
        int port = Integer.parseInt(pstore.getString("port"));
        String db = pstore.getString("database");
        String username = pstore.getString("username");
        String password = pstore.getString("password");
        logger.debug("web-resource db: " + host + "/" + db + ":" + port);
        webResourcePersistenceConfiguration =
                new WebResourcesPersistenceConfiguration(host, port, db, username, password);
    }

    private void initDocumentStorage() throws URISyntaxException {
        HierarchicalConfiguration profileStoreXmlConfiguration = xmlConfiguration.configurationAt("document-store");

        //parameter validity check
        if (profileStoreXmlConfiguration.getString("graph-name") == null)
            throw new InvalidParameterException("graph-name should not be null");

        if (profileStoreXmlConfiguration.getString("custom-annotations-template") == null)
            throw new InvalidParameterException("custom-annotations-template should not be null");

        String webResourcesGraphName = profileStoreXmlConfiguration.getString("graph-name");
        logger.debug("doc-graph-name: " + webResourcesGraphName);
        URI defaultGraph = new URI(webResourcesGraphName);

        String customAnnotationsGraphTemplate =
                profileStoreXmlConfiguration.getString("custom-annotations-template");
        logger.debug("custom-annotations-template: " + customAnnotationsGraphTemplate);
        URI customAnnotationsTemplate = new URI(customAnnotationsGraphTemplate);

        //init profile/document store with the graph to be populated
        HierarchicalConfiguration tripleStore = profileStoreXmlConfiguration.configurationAt("triple-store");
        String host = tripleStore.getString("host");
        String port = tripleStore.getString("port");
        String username = tripleStore.getString("username");
        String password = tripleStore.getString("password");

        DocumentStorageConfiguration documentStoreConfiguration =
                new DocumentStorageConfiguration(host, port, username, password);

        RDFizer rdFizer;
        try {
            rdFizer = getRDFizer();
        } catch (TypeHandlerRegistryException e) {
            throw new RuntimeException("Error while initializing the TypedRDFizer", e);
        }
        documentStorage =
                new SesameVirtuosoDocumentStorage(documentStoreConfiguration,
                                                  rdFizer,
                                                  defaultGraph,
                                                  customAnnotationsTemplate);
    }

    private void initDocumentsToAnalyze() {
        HierarchicalConfiguration docAnalyzer = xmlConfiguration.configurationAt("documents-to-analyze");
        String documentsToAnalyzeString = docAnalyzer.getString("");
        Long numberDocuments;
        try {
            numberDocuments = Long.parseLong(documentsToAnalyzeString);
        } catch (NumberFormatException nfe) {
            logger.error("error parsing long from string: " + nfe.getMessage());
            throw new NumberFormatException(nfe.getMessage());
        }
        if (numberDocuments <= 0) {
            throw new InvalidParameterException("numberDocuments should be greater than 0");
        }
        this.documentsToAnalyse = numberDocuments;
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

    public DocumentStorage getDocumentStorage() {
        if(documentStorage == null)
            throw new IllegalStateException("Error: documentStorage is null");
        return documentStorage;
    }

    public Long getDocumentsToAnalyze() {
        return documentsToAnalyse;
    }

    public WebResourcesPersistenceConfiguration getWebResourcesPersistenceConfiguration() {
        if (webResourcePersistenceConfiguration == null)
            throw new IllegalStateException("Error: webResourcesPersistenceConfiguration is null");
        return webResourcePersistenceConfiguration;
    }

    public RDFizer getRDFizer() throws TypeHandlerRegistryException {
        TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
        URIResourceTypeHandler uriResourceTypeHandler = new URIResourceTypeHandler();
        StringValueTypeHandler stringValueTypeHandler = new StringValueTypeHandler();
        IntegerValueTypeHandler integerValueTypeHandler = new IntegerValueTypeHandler();
        URLResourceTypeHandler urlResourceTypeHandler = new URLResourceTypeHandler();
        DateValueTypeHandler dateValueTypeHandler = new DateValueTypeHandler();
        LongValueTypeHandler longValueTypeHandler = new LongValueTypeHandler();
        typeHandlerRegistry.registerTypeHandler(uriResourceTypeHandler, java.net.URI.class, XMLSchema.ANYURI);
        typeHandlerRegistry.registerTypeHandler(stringValueTypeHandler, String.class, XMLSchema.STRING);
        typeHandlerRegistry.registerTypeHandler(integerValueTypeHandler, Integer.class, XMLSchema.INTEGER);
        typeHandlerRegistry.registerTypeHandler(integerValueTypeHandler, Integer.class, XMLSchema.INT);
        typeHandlerRegistry.registerTypeHandler(urlResourceTypeHandler, URL.class, XMLSchema.ANYURI);
        typeHandlerRegistry.registerTypeHandler(dateValueTypeHandler, Date.class, XMLSchema.DATE);
        typeHandlerRegistry.registerTypeHandler(longValueTypeHandler, Long.class, XMLSchema.LONG);
        return new TypedRDFizer(null, typeHandlerRegistry);
    }

    public Properties getActivityLogConfiguration() {
        return activityLogConfiguration;
    }
}
