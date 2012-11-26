package com.collective.persistencewebresources.domain;

import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import org.joda.time.DateTime;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DomainFixture {

	public static SourceRss newTestFonteRss() throws MalformedURLException {
		SourceRss source = new SourceRss();
		source.setId(new Integer(1));
        source.setLingua("language");
        source.setParola("word");
        source.setCategoria("catogory-fake");
        source.setSource(newTestFonte());
        source.setDataUltimaEstrazione(new DateTime().minusDays(3));
        source.setUrl(new URL("http://www.example.com"));
        source.setValid(true);
		return source;
	}
	
	public static WebResource newTestUrl() throws MalformedURLException {
        DateTime dataEstrazione = new DateTime();
	    WebResource webResource = new WebResource();
	    webResource.setTitolo("titolo di esempio " + dataEstrazione.getMillis());
	    webResource.setDescrizione("descrizione di esempio " + dataEstrazione.getMillis());
	    webResource.setDataEstrazione(dataEstrazione);
	    webResource.setDataPubblicazione(dataEstrazione);
	    webResource.setUrl(new URL("http://www.cybion.it/test" + dataEstrazione.getMillis() + ".xml"));
	    webResource.setContenutoHtml("<stringContentHtml>");
	    webResource.setContenutoTesto("string content testo");
	    webResource.setAnalyzed(false);
	    webResource.setSourceRss(newTestFonteRss());
	    return webResource;
	  }

	public static SourceRss newTestFonteRssComplete() {
		SourceRss sourceRss = new SourceRss();
		sourceRss.setLingua("lingua");
		sourceRss.setParola("parola");
        sourceRss.setCategoria("categoria");
        try {
            sourceRss.setUrl(new URL("http://sourcerss.url.com"));
        } catch (MalformedURLException e) {
            //never happens
        }
        sourceRss.setDataUltimaEstrazione(new DateTime());
		sourceRss.setSource(newTestFonte());
		return sourceRss;
	}

	private static Source newTestFonte() {
		Source source = new Source();
		source.setId(new Integer(1));
        source.setNome("nomeFonte");
		return source;
	}

    /**
     * Returns a number of {@link WebResource} to be analyzed.
     *
     * @param count
     * @return
     */
    public static List<WebResource> newUrlsToBeAnalyzed(int count) throws MalformedURLException {
        if(count < 0)
            throw new IllegalArgumentException("Do you think I'm able to instatiate a negative number of objects?");
        List<WebResource> webResources = new ArrayList<WebResource>();
        for(int i = 0; i< count; i++)
            webResources.add(newTestUrl());
        return webResources;
    }
}
