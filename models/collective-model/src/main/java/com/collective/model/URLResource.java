package com.collective.model;

import java.net.URL;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class URLResource extends Resource {

    private URL url;

    public URLResource() {
        super();
    }

    public URLResource(UUID id) {
        super(id);
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "URLResource{" +
                "url=" + url +
                "} " + super.toString();
    }
}
