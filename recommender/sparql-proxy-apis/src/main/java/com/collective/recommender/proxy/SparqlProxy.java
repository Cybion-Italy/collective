package com.collective.recommender.proxy;

import com.collective.recommender.proxy.filtering.Filter;
import com.collective.recommender.proxy.ranking.Ranker;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface SparqlProxy {

    public enum TYPE {
        GRAPH,
        TUPLE
    }

    public void registerQuery(
            String id,
            String queryTemplate,
            SparqlProxy.TYPE queryType,
            Class<? extends Filter> filter,
            Class<? extends Ranker> ranker)
            throws SparqlProxyException;

    public <T> List<T> getList(String id, T returnType, String... args)
            throws SparqlProxyException;

    public <T> T getObject(String id, String... args)
            throws SparqlProxyException;

}
