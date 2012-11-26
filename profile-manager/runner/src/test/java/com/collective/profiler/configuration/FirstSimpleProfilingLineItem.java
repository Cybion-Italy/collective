package com.collective.profiler.configuration;

import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class FirstSimpleProfilingLineItem extends ProfilingLineItem {

    public FirstSimpleProfilingLineItem(String name, String description) {
        super(name, description);
    }

    @Override
    public void execute(Object input) throws ProfilingLineItemException {
        // noop
    }
}
