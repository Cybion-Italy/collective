package com.collective.model;

import java.util.UUID;

/**
 * A generic Agent as described in the <i>Collective Conceptual Model</i>.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public abstract class Agent {

    private Long id;

    protected Agent() {}

    protected Agent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (id != null ? !id.equals(agent.id) : agent.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                '}';
    }
}
