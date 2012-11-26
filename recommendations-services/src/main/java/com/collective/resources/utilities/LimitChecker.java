package com.collective.resources.utilities;

import com.collective.resources.AbstractRecommendation;
import org.apache.log4j.Logger;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class LimitChecker {

    private static final int maxRecommendations = AbstractRecommendation.MAX_RECOMMENDATIONS;

    private static Logger logger = Logger.getLogger(LimitChecker.class);

    public static void checkRequestedItemsAmount(int maxItems) {
        if (maxItems > maxRecommendations) {
            logger.warn("got request of too many items: " + maxItems);
            throw new RuntimeException("too many items requested: " +
                    "maxItems " + maxItems + " " +
                    "should be less than " + maxRecommendations);
        }
    }
}
