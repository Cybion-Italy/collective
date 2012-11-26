package com.collective.profiler.container.defaults;

import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.profiler.line.ProfilingResult;

/**
 * This {@link com.collective.profiler.line.ProfilingLineItem} is the last
 * item of every pipeline by default. And it's able to instantiate a valid
 * {@link com.collective.profiler.line.ProfilingResult}.
 * 
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DumpProfilingLineContainerItem<T> extends ProfilingLineItem {

    private ProfilingResult result;

    private T inputType;

    public DumpProfilingLineContainerItem(String name, String description, T inputType) {
        super(name, description);
        this.inputType = inputType;
    }

    public void execute(Object input) throws ProfilingLineItemException {
        T value = (T) input;
        result = new ProfilingResult(value);
    }

    public ProfilingResult getProfile() {
        if(result == null)
            throw new IllegalStateException("Maybe the item has not been executed yet?");
        return result;
    }
}
