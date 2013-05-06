package com.collective.recommender.proxyimpl.ranking;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;

import java.util.Comparator;

public class WebResourceEnhancedComparator implements Comparator<WebResourceEnhanced> {

    @Override
    public int compare(
            WebResourceEnhanced webResourceEnhanced,
            WebResourceEnhanced webResourceEnhanced1
    ) {
        if (webResourceEnhanced.getId() > webResourceEnhanced1.getId())
            return 1;
        if (webResourceEnhanced.getId() < webResourceEnhanced1.getId())
            return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

}
