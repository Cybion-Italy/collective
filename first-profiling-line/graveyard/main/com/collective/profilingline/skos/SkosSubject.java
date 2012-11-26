package com.collective.profilingline.skos;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * It models a <i>SKOS subject</i>.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosSubject {

    private final static String URI_FRAGMENT = "http://dbpedia.org/resource/Category:";

    private URI identifier;

    private String label;

    public SkosSubject() {}

    public SkosSubject(String identifier, String label) throws URISyntaxException {
        this.identifier = new URI(URI_FRAGMENT + identifier);
        this.label = label.replace('_', ' ');
    }

    public void setIdentifier(String identifier) throws URISyntaxException {
        this.identifier = new URI(URI_FRAGMENT + identifier);
    }

    public void setLabel(String label) {
        this.label = label.replace('_', ' ');;
    }

    public URI getIdentifier() {
        return identifier;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkosSubject that = (SkosSubject) o;

        if (identifier != null ? !identifier.equals(that.identifier) : that.identifier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SkosSubject{" +
                "identifier=" + identifier +
                ", label='" + label + '\'' +
                '}';
    }
}
