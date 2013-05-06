package com.collective.recommender.proxyimpl;

import com.collective.recommender.*;
import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.SparqlProxy;
import com.collective.recommender.proxy.SparqlProxyException;
import com.collective.recommender.proxy.SparqlQuery;
import com.collective.recommender.proxy.filtering.Filter;
import com.collective.recommender.proxy.filtering.FilterException;
import com.collective.recommender.proxy.ranking.Ranker;
import com.collective.recommender.proxy.ranking.RankerException;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;
import org.openrdf.model.Statement;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import virtuoso.sesame2.driver.VirtuosoRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci (matteo (dot) moci (at) gmail (dot) com)
 */
public class SesameVirtuosoSparqlProxy implements SparqlProxy {

    private static Logger logger = Logger.getLogger(SesameVirtuosoSparqlProxy.class);

    protected Repository repository;

    protected Be3 rdfizer;

    protected RecommenderConfiguration recommenderConfiguration;

    protected Map<String, QueryRecord> queries = new HashMap<String, QueryRecord>();

    protected URI usersCustomAnnotationsTemplate;

    public SesameVirtuosoSparqlProxy() {}

    public SesameVirtuosoSparqlProxy(RecommenderConfiguration recommenderConfiguration,
                                     Be3 rdFizer) {
        this.recommenderConfiguration = recommenderConfiguration;
        this.usersCustomAnnotationsTemplate = this.recommenderConfiguration
                                        .getIndexes().get("custom-annotations");
        this.repository = getRepository(this.recommenderConfiguration);
        try {
            repository.initialize();
            logger.info("Virtuoso connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing connection to Virtuoso";
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
        this.rdfizer = rdFizer;
    }

    private Repository getRepository(RecommenderConfiguration recommenderConfiguration) {
        return new VirtuosoRepository(
                "jdbc:virtuoso://"
                        + recommenderConfiguration.getHost() + ":"
                        + recommenderConfiguration.getPort(),
                        recommenderConfiguration.getUser(),
                        recommenderConfiguration.getPassword()
        );
    }

    @Override
    public void registerQuery(
            String id,
            String queryTemplate,
            SparqlQuery.TYPE queryType,
            Class<? extends Filter> filter,
            Class<? extends Ranker> ranker)
            throws SparqlProxyException {
        if (queries.containsKey(id)) {
            final String errMsg = "Error: query with id: '" + id + "' is already registered";
            logger.error(errMsg);
            throw new SparqlProxyException(errMsg);
        }
        queries.put(id, new QueryRecord(queryTemplate, queryType, filter, ranker));
    }

    @Override
    public <T> List<T> getList(String id, T returnType, String... args) throws SparqlProxyException {
        if (!queries.containsKey(id)) {
            final String errMsg = "Error: query with id: '" + id + "' does not exists";
            logger.error(errMsg);
            throw new SparqlProxyException(errMsg);
        }
        String queryTemplate = queries.get(id).getQueryTemplate();
        Filter filter;
        try {
            filter = queries.get(id).getFilter().newInstance();
        } catch (InstantiationException e) {
            final String errMsg = "Error while instantiating filter";
            logger.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (IllegalAccessException e) {
            final String errMsg = "Error while accessing to filter";
            logger.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        Ranker ranker;
        try {
            ranker = queries.get(id).getRanker().newInstance();
        } catch (InstantiationException e) {
            final String errMsg = "Error while instantiating the provided ranker";
            throw new SparqlProxyException(errMsg, e);
        } catch (IllegalAccessException e) {
            final String errMsg = "Error while accessing the provided ranked";
            throw new SparqlProxyException(errMsg, e);
        }
        String queryInstance = valorizeQuery(queryTemplate, args);
        RepositoryConnection repositoryConnection = getConnection();
        Query query;
        try {
            query = queries.get(id).prepareQuery(repositoryConnection, queryInstance);
        } catch (RepositoryException e) {
            final String errMsg = "Error while accessing to the triple store";
            logger.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (MalformedQueryException e) {
            final String errMsg = "Provided SPARQL query is not well-formed";
            logger.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        List<Statement> statements = new ArrayList<Statement>();
        if(queries.get(id).queryType.equals(SparqlQuery.TYPE.TUPLE)) {
            TupleQueryResult queryResult;
            try {
                queryResult = ((TupleQuery) query).evaluate();
            } catch (QueryEvaluationException e) {
                final String errMsg = "Error during the evaluation of the SPARQL query";
                logger.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
            try {
                statements = filter.getStatements(queryResult, queries.get(id).queryType);
            } catch (FilterException e) {
                final String errMsg = "Error while inspecting the query result";
                logger.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
            try {
                queryResult.close();
            } catch (QueryEvaluationException e) {
                final String errMsg = "Error while closing queryresult";
                logger.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
        } else if(queries.get(id).queryType.equals(SparqlQuery.TYPE.GRAPH)) {
            GraphQueryResult queryResult;
            try {
                queryResult = ((GraphQuery) query).evaluate();
            } catch (QueryEvaluationException e) {
                final String errMsg = "Error during the evaluation of the SPARQL query";
                logger.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
            try {
                statements = filter.getStatements(queryResult, queries.get(id).queryType);
            } catch (FilterException e) {
                final String errMsg = "Error while inspecting the query result";
                logger.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
            try {
                queryResult.close();
            } catch (QueryEvaluationException e) {
                final String errMsg = "Error while closing queryresult";
                logger.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
        }
        releaseConnection(repositoryConnection);
        try {
            return ranker.rank(filter.getObjects(statements, this.rdfizer));
        } catch (FilterException e) {
            final String errMsg = "Error while filtering query results";
            logger.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (RankerException e) {
            final String errMsg = "Error while ranking query results";
            logger.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
    }

    protected void releaseConnection(RepositoryConnection repositoryConnection)
            throws SparqlProxyException {
        try {
            repositoryConnection.close();
        } catch (RepositoryException e) {
            final String errMsg = "Error while closing session on Virtuoso";
            logger.error(errMsg);
            throw new SparqlProxyException(errMsg, e);
        }
    }

    private String valorizeQuery(String queryTemplate, String[] args) {
        return String.format(queryTemplate, args);
    }

    public <T> T getObject(String id, String... args) throws SparqlProxyException {
        throw new UnsupportedOperationException("NIY");
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws RecommenderException {
        try {
            repository.shutDown();
        } catch (RepositoryException e) {
            final String errMsg = "Error while shutting down the connection with Virtuoso";
            logger.error(errMsg, e);
            throw new RecommenderException(errMsg, e);
        }
    }

    protected RepositoryConnection getConnection() throws SparqlProxyException {
        try {
            return repository.getConnection();
        } catch (RepositoryException e) {
            final String errMsg = "Error while getting session on Virtuoso";
            logger.error(errMsg, e);
            throw new SparqlProxyException("Error while getting session on Virtuoso", e);
        }
    }

    private class QueryRecord {

        private String queryTemplate;

        private SparqlQuery.TYPE queryType;

        private Class<? extends Filter> filter;

        private Class<? extends Ranker> ranker;

        public QueryRecord(
                String queryTemplate,
                SparqlQuery.TYPE queryType,
                Class<? extends Filter> filter,
                Class<? extends Ranker> ranker
        ) {
            this.queryTemplate = queryTemplate;
            this.queryType = queryType;
            this.filter = filter;
            this.ranker = ranker;
        }

        public String getQueryTemplate() {
            return queryTemplate;
        }

        public Query prepareQuery(
                RepositoryConnection repositoryConnection,
                String queryInstance) throws MalformedQueryException, RepositoryException {
            if (queryType.equals(SparqlQuery.TYPE.TUPLE)) {
                return repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryInstance);
            }
            if (queryType.equals(SparqlQuery.TYPE.GRAPH)) {
                return repositoryConnection.prepareGraphQuery(QueryLanguage.SPARQL, queryInstance);
            }
            throw new IllegalArgumentException("Unable to instantiate a proper query");
        }

        public Class<? extends Filter> getFilter() {
            return filter;
        }

        public Class<? extends Ranker> getRanker() {
            return ranker;
        }
    }
}
