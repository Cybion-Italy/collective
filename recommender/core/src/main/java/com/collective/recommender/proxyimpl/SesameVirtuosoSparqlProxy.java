package com.collective.recommender.proxyimpl;

import com.collective.recommender.configuration.RecommenderConfiguration;
import com.collective.recommender.proxy.BaseSesameSparqlProxy;
import com.collective.recommender.proxy.SparqlProxyException;
import com.collective.recommender.proxy.filtering.Filter;
import com.collective.recommender.proxy.filtering.FilterException;
import com.collective.recommender.proxy.queries.QueryRecord;
import com.collective.recommender.proxy.ranking.Ranker;
import org.apache.log4j.Logger;
import org.nnsoft.be3.Be3;
import org.openrdf.model.Statement;
import org.openrdf.query.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import virtuoso.sesame2.driver.VirtuosoRepository;

import java.net.URI;
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

    private final Repository getRepository(RecommenderConfiguration recommenderConfiguration) {
        return new VirtuosoRepository(
                "jdbc:virtuoso://"
                        + recommenderConfiguration.getHost() + ":"
                        + recommenderConfiguration.getPort(),
                        recommenderConfiguration.getUser(),
                        recommenderConfiguration.getPassword()
        );
    }

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
        final RepositoryConnection repositoryConnection = getConnection(this.repository);
        final Query query = getQuery(queryRecord, queryInstance, repositoryConnection);
        final Filter filter = getFilterInstance(queryRecord);
        final List<Statement> statements = getStatements(queryRecord, filter, query);
        releaseConnection(repositoryConnection);
        final List<T> unorderedObjects = deserializeObjects(filter, statements);
        final Ranker ranker = getRankerInstance(queryRecord);
        return rankObjects(ranker, unorderedObjects);
    }

    @Override
    public <T> T getObject(String id, String... args) throws SparqlProxyException {
        throw new UnsupportedOperationException("NIY");
    }

    @Override
    protected final <T> List<T> deserializeObjects(Filter<T> filter, List<Statement> statements)
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

    @Override
    protected final <T> T deserializeObject(Filter<T> filter, List<Statement> statements)
            throws SparqlProxyException {
        throw new UnsupportedOperationException("NIY");
    }

    private String valorizeQuery(String queryTemplate, String[] args) {
        return String.format(queryTemplate, args);
    }

    protected Ranker getRankerInstance(QueryRecord queryRecord) throws SparqlProxyException {

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

    protected Filter getFilterInstance(QueryRecord queryRecord) throws SparqlProxyException {

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
}
