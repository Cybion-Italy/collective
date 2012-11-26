package com.collective.recommender.proxy.filtering;

import com.collective.rdfizer.RDFizer;
import com.collective.recommender.proxy.SparqlProxy;
import org.openrdf.model.Statement;
import org.openrdf.query.QueryResult;
import org.openrdf.query.TupleQueryResult;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Filter<T> {

    public List<Statement> getStatements(QueryResult queryResult, SparqlProxy.TYPE queryType)
            throws FilterException;

    public List<T> getObjects(List<Statement> statements, RDFizer rdfizer) throws FilterException;

}
