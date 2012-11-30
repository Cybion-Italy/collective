package com.collective.model;


import org.nnsoft.be3.annotations.RDFClassType;

/**
 * A set of roles a {@link com.collective.model.User} could play
 * in a {@link com.collective.model.Project}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
//TODO: rdfizer should be able to manage it
@RDFClassType(type = "http://collective.com/ProjectInvolvement/Role")
public enum Role {

    initiator,
    collaborator,
    manager,
    supervisor,
    supporting,
    
}
