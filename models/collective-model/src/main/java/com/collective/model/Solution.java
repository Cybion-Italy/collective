package com.collective.model;

import java.util.List;
import java.util.UUID;

/**
 * This class represents a solution how it has been defined within the
 * <i>Collective Conceptual Model</i>.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Solution {

    //TODO: refactor to Long?
    private UUID id;

    private String description;

    /**
     * a list of resources that could contain important
     * and useful information applicable to the {@link com.collective.model.Problem}
     * that this solution solves.
     */
    private List<Resource> resources;

    public Solution(UUID id) {
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

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Solution solution = (Solution) o;

        if (id != null ? !id.equals(solution.id) : solution.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", resources=" + resources +
                '}';
    }
}