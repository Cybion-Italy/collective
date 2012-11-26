package com.collective.profiler.configuration;

import com.collective.messages.persistence.configuration.MessagesPersistenceConfiguration;
import com.collective.profiler.container.DefaultProfilingLineContainer;
import com.collective.profiler.container.ProfilingLineContainer;
import com.collective.profiler.container.ProfilingLineContainerException;
import com.collective.profiler.data.*;
import com.collective.profiler.line.ProfilingLine;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.storage.ProfileStore;

import com.collective.profiler.storage.ProfileStoreConfiguration;
import com.collective.profiler.storage.SesameVirtuosoProfileStore;
import com.collective.rdfizer.RDFizer;
import com.collective.rdfizer.TypedRDFizer;
import com.collective.rdfizer.typehandler.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.openrdf.model.vocabulary.XMLSchema;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class ConfigurationManager {

    private static ConfigurationManager instance;

    public static ConfigurationManager getInstance(String filePath) {
        if(instance == null)
            instance = new ConfigurationManager(filePath);
        return instance;
    }

    private static Logger logger = Logger.getLogger(ConfigurationManager.class);

    private DataManager dataManager;

    private ProfilingLineContainer profilingLineContainer;

    private ProfileStore profileStore;

    private XMLConfiguration xmlConfiguration;

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
            initProfilingLineContainer();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error while instantiating the proper ProfilingLine", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error while instantiating the proper ProfilingLine", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error while instantiating the proper ProfilingLine", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error while instantiating the proper ProfilingLine", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Profiling line not found", e);
        } catch (ProfilingLineContainerException e) {
            throw new RuntimeException("Error while adding a ProfilingLine to the container", e);
        }
        try {
            initProfileStore();
        } catch (URISyntaxException e) {
            throw new RuntimeException("error while initialising a ProfileStore", e);
        }
        try {
            initDataManager();
        } catch (DataManagerConfigurationException e) {
            throw new RuntimeException("Error while instantiating DataManager", e);
        } catch (DataManagerException e) {
            throw new RuntimeException("Error while instantiating DataManager", e);
        }
        try {
            checkKeysIntegrity();
        } catch (DataManagerException e) {
            throw new RuntimeException("Error while checking keys integrity", e);
        }

    }

    private void checkKeysIntegrity() throws DataManagerException {
        ProfileStoreConfiguration profileStoreConfiguration =
                profileStore.getConfiguration();
        Map<String, List<String>> registeredKeys
                = dataManager.getRegisteredKeys();
        Set<String> psKeys = profileStoreConfiguration.getNameSpacesConfiguration().keySet();
        Set<String> dmKeys =  registeredKeys.keySet();
        if(psKeys.size() != dmKeys.size()) {
            throw new RuntimeException("Error: keys declared for Data Manager are not" +
                    "the same number of the ones declared by the ProfileStore");
        }
        if(!psKeys.equals(dmKeys)) {
            throw new RuntimeException("Error: keys declared for Data Manager are not" +
                    "the same");
        }
    }

    private void initProfileStore() throws URISyntaxException {
        HierarchicalConfiguration pstore = xmlConfiguration.configurationAt("profile-store");
        Map<String, URI> nameSpacesConfiguration = new HashMap<String, URI>();

        HierarchicalConfiguration tripleStore = pstore.configurationAt("triple-store");
        String host = tripleStore.getString("host");
        String port = tripleStore.getString("port");
        String username = tripleStore.getString("username");
        String password = tripleStore.getString("password");

        List<HierarchicalConfiguration> keys = pstore.configurationsAt("keys.key");
        for(HierarchicalConfiguration profileStore : keys) {
            String profileStoreName = profileStore.getString("[@name]");
            String profileStoreUri = profileStore.getString("");
            if(profileStoreUri == null || profileStoreUri.equals("")) {
                throw new RuntimeException("URIs for key '"  + profileStoreName + "'cannot be missing ");
            }
            if(profileStoreUri.charAt(profileStoreUri.length() -1) != '/') {
                throw new RuntimeException("URIs should terminate with a slash. It seems it is missing ");
            }
            try {
                nameSpacesConfiguration.put(profileStoreName, new URI(profileStoreUri));
            } catch (URISyntaxException e) {
                final String errorMsg = "used wrong uri to define profileStoreUri: " + profileStoreUri;
                logger.error(errorMsg);
                throw new URISyntaxException(errorMsg, "");
            }
        }

        ProfileStoreConfiguration profileStoreConfiguration =
                new ProfileStoreConfiguration(host, port, username, password, nameSpacesConfiguration);
        RDFizer rdFizer;
        try {
            rdFizer = getRDFizer();
        } catch (TypeHandlerRegistryException e) {
            throw new RuntimeException("Error while initializing the TypedRDFizer", e);
        }
        profileStore = new SesameVirtuosoProfileStore(profileStoreConfiguration, rdFizer);
    }

    private RDFizer getRDFizer() throws TypeHandlerRegistryException {
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

    private void initDataManager() throws DataManagerConfigurationException, DataManagerException {
        HierarchicalConfiguration dmanager = xmlConfiguration.configurationAt("data-manager");
        List<HierarchicalConfiguration> keys = dmanager.configurationsAt("keys.key");
        DataManagerConfiguration dataManagerConfiguration = new DataManagerConfiguration();
        for(HierarchicalConfiguration key : keys) {
            String keyString = key.getString("[@name]");
            String dataSourceClassName = key.getString("[@class]");
            List<HierarchicalConfiguration> lines = key.configurationsAt("lines");
            for(HierarchicalConfiguration line : lines) {
                String lineNameString = line.getString("line");
                dataManagerConfiguration.registerKey(keyString, lineNameString, dataSourceClassName);
            }
        }
        HierarchicalConfiguration database = dmanager.configurationAt("database");
        String host = database.getString("host");
        int port = database.getInt("port");
        String db = database.getString("db");
        String username = database.getString("username");
        String password = database.getString("password");

        MessagesPersistenceConfiguration messagesPersistenceConfiguration =
                new MessagesPersistenceConfiguration( host, port, db, username, password);

        /* use this to access messages database */
        dataManagerConfiguration.setMessagesPersistenceConfiguration(messagesPersistenceConfiguration);

        this.dataManager = new ModularDataManager(dataManagerConfiguration);
    }

    private void initProfilingLineContainer() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ClassNotFoundException, ProfilingLineContainerException {
        profilingLineContainer = new DefaultProfilingLineContainer();
        List<HierarchicalConfiguration> plines =
                xmlConfiguration.configurationsAt("profiling-lines.profiling-line");
        for(HierarchicalConfiguration pline : plines) {
            String name = pline.getString("name");
            String description = pline.getString("description");
            String clazz = pline.getString("class");
            List<HierarchicalConfiguration> plineItems =
                    pline.configurationsAt("profiling-line-items.profiling-line-item");
            ProfilingLine profilingLine = newProfilingLineInstance(clazz, name, description);
            for(HierarchicalConfiguration plineItem : plineItems) {
                String itemName = plineItem.getString("name");
                String itemDescription = plineItem.getString("description");
                String itemClazz = plineItem.getString("class");
                profilingLine.enqueueProfilingLineItem(
                        newProfilingLineItemInstance(
                                itemClazz,
                                itemName,
                                itemDescription
                        )
                );
            }
            profilingLineContainer.addProfilingLine(profilingLine);
        }

    }

    public ProfilingLineContainer getProfilingLineContainer() {
        if(profilingLineContainer == null)
            throw new IllegalStateException("Error: profilingLineContainer is null");
        return profilingLineContainer;
    }

    public DataManager getDataManager() {
        if(dataManager == null)
            throw new IllegalStateException("Error: dataManager is null");
        return dataManager;
    }

    public ProfileStore getProfileStore() {
        if(profileStore == null)
            throw new IllegalStateException("Error: profileStore is null");
        return profileStore;
    }

    private ProfilingLineItem newProfilingLineItemInstance(String className, String name, String description)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class clazz = this.getClass().getClassLoader().loadClass(className);
        Constructor constructor = clazz.getConstructor(String.class, String.class);
        return (ProfilingLineItem) constructor.newInstance(name, description);
    }

    private ProfilingLine newProfilingLineInstance(String className, String name, String description)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class clazz = this.getClass().getClassLoader().loadClass(className);
        Constructor constructor = clazz.getConstructor(String.class, String.class);
        return (ProfilingLine) constructor.newInstance(name, description);
    }

}
