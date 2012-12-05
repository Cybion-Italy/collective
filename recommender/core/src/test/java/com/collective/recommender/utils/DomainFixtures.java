package com.collective.recommender.utils;


import com.collective.model.persistence.enhanced.SourceRssEnhanced;
import com.collective.model.persistence.enhanced.WebResourceEnhanced;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DomainFixtures {

    public static final String LABEL_PREFIX = "label_";

    public static URI getCustomConcept(String userId, String label) {
        URI customConceptUri = null;
        try {
            customConceptUri = new URI(UserIdParser.BASE +
                                       "no_company/" +
                                        userId + "/" +
                                        label);
        } catch (URISyntaxException e) {
            //should never happen
            e.printStackTrace();
        }
        return customConceptUri;
    }

    public static WebResourceEnhanced getResourceWithAnnotations(String id,
                                                          List<URI> customConcepts) {
        WebResourceEnhanced webResource = new WebResourceEnhanced();

        webResource.setId(Integer.parseInt( "" + id ));
        webResource.setTitolo("fake title " + id);
        try {
            webResource.setUrl(new URL("http://www.fakeurl.com/" + id));
        } catch (MalformedURLException e) {
            //should never happen
            e.printStackTrace();
        }
        webResource.setDescrizione("fake description " + id);
        //set topics
        webResource.setTopics(customConcepts);
        webResource.setSourceRssEnhanced(getSourceRssEnhanced(webResource));
        return webResource;
    }

    private static SourceRssEnhanced getSourceRssEnhanced(WebResourceEnhanced webResource) {

        SourceRssEnhanced sourceRssEnhanced = new SourceRssEnhanced();
        sourceRssEnhanced.setId(webResource.getId() + 1);
        try {
            sourceRssEnhanced.setUrl(new URL("http://www.fakeurlsource.com/"
                                       + sourceRssEnhanced.getId().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        sourceRssEnhanced.setCategoria("fake category");

        return sourceRssEnhanced;
    }

    public static List<WebResourceEnhanced> getResourcesAmount(String userId,
                                                               Integer amount) {

        List<WebResourceEnhanced> webResources = new ArrayList<WebResourceEnhanced>();
        for (Integer i = 0; i < amount ; i++) {
            List<URI> resourcesCustomConcepts = new ArrayList<URI>();
            URI customConcept = getCustomConcept(userId, LABEL_PREFIX + userId);
            resourcesCustomConcepts.add(customConcept);
            WebResourceEnhanced webResourceEnhanced =
                    getResourceWithAnnotations(i.toString(), resourcesCustomConcepts);

            webResources.add(webResourceEnhanced);
        }

        return webResources;
    }

    public static WebResourceEnhanced getWebResourceEnhanced(Integer id) {
        WebResourceEnhanced webResourceEnhanced = new WebResourceEnhanced();
        webResourceEnhanced.setId(id);
        try {
            webResourceEnhanced.setUrl(new URL("http://www.test.com/"
                                                + id.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return webResourceEnhanced;
    }
}
