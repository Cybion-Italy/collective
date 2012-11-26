package com.collective.document.analyzer.analysis;

import tv.notube.commons.alchemyapi.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MockAlchemyAPI extends AlchemyAPI {

    public static final String FAKE_NAMED_ENTITY = "http://fakenamedentity.com";

    public static final String FAKE_CONCEPT = "http://fakeconcept.com";

    public MockAlchemyAPI(String apikey) {
        super(apikey);
    }

    public AlchemyAPIResponse getNamedEntities(String textToAnalyse) {
        AlchemyAPIResponse response;
        response = new AlchemyAPIResponse(AlchemyAPIResponse.Status.OK);
        List<Identified> namedEntities = new ArrayList<Identified>();
        try {
            namedEntities.add(new NamedEntity(new URI(FAKE_NAMED_ENTITY), 1.0f));
        } catch (URISyntaxException e) {
            //never happens
        }
        response.setIdentified(namedEntities);
        return response;
    }

    public AlchemyAPIResponse getRankedConcept(String textToAnalyse) {
        AlchemyAPIResponse response;
        response = new AlchemyAPIResponse(AlchemyAPIResponse.Status.OK);
        List<Identified> rankedConcepts = new ArrayList<Identified>();

        try {
            rankedConcepts.add(new Concept(new URI(FAKE_CONCEPT), 0.5f));
        } catch (URISyntaxException e) {
            //never happens
        }
        response.setIdentified(rankedConcepts);
        return response;
    }
}
