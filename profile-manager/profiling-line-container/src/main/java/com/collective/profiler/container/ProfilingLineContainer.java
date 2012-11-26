package com.collective.profiler.container;

import com.collective.profiler.line.ProfilingInput;
import com.collective.profiler.line.ProfilingLine;
import com.collective.profiler.line.ProfilingResult;

import java.util.Set;

/**
 * This interface defines the main behavior of a
 * {@link com.collective.profiler.line.ProfilingLine} container.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface ProfilingLineContainer {

    public Set getProfilingLineNames();

    public int getNumberOfProfilingLines();

    public void addProfilingLine(ProfilingLine profilingLine)
            throws ProfilingLineContainerException;

    public void removeProfilingLine(String profilingLineName)
            throws ProfilingLineContainerException;

    public ProfilingLine getProfilingLine(String profilingLineName)
        throws ProfilingLineContainerException;

    public ProfilingResult profile(ProfilingInput profilingInput, String profilingLineName)
            throws ProfilingLineContainerException;
    
}
