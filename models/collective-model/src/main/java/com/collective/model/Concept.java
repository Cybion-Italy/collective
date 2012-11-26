package com.collective.model;

import java.net.URI;

/**
 * This class roughly represents a concept from an external <i>Linked Data</i>
 * cloud.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Concept {

    private URI id;

    /**
     * Typically the subject of a <i>rdfs:label</i> property.
     */
    private String label;

    public URI getId() {
        return id;
    }

    public void setId(URI id) {
        this.id = id;
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

        Concept concept = (Concept) o;

        if (!id.equals(concept.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Concept{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
