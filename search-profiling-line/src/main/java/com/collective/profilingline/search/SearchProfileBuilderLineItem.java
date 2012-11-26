package com.collective.profilingline.search;

import com.collective.model.profile.SearchProfile;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import org.apache.log4j.Logger;


/**
 * This {@link com.collective.profiler.line.ProfilingLineItem} is responsible
 * to build a concrete instance of {@link com.collective.model.profile.SearchProfile} and
 * should be the unique item of a {@link com.collective.profiler.line.ProfilingLine}.
 *
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 *
 */
public class SearchProfileBuilderLineItem extends ProfilingLineItem {

    private final static Logger logger = Logger.getLogger(SearchProfileBuilderLineItem.class);

    public SearchProfileBuilderLineItem(String name, String description) {
        super(name, description);
    }

    public void execute(Object o) throws ProfilingLineItemException {
        // it just bypasses the profiling line
        SearchProfile resultSearchProfile = (SearchProfile) o;
        super.next.execute(resultSearchProfile);
    }
}
