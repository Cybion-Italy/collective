package com.collective.concepts.core.lookup;

import com.collective.concepts.core.lookup.result.ResultListener;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface UserDefinedConceptLookupEngine {

    public void lookup(String text, long userId, ResultListener resultListener)
            throws UserDefinedConceptLookupEngineException;

}
