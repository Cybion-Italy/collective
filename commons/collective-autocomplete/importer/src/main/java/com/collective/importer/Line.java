package com.collective.importer;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Line {

    private URL url;

    private String label;

    private long owner;

    public static Line buildLine(String uri, String label)
            throws LineNotWellFormedException {
        URL uriObj;
        try {
            uriObj = new URL(uri);
        } catch (MalformedURLException e) {
            throw new LineNotWellFormedException("uri: '" + uri +
                    "' and label '" + label + "' you provided are not valid", e);
        }
        return new Line(uriObj, label);
    }

    public Line() {}

    public Line(URL url, String label) {
        this.url = url;
        this.label = label;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (url != null ? !url.equals(line.url) : line.url != null) return false;

        return true;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Line{" +
                "url=" + url +
                ", label='" + label + '\'' +
                ", owner=" + owner +
                '}';
    }
}
