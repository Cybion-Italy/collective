package com.collective.permanentsearch.model;

import java.net.URI;
import com.google.gson.annotations.Expose;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class LabelledURI {

    @Expose
    private String label;

    @Expose
    private URI url;

    public LabelledURI() {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabelledURI labelledUri = (LabelledURI) o;

        if (label != null ? !label.equals(labelledUri.label) : labelledUri.label != null) return false;
        if (url != null ? !url.equals(labelledUri.url) : labelledUri.url != null) return false;

        return true;
    }

    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "URILabel{" +
                "label='" + label + '\'' +
                ", url=" + url +
                '}';
    }
}
