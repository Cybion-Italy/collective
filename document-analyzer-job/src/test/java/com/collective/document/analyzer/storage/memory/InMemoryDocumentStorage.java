package com.collective.document.analyzer.storage.memory;

import com.collective.analyzer.storage.DocumentStorageException;
import com.collective.analyzer.storage.SesameVirtuosoDocumentStorage;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class InMemoryDocumentStorage extends SesameVirtuosoDocumentStorage
{

    private static final Logger logger = Logger.getLogger(InMemoryDocumentStorage.class);

    public InMemoryDocumentStorage(Be3 rdfizer,
                                   URI defaultGraph,
                                   URI usersAnnotationsTemplate) {

        super(rdfizer, defaultGraph, usersAnnotationsTemplate);
        //build in memory sail repository
        this.repository = new SailRepository(new MemoryStore());
        try {
            this.repository.initialize();
            logger.info("Sesame in memory connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing Sesame in memory";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }

    public List<Statement> getAllGraphStatements(String graphName)
            throws DocumentStorageException
    {
        RepositoryConnection repositoryConnection = getConnection();
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
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        } catch (QueryEvaluationException e) {
            final String errMsg = "Error while evaluating query: '"
                    + constructGraphQuery + "'";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        } catch (MalformedQueryException e) {
            final String errMsg = "Malformed query: '"
                    + constructGraphQuery + "'";
            logger.error(errMsg, e);
            throw new DocumentStorageException(errMsg, e);
        } finally {
            releaseConnection(repositoryConnection);
        }
        return statements;
    }
}
