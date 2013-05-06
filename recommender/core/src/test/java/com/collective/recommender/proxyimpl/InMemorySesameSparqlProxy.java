package com.collective.recommender.proxyimpl;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.recommender.proxy.SparqlProxyException;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;
import org.nnsoft.be3.RDFizerException;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The in memory repository has more features and responsibilities,
 * only in order to enable easier testing (write graphs, delete graphs etc... )
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class InMemorySesameSparqlProxy extends SesameVirtuosoSparqlProxy {

    private static final Logger LOGGER = Logger.getLogger(InMemorySesameSparqlProxy.class);

    public InMemorySesameSparqlProxy(Be3 rdFizer) {

        //init in memory repository
        this.repository = new SailRepository(new MemoryStore());

        //this will be taken from configuration
        try {
            super.usersCustomAnnotationsTemplate =
                    new URI("http://collective.com/annotation/user/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            repository.initialize();
            LOGGER.info("in memory repository connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing connection to in memory repository";
            LOGGER.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        this.rdfizer = rdFizer;
    }

    public List<Statement> getAllGraphStatements(String graphName)
            throws SparqlProxyException {
        RepositoryConnection repositoryConnection = super.getConnection();
        List<Statement> statements = new ArrayList<Statement>();

        String constructGraphQuery = "CONSTRUCT { ?s ?p ?o } " +
                                     "WHERE {" +
                                     "GRAPH <" + graphName + "> " +
                                     "{ ?s ?p ?o } . }";

        try {
            GraphQueryResult graphResult =
                    repositoryConnection.prepareGraphQuery(
            QueryLanguage.SPARQL, constructGraphQuery).evaluate();

            while (graphResult.hasNext()) {
                Statement st = graphResult.next();
                statements.add(st);
            }

        } catch (RepositoryException e) {
            final String errMsg = "Error while getting all tuples from graph '"
                    + graphName + "'";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (QueryEvaluationException e) {
            final String errMsg = "Error while evaluating query: '"
                    + constructGraphQuery + "'";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (MalformedQueryException e) {
            final String errMsg = "Malformed query: '"
                    + constructGraphQuery + "'";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } finally {
            super.releaseConnection(repositoryConnection);
        }

        return statements;
    }

    /* stores the Webresources and its custom concepts annotations
     * in user named graph */
    public void storeUserWebResource(
            WebResourceEnhanced webResourceEnhanced,
            Long userId
    ) throws SparqlProxyException {
        RepositoryConnection repositoryConnection = super.getConnection();
        List<Statement> statements;
        try {
            statements = rdfizer.getRDFStatements(webResourceEnhanced);
        } catch (RDFizerException e) {
            final String errMsg = "Error while serializing enhancedWebResource: '"
                    + webResourceEnhanced + "'";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        for (Statement statement : statements) {
            try {
                repositoryConnection.add(statement,
                        new URIImpl(usersCustomAnnotationsTemplate.toString() + userId));
            } catch (RepositoryException e) {
                final String errMsg = "Error while adding RDF statement: '"
                        + statement + "' when saving enhancedWebResource: '"
                        + webResourceEnhanced + "'";
                LOGGER.debug(errMsg, e);
                continue;
            }
        }
        try {
            repositoryConnection.commit();
        } catch (RepositoryException e) {
            final String errMsg = "Error while committing enhancedWebResource: '"
                    + webResourceEnhanced + "' on Virtuoso";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        LOGGER.info("enhancedWebResource: '" + webResourceEnhanced +
                    "' successfully stored on graph: '" + usersCustomAnnotationsTemplate + userId +
                    "'");
        super.releaseConnection(repositoryConnection);
    }
}
