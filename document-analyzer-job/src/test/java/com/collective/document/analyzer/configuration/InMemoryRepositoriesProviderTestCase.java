package com.collective.document.analyzer.configuration;

import com.collective.analyzer.configuration.ConfigurationManager;
import com.collective.concepts.core.UserDefinedConceptStoreException;
import com.collective.document.analyzer.storage.memory.InMemoryDocumentStorage;
import com.collective.document.analyzer.storage.memory.InMemoryUserDefinedConceptStore;
import com.collective.document.analyzer.storage.memory.InMemoryWebResourceRepository;
import com.collective.rdfizer.RDFizer;
import com.collective.rdfizer.typehandler.TypeHandlerRegistryException;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class sets up all the in memory repositories to be used by who extends
 * this class. Loads in memory repositories for all storage needed,
 * not loading configurations from real configuration parameters
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class InMemoryRepositoriesProviderTestCase {

    private static final Logger logger =
            Logger.getLogger(ConfigurationManagerTestCase.class);
    private static final String confFilePath = "configuration.xml";
    protected ConfigurationManager configurationManager;
    protected InMemoryDocumentStorage documentStore;
    protected InMemoryWebResourceRepository resourcesRepository;
    protected InMemoryUserDefinedConceptStore userDefinedConceptStore;
    protected Long documentsToAnalyze = 3L;
    protected static final String DEFAULT_GRAPH =
            "http://collective.com/resources/TEST/web/alternative";
    protected static final String customAnnotationsTemplate =
            "http://collective.com/annotation/user/";

    @BeforeClass
    public void setUpConfig() throws TypeHandlerRegistryException,
            URISyntaxException {

        URI defaultGraph = null;
        try {
            defaultGraph = new URI(DEFAULT_GRAPH);
        } catch (URISyntaxException e) {
            //should never happen
        }

        //used just to load the RDFizer, even if using a valid configuration,
        //because other objects are created ad hoc
        logger.info("Loading configuration from: '" + confFilePath + "'");
        ConfigurationManager configurationManager =
                ConfigurationManager.getInstance(confFilePath);
        RDFizer rdfizer = configurationManager.getRDFizer();

        URI customAnnotationsTemplateUri = new URI(customAnnotationsTemplate);
        documentStore = new InMemoryDocumentStorage(rdfizer, defaultGraph,
                                                  customAnnotationsTemplateUri);
        resourcesRepository     = new InMemoryWebResourceRepository();
        userDefinedConceptStore = new InMemoryUserDefinedConceptStore();
        populateDocumentStorageWithTestData();
    }

    private void populateDocumentStorageWithTestData() {
        /* build a user defined concept */
        Long userId = 1L;
        com.collective.concepts.core.Concept concept =
                new com.collective.concepts.core.Concept(
                        "company",
                        "owner",
                        "name",
                        "label");

        concept.addKeyword("keyword");

        //fill the conceptStore with one concept
        try {
            userDefinedConceptStore.storeConcept(userId, concept);
        } catch (UserDefinedConceptStoreException e) {
            //should never happen
            e.printStackTrace();
        }
    }

    @AfterClass
    public void tearDownConfig() {
        configurationManager = null;
        documentStore = null;
        resourcesRepository = null;
        userDefinedConceptStore = null;
        logger.info("ended test");
    }
}
