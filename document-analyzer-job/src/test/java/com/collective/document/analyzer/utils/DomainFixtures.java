package com.collective.document.analyzer.utils;

import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.collective.model.persistence.enhanced.SourceRssEnhanced;
import com.collective.model.persistence.enhanced.WebResourceEnhanced;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DomainFixtures {

    public static WebResourceEnhanced getWebResourceEnhanced() throws MalformedURLException {
        WebResourceEnhanced webResourceEnhanced = new WebResourceEnhanced();
        webResourceEnhanced.setId(23);
        webResourceEnhanced.setUrl(new URL("http://fakeresource.com"));
        webResourceEnhanced.setDescrizione("fake description");
        webResourceEnhanced.setTitolo("fake title");
        webResourceEnhanced.setSourceRssEnhanced(getSourceRssEnhanced());
        return webResourceEnhanced;
    }

    public static SourceRssEnhanced getSourceRssEnhanced() throws MalformedURLException {
        SourceRssEnhanced sourceRssEnhanced = new SourceRssEnhanced();
        sourceRssEnhanced.setId(24);
        sourceRssEnhanced.setUrl(new URL("http://fakeurlsource.rss.com"));
        sourceRssEnhanced.setCategoria("fakecategory");
        return sourceRssEnhanced;
    }

    public static SourceRss getSourceRss() {
        SourceRss sourceRss = new SourceRssEnhanced();
        sourceRss.setId(24);
        sourceRss.setValid(true);
        try {
            sourceRss.setUrl(new URL("http://fakeurlsource.rss.com"));
        } catch (MalformedURLException e) {
            //never happens
        }
        sourceRss.setCategoria("fakecategory");
        return sourceRss;
    }

    public static List<WebResource> getNotAnalysedWebResources(int amount) {
        List<WebResource> toAnalyseWebResources = new ArrayList<WebResource>();
        for (int i = 1; i <= amount; i++) {
            toAnalyseWebResources.add(getWebResource(i, false));
        }
        return toAnalyseWebResources;
    }

    private static WebResource getWebResource(int id, boolean isAnalysed) {
        WebResource webResource =  new WebResource();
        //set values
        webResource.setId(id);
        webResource.setAnalyzed(false);

        try {
            webResource.setUrl(new URL("http://www.example.com"));
        } catch (MalformedURLException e) {
            //never happens
        }
        //TODO add values
        webResource.setTitolo("a");
        webResource.setDescrizione("b");
        webResource.setContenutoTesto("c");
        webResource.setSourceRss(getSourceRss());
        return webResource;
    }


}
