package com.collective.recommender.proxyimpl;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.recommender.SesameVirtuosoRecommenderTestCaseTurnedOff;
import com.collective.recommender.configuration.ConfigurationManager;
import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.SparqlProxy;
import com.collective.recommender.proxy.SparqlProxyException;
import com.collective.recommender.proxy.SparqlQuery;
import com.collective.recommender.proxyimpl.filtering.WebResourceEnhancedFilter;
import com.collective.recommender.proxyimpl.ranking.WebResourceEnhancedRanker;
import org.apache.log4j.Logger;
import org.nnsoft.be3.DefaultTypedBe3Impl;
import org.nnsoft.be3.typehandler.*;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.Sail;
import org.openrdf.sail.memory.MemoryStore;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SesameVirtuosoSparqlProxyTestCaseTurnedOff {

    private static final Logger LOGGER = Logger.getLogger(SesameVirtuosoRecommenderTestCaseTurnedOff.class);

    private final static String CONFIG_FILE = "src/test/resources/recommender-configuration.xml";

    public static final String OPERATING_SYSTEM = "http://dbpedia.org/resource/Operating_system";

    private String urlsGraphClass = "http://collective.com/resources/web/alternative";

    private SparqlProxy sparqlProxy;

    @BeforeTest
    public void setUp() throws RepositoryException, URISyntaxException, TypeHandlerRegistryException {

        RecommenderConfiguration recommenderConfiguration
                = ConfigurationManager.getInstance(CONFIG_FILE).getRecommenderConfiguration();

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
        sparqlProxy = new SesameVirtuosoSparqlProxy(recommenderConfiguration, typedRdfizer);
    }

    @AfterTest
    public void tearDown() {
        sparqlProxy = null;
    }

    @Test
    public void testGetList() throws SparqlProxyException {
        String queryTemplate = "CONSTRUCT {\n" +
                "    ?s ?p ?o.\n" +
                "    ?q ?r ?t.\n" +
                "}\n" +
                "FROM <%s>\n" +
                "WHERE {\n" +
                "    ?s a <http://collective.com/resources/web>.\n" +
                "    ?s <http://purl.org/dc/terms/subject> <%s>.\n" +
                "    ?s ?p ?o.\n" +
                "    ?s <http://collective.com/resources/web/hasSourceRSS> ?q.\n" +
                "    ?q ?r ?t.\n" +
                "}";
        String queryId = "resource-rec-query";
        sparqlProxy.registerQuery(
                queryId,
                queryTemplate,
                SparqlQuery.TYPE.GRAPH,
                WebResourceEnhancedFilter.class,
                WebResourceEnhancedRanker.class

        );
        List resources = sparqlProxy.getList(
                queryId,
                WebResourceEnhanced.class,
                this.urlsGraphClass,
                OPERATING_SYSTEM
        );
        assertNotNull(resources);
        LOGGER.info("found " + resources.size() + " about " + OPERATING_SYSTEM);
        assertTrue(resources.size() >= 9);
    }
}
