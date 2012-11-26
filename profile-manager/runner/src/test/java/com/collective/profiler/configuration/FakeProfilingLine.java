package com.collective.profiler.configuration;

import com.collective.profiler.line.ProfilingInput;
import com.collective.profiler.line.ProfilingLine;
import com.collective.profiler.line.ProfilingLineException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FakeProfilingLine extends ProfilingLine {


    public FakeProfilingLine() {
        super("fake-profiling-line", "just a fake ProfilingLine for testing purposes");
    }

    public FakeProfilingLine(String name, String description) {
        super(name, description);
    }

    @Override
    public void run(ProfilingInput profilingInput) throws ProfilingLineException {
        
    }
}
