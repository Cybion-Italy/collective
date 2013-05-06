package com.collective.recommender.proxyimpl;

import com.collective.recommender.*;
import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.BaseSesameSparqlProxy;
import com.collective.recommender.proxy.SparqlProxyException;
import com.collective.recommender.proxy.SparqlQuery;
import com.collective.recommender.proxy.filtering.Filter;
import com.collective.recommender.proxy.filtering.FilterException;
import com.collective.recommender.proxy.queries.QueryRecord;
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
public class SesameVirtuosoSparqlProxy extends BaseSesameSparqlProxy {

    private static final Logger LOGGER = Logger.getLogger(SesameVirtuosoSparqlProxy.class);

    protected Repository repository;

    protected Be3 rdfizer;

    protected RecommenderConfiguration recommenderConfiguration;

    protected Map<String, QueryRecord> queries = new HashMap<String, QueryRecord>();

    protected URI usersCustomAnnotationsTemplate;

    public SesameVirtuosoSparqlProxy() {}

    public SesameVirtuosoSparqlProxy(final RecommenderConfiguration recommenderConfiguration,
                                     final Be3 rdFizer) {
        this.recommenderConfiguration = recommenderConfiguration;
        this.usersCustomAnnotationsTemplate = this.recommenderConfiguration
                                        .getIndexes().get("custom-annotations");
        this.repository = getRepository(this.recommenderConfiguration);
        try {
            repository.initialize();
            LOGGER.info("Virtuoso connection initialized.");
        } catch (RepositoryException e) {
            final String errMsg = "Error while initializing connection to Virtuoso";
            LOGGER.error(errMsg, e);
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

//    @Override
//    public void registerQuery(
//            String id,
//            String queryTemplate,
//            SparqlQuery.TYPE queryType,
//            Class<? extends Filter> filter,
//            Class<? extends Ranker> ranker)
//            throws SparqlProxyException {
//        if (queries.containsKey(id)) {
//            final String errMsg = "Error: query with id: '" + id + "' is already registered";
//            LOGGER.error(errMsg);
//            throw new SparqlProxyException(errMsg);
//        }
//        queries.put(id, new QueryRecord(queryTemplate, queryType, filter, ranker));
//    }

    @Override
    public <T> List<T> getList(String id, T returnType, String... args) throws SparqlProxyException {
        if (!queries.containsKey(id)) {
            final String errMsg = "Error: query with id: '" + id + "' does not exists";
            LOGGER.error(errMsg);
            throw new SparqlProxyException(errMsg);
        }
        final QueryRecord queryRecord = queries.get(id);
        final String queryTemplate = queryRecord.getQueryTemplate();
        final String queryInstance = valorizeQuery(queryTemplate, args);
        final RepositoryConnection repositoryConnection = getConnection();
        final Query query = getQuery(queryRecord, queryInstance, repositoryConnection);
        final Filter filter = getFilterInstance(queryRecord);
        final List<Statement> statements = getStatements(queryRecord, filter, query);
        releaseConnection(repositoryConnection);
        final List<T> unorderedObjects = deserializeObjects(filter, statements);
        final Ranker ranker = getRankerInstance(queryRecord);
        return rankObjects(ranker, unorderedObjects);
    }

    private <T> List<T> rankObjects(Ranker ranker, List<T> unorderedObjects) throws SparqlProxyException {

        try {
            return ranker.rank(unorderedObjects);
        } catch (RankerException e) {
            final String errMsg = "Error while ranking query results";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
    }

    private <T> List<T> deserializeObjects(Filter filter, List<Statement> statements)
            throws SparqlProxyException {

        final List<T> unorderedObjects;
        try {
            unorderedObjects = filter.getObjects(statements, this.rdfizer);
        } catch (FilterException e) {
            final String errMsg = "Error while filtering query results";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        return unorderedObjects;
    }

    private List<Statement> getStatements(QueryRecord queryRecord, Filter filter, Query query)
            throws SparqlProxyException {

        List<Statement> statements = new ArrayList<Statement>();
        if(queryRecord.getQueryType().equals(SparqlQuery.TYPE.TUPLE)) {
            final TupleQueryResult tupleQueryResult;
            try {
                tupleQueryResult = ((TupleQuery) query).evaluate();
            } catch (QueryEvaluationException e) {
                final String errMsg = "Error during the evaluation of the SPARQL query";
                LOGGER.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
            statements = getStatementsFromTuples(queryRecord, filter, tupleQueryResult);
            closeTupleResult(tupleQueryResult);
        } else if(queryRecord.getQueryType().equals(SparqlQuery.TYPE.GRAPH)) {
            final GraphQueryResult graphQueryResult;
            try {
                graphQueryResult = ((GraphQuery) query).evaluate();
            } catch (QueryEvaluationException e) {
                final String errMsg = "Error during the evaluation of the SPARQL query";
                LOGGER.error(errMsg, e);
                throw new SparqlProxyException(errMsg, e);
            }
            statements = getStatementsFromGraph(queryRecord, filter, graphQueryResult);
            closeGraphResult(graphQueryResult);
        }
        return statements;
    }

    private void closeTupleResult(TupleQueryResult queryResult) throws SparqlProxyException {

        try {
            queryResult.close();
        } catch (QueryEvaluationException e) {
            final String errMsg = "Error while closing queryresult";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
    }

    private void closeGraphResult(final GraphQueryResult queryResult) throws SparqlProxyException {

        try {
            queryResult.close();
        } catch (QueryEvaluationException e) {
            final String errMsg = "Error while closing queryresult";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
    }

    private List<Statement> getStatementsFromGraph(final QueryRecord queryRecord,
                                                   final Filter filter,
                                                   final GraphQueryResult queryResult)
            throws SparqlProxyException {

        List<Statement> statements;
        try {
            statements = filter.getStatements(queryResult, queryRecord.getQueryType());
        } catch (FilterException e) {
            final String errMsg = "Error while inspecting the query result";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        return statements;
    }

    private List<Statement> getStatementsFromTuples(final QueryRecord queryRecord,
                                                    final Filter filter,
                                                    final TupleQueryResult queryResult)
            throws SparqlProxyException {

        List<Statement> statements;
        try {
            statements = filter.getStatements(queryResult, queryRecord.getQueryType());
        } catch (FilterException e) {
            final String errMsg = "Error while inspecting the query result";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        return statements;
    }

    private Query getQuery(final QueryRecord queryRecord, final String queryInstance,
                           final RepositoryConnection repositoryConnection)
            throws SparqlProxyException {

        Query query;
        try {
            query = queryRecord.prepareQuery(repositoryConnection, queryInstance);
        } catch (RepositoryException e) {
            final String errMsg = "Error while accessing to the triple store";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (MalformedQueryException e) {
            final String errMsg = "Provided SPARQL query is not well-formed";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        return query;
    }

    private Ranker getRankerInstance(QueryRecord queryRecord) throws SparqlProxyException {

        Ranker ranker;
        try {
            ranker = queryRecord.getRanker().newInstance();
        } catch (InstantiationException e) {
            final String errMsg = "Error while instantiating the provided ranker";
            throw new SparqlProxyException(errMsg, e);
        } catch (IllegalAccessException e) {
            final String errMsg = "Error while accessing the provided ranked";
            throw new SparqlProxyException(errMsg, e);
        }
        return ranker;
    }

    private Filter getFilterInstance(QueryRecord queryRecord) throws SparqlProxyException {

        Filter filter;
        try {
            filter = queryRecord.getFilter().newInstance();
        } catch (InstantiationException e) {
            final String errMsg = "Error while instantiating filter";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        } catch (IllegalAccessException e) {
            final String errMsg = "Error while accessing to filter";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException(errMsg, e);
        }
        return filter;
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
            LOGGER.error(errMsg, e);
            throw new RecommenderException(errMsg, e);
        }
    }

    protected RepositoryConnection getConnection() throws SparqlProxyException {
        try {
            return repository.getConnection();
        } catch (RepositoryException e) {
            final String errMsg = "Error while getting session on Virtuoso";
            LOGGER.error(errMsg, e);
            throw new SparqlProxyException("Error while getting session on Virtuoso", e);
        }
    }

//    private class QueryRecord {
//
//        private String queryTemplate;
//
//        private SparqlQuery.TYPE queryType;
//
//        private Class<? extends Filter> filter;
//
//        private Class<? extends Ranker> ranker;
//
//        public QueryRecord(
//                String queryTemplate,
//                SparqlQuery.TYPE queryType,
//                Class<? extends Filter> filter,
//                Class<? extends Ranker> ranker
//        ) {
//            this.queryTemplate = queryTemplate;
//            this.queryType = queryType;
//            this.filter = filter;
//            this.ranker = ranker;
//        }
//
//        public String getQueryTemplate() {
//            return queryTemplate;
//        }
//
//        public Query prepareQuery(
//                RepositoryConnection repositoryConnection,
//                String queryInstance) throws MalformedQueryException, RepositoryException {
//            if (queryType.equals(SparqlQuery.TYPE.TUPLE)) {
//                return repositoryConnection.prepareTupleQuery(QueryLanguage.SPARQL, queryInstance);
//            }
//            if (queryType.equals(SparqlQuery.TYPE.GRAPH)) {
//                return repositoryConnection.prepareGraphQuery(QueryLanguage.SPARQL, queryInstance);
//            }
//            throw new IllegalArgumentException("Unable to instantiate a proper query");
//        }
//
//        public Class<? extends Filter> getFilter() {
//            return filter;
//        }
//
//        public Class<? extends Ranker> getRanker() {
//            return ranker;
//        }
//    }
}
