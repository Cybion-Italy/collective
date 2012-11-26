package com.collective.recommender.proxy;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.permanentsearch.model.LabelledURI;
import com.collective.recommender.RecommenderException;
import com.collective.recommender.SesameVirtuosoRecommender;
import com.collective.recommender.proxy.filtering.WebResourceEnhancedFilter;
import com.collective.recommender.proxy.ranking.WebResourceEnhancedRanker;
import com.collective.recommender.utils.DomainFixtures;
import org.apache.log4j.Logger;
import org.openrdf.model.Statement;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class SesameInMemorySparqlProxyTestCase
        extends InMemorySparqlProxyProviderTestCase {

    private static final Logger logger =
            Logger.getLogger(SesameInMemorySparqlProxyTestCase.class);

    //use the already declared query
    private static final String RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH =
            SesameVirtuosoRecommender.RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH;
    @Test
    public void shouldPrintContentInUserCustomConceptsGraph()
            throws SparqlProxyException {
        //print content
        List<Statement> statements =
                    super.sparqlProxy.getAllGraphStatements(
                            super.sparqlProxy.usersCustomAnnotationsTemplate +
                            super.userId);

        //print statements found in graph in memory repository
        logger.debug("triples found in graph '"
                + super.sparqlProxy.usersCustomAnnotationsTemplate + super.userId + "'");
        for (Statement stmt : statements) {
            logger.debug(stmt.toString());
        }
        //TODO (low) no asserts here, testing nothing but printing
    }

    @Test
    public void shouldRegisterCustomConceptsQueryAndCheckQueryResults()
            throws RecommenderException {
        //register custom query (act like a recommender that registers queries)
        try {
            this.sparqlProxy.registerQuery(
                    "resources-custom-concepts-to-permanent-search",
                    RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH,
                    SparqlProxy.TYPE.GRAPH,
                    WebResourceEnhancedFilter.class,
                    WebResourceEnhancedRanker.class
            );
        } catch (SparqlProxyException e) {
            throw new RuntimeException("Error while adding '"
                    + RESOURCES_CUSTOM_CONCEPTS_PERMANENT_SEARCH + "' query");
        }

        // build the user's custom concept
        // (should be the same as the one loaded in repository)
        LabelledURI labelledURI = getLabelledUri();

        //execute query, matching a single custom concept
        Set<WebResourceEnhanced> matchingWebResources = new HashSet<WebResourceEnhanced>();

        try {
            List resources = this.sparqlProxy.getList(
                    "resources-custom-concepts-to-permanent-search",
                    WebResourceEnhanced.class,
                    super.sparqlProxy.usersCustomAnnotationsTemplate + super.userId,
                    labelledURI.getUrl().toString()
            );
            matchingWebResources.addAll(resources);
        } catch (SparqlProxyException e) {
            throw new RecommenderException("", e);
        }

        //print results
        logger.debug("matched webResources: ");
        for (WebResourceEnhanced webResourceEnhanced : matchingWebResources) {
            logger.debug(webResourceEnhanced.toString());
        }

        Set<WebResourceEnhanced> expectedResourcesSet =
                new HashSet<WebResourceEnhanced>(super.savedResources);
        //check results
        Assert.assertEquals(matchingWebResources, expectedResourcesSet);
    }

    // should be the same as the one loaded in repository
    private LabelledURI getLabelledUri() {
        URI customConcept =
                DomainFixtures.getCustomConcept(super.userId,
                        DomainFixtures.LABEL_PREFIX + super.userId);
        LabelledURI labelledURI = new LabelledURI();
        labelledURI.setUrl(customConcept);
        labelledURI.setLabel(DomainFixtures.LABEL_PREFIX + super.userId);
        return labelledURI;
    }
}
