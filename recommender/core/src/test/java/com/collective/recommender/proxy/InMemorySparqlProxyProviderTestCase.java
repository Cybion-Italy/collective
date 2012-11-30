package com.collective.recommender.proxy;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.recommender.utils.DomainFixtures;
import org.apache.log4j.Logger;
import org.nnsoft.be3.DefaultTypedBe3Impl;
import org.nnsoft.be3.typehandler.*;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * This class sets up the in memory sparql proxy to be used by who extends
 * this class. Not loading configurations from real configuration parameters
 * speeds up testing and allows to test with no impact on real repositories
 * and no dependency on data.
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class InMemorySparqlProxyProviderTestCase {

    private static final Logger logger =
            Logger.getLogger(InMemorySparqlProxyProviderTestCase.class);

    protected SesameInMemorySparqlProxy sparqlProxy;

    protected String userId;

    protected List<WebResourceEnhanced> savedResources;

    @BeforeClass
    public void setUpConfig() throws RepositoryException,
            TypeHandlerRegistryException {

        logger.debug("initialising in memory stores");
        //for rdfizer
        Sail sailStack = new MemoryStore();
        Repository repository = new SailRepository(sailStack);
        repository.initialize();

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

        DefaultTypedBe3Impl typedRdfizer = new DefaultTypedBe3Impl(repository, typeHandlerRegistry);
        this.sparqlProxy = new SesameInMemorySparqlProxy(typedRdfizer);
        //prepare data for tests
        setUpCustomConceptsData();
    }

    private void setUpCustomConceptsData() {
        Integer amount = 2;
        userId = "5263462";
        Long userIdLong = Long.parseLong(userId);
        savedResources = DomainFixtures.getResourcesAmount(userId, amount);

        /* writes some custom concepts webresource annotation in repository */
        for (WebResourceEnhanced webResourceEnhanced : savedResources) {
            logger.debug("saving " + webResourceEnhanced.toString());
            try {
                sparqlProxy.storeUserWebResource(webResourceEnhanced, userIdLong);
            } catch (SparqlProxyException e) {
                logger.error("failed to save test data to in memory repository");
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public void tearDownConfig() {
        sparqlProxy = null;
        logger.debug("tore down in-memory stores");
    }

}
