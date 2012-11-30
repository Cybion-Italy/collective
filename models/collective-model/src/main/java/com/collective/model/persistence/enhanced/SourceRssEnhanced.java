package com.collective.model.persistence.enhanced;

import com.collective.model.persistence.SourceRss;
import org.nnsoft.be3.annotations.RDFClassType;
import org.nnsoft.be3.annotations.RDFIdentifier;
import org.nnsoft.be3.annotations.RDFProperty;

import java.net.URL;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
@RDFClassType( type = "http://collective.com/resource/SourceRSS" )
public class SourceRssEnhanced extends SourceRss {

    public SourceRssEnhanced() {}

    public SourceRssEnhanced(SourceRss sourceRss) {
        super(sourceRss);
    }

    @RDFIdentifier
    public Integer getId() {
        return super.getId();
    }

    @RDFProperty(properties = {"http://collective.com/resources/SourceRSS#Category"})
    @Override
    public String getCategoria() {
        return super.getCategoria();
    }

    @RDFProperty (properties = {"http://collective.com/resources/web/hasFeed"})
    @Override
    public URL getUrl() {
        return super.getUrl();
    }

}
