package com.collective.recommender.proxyimpl.filtering;

import com.collective.model.profile.UserProfile;
import com.collective.recommender.proxy.SparqlQuery;
import com.collective.recommender.proxy.filtering.Filter;
import com.collective.recommender.proxy.filtering.FilterException;
import org.nnsoft.be3.Be3;
import org.nnsoft.be3.RDFizerException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryResult;
import org.openrdf.query.TupleQueryResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserProfileFilter implements Filter<UserProfile> {

    @Override
    public List<Statement> getStatements(QueryResult queryResult, SparqlQuery.TYPE queryType)
            throws FilterException {
        if(queryType.equals(SparqlQuery.TYPE.TUPLE)) {
            List<Statement> statements = new ArrayList<Statement>();
            TupleQueryResult tupleQueryResult = (TupleQueryResult) queryResult;
            try {
                while (queryResult.hasNext()) {
                    List<String> variables = tupleQueryResult.getBindingNames();
                    BindingSet bindingSet = tupleQueryResult.next();
                    Value s = bindingSet.getBinding(variables.get(0)).getValue();
                    Value p = bindingSet.getBinding(variables.get(1)).getValue();
                    Value o = bindingSet.getBinding(variables.get(2)).getValue();
                    statements.add(new StatementImpl(
                            new URIImpl(s.stringValue()),
                            new URIImpl(p.stringValue()),
                            o
                    ));
                }
            } catch (Exception e) {
                throw new FilterException("", e);
            }
            return statements;
        }
        if(queryType.equals(SparqlQuery.TYPE.GRAPH)) {
            List<Statement> statements = new ArrayList<Statement>();
            GraphQueryResult graphQueryResult = (GraphQueryResult) queryResult;
            try {
                while (queryResult.hasNext()) {
                    statements.add(graphQueryResult.next());
                }
            } catch (Exception e) {
                throw new FilterException("", e);
            }
            return statements;
        }
        throw new IllegalArgumentException("Did you provide a correct Query Type?");
    }

    @Override
    public List<UserProfile> getObjects(List<Statement> statements, Be3 rdfizer)
            throws FilterException {
        List<UserProfile> usersProfiles = new ArrayList<UserProfile>();
        Set<URI> uniqueSubjects = new HashSet<URI>();
        for(Statement statement : statements) {
            uniqueSubjects.add(new URIImpl(statement.getSubject().toString()));
        }
        for(URI uri : uniqueSubjects) {
            try {
                usersProfiles.add((UserProfile) rdfizer.getObject(
                        filterStatementBySubject(uri, statements),
                        uri,
                        UserProfile.class)
                );
            } catch (RDFizerException e) {
                throw new FilterException("Error while deserializing the WebResourceEnhanced", e);
            }
        }
        return usersProfiles;
    }

    @Override
    public UserProfile getObject(List<Statement> statements, Be3 be3) throws FilterException {

        throw new UnsupportedOperationException("NIY");
    }

    private List<Statement> filterStatementBySubject(
            URI subject,
            List<Statement> statements) {
        List<Statement> result = new ArrayList<Statement>();
        for(Statement statement : statements) {
            if(statement.getSubject().equals(subject)) {
                result.add(statement);
            }
        }
        return result;
    }

}
