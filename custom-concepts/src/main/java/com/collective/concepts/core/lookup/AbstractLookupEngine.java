package com.collective.concepts.core.lookup;

import com.collective.concepts.core.UserDefinedConceptStore;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public  abstract class AbstractLookupEngine
        implements UserDefinedConceptLookupEngine {

    protected UserDefinedConceptStore conceptStore;

    public AbstractLookupEngine(
            UserDefinedConceptStore conceptStore) {
        this.conceptStore = conceptStore;
    }

}
