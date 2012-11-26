package com.collective.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represent a resource associated to a {@link com.collective.model.Problem}
 * {@link com.collective.model.Solution}. It could be a file, a Web resource or even
 * a free-text description.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public abstract class Resource implements Serializable {

    //TODO refactor to Long?
    private UUID id;

    private String description;

    public Resource() {}

    protected Resource(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (!id.equals(resource.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
