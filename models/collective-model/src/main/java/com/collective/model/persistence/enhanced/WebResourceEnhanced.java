package com.collective.model.persistence.enhanced;

import com.collective.model.persistence.WebResource;
import com.google.gson.annotations.Expose;
import org.nnsoft.be3.annotations.RDFClassType;
import org.nnsoft.be3.annotations.RDFIdentifier;
import org.nnsoft.be3.annotations.RDFProperty;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
//TODO (high) refactor whole model to ENGLISH
//TODO (high) add json serialization to map precisely in the services response
@RDFClassType(type = "http://collective.com/resources/web")
public class WebResourceEnhanced extends WebResource {

    private SourceRssEnhanced sourceRssEnhanced;

    @Expose
    private List<URI> topics = new ArrayList<URI>();
//    private int id;

    public WebResourceEnhanced() {}

    public WebResourceEnhanced(WebResource webResource, List<URI> topics) {
        super(webResource);
        SourceRssEnhanced sourceRssEnhanced = new SourceRssEnhanced(webResource.getSourceRss());
        this.setSourceRssEnhanced(sourceRssEnhanced);
        this.topics.addAll(topics);
    }

    @RDFIdentifier
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setTitolo(String titolo) {
        super.setTitolo(titolo);
    }

    @RDFProperty(properties = {"http://purl.org/dc/terms/title"})
    @Override
    public String getTitolo() {
        return super.getTitolo();
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
    }

    @RDFProperty(properties = {"http://purl.org/dc/terms/abstract"})
    @Override
    public String getDescrizione() {
        return super.getDescrizione();
    }

    @RDFProperty(properties = {"http://collective.com/resources/web/hasSourceRSS"})
    public SourceRssEnhanced getSourceRssEnhanced() {
        return this.sourceRssEnhanced;
    }

    public void setSourceRssEnhanced(SourceRssEnhanced sourceRssEnhanced) {
        this.sourceRssEnhanced = sourceRssEnhanced;
    }

    @RDFProperty(properties = {"http://purl.org/dc/terms/subject"})
    public List<URI> getTopics() {
        return topics;
    }

    public void setTopics(List<URI> topics) {
        this.topics = topics;
    }

    public void addTopic(URI topic) {
        this.topics.add(topic);
    }

    @RDFProperty(properties = {"http://collective.com/resources/web/webpage"})
    @Override
    public URL getUrl() {
        return super.getUrl();
    }

    @Override
    public void setUrl(URL url) {
        super.setUrl(url);
    }

    @Override
    public String toString() {
        return super.toString() + " > WebResourceEnhanced{" +
                "sourceRssEnhanced=" + sourceRssEnhanced +
                ", topics=" + topics +
                '}';
    }

//    public void setId(int id) {
//        this.id = id;
//    }
}
