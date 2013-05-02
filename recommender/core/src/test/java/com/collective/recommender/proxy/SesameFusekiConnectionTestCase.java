package com.collective.recommender.proxy;

import org.apache.log4j.Logger;
import org.openrdf.query.*;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.http.HTTPRepository;
import org.testng.annotations.Test;


import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class SesameFusekiConnectionTestCase {

    private static final Logger LOGGER = Logger.getLogger(SesameFusekiConnectionTestCase.class);

    @Test
    public void shouldGetTriplesFromFuseki() {
        String endpointFuseki = "http://vmegov01.deri.ie:8080/l2m/query";

        try {
            HTTPRepository endPoint = new HTTPRepository(endpointFuseki);
            endPoint.initialize();

            RepositoryConnection conn = endPoint.getConnection();
            try {
                String sparqlQuery = "SELECT * WHERE {?X ?P ?Y.} LIMIT 10";
                TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
                TupleQueryResult result = query.evaluate();

                while (result.hasNext()) {
                    // do something linked and open
                    LOGGER.info("names: " + result.getBindingNames());
                    LOGGER.info(result.next().getValue(result.getBindingNames().get(0)));
                }
            } finally {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }
}
