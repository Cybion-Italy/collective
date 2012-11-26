package com.collective.profiler.line;

/**
 * Raised if something goes wrong within the execution
 * of a {@link com.collective.profiler.line.ProfilingLineItem}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProfilingLineItemException extends Exception {
	
	public ProfilingLineItemException(String message, Exception e) {
		super(message, e);
	}
	
}
