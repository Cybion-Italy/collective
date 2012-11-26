package com.collective.profiler.container;

/**
 * Raised if something goes wrong within the operation foreseen
 * by {@link com.collective.profiler.container.ProfilingLineContainer}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineContainerException extends Exception {
    
    public ProfilingLineContainerException(String message) {
        super(message);
    }

    public ProfilingLineContainerException(String message, Exception e) {
        super(message, e);
    }
}
