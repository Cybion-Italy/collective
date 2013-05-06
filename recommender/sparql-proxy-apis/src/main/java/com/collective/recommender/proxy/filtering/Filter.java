package com.collective.recommender.proxy.filtering;

import com.collective.recommender.proxy.SparqlQuery;
import org.nnsoft.be3.Be3;
import org.openrdf.model.Statement;
import org.openrdf.query.QueryResult;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Filter<T> {

    public List<Statement> getStatements(QueryResult queryResult, SparqlQuery.TYPE queryType)
            throws FilterException;

    public List<T> getObjects(List<Statement> statements, Be3 rdfizer) throws FilterException;

}
