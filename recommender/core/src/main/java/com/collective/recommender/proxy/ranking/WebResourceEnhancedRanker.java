package com.collective.recommender.proxy.ranking;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WebResourceEnhancedRanker implements Ranker<WebResourceEnhanced> {

    private int limit = 10;

    public List<WebResourceEnhanced> rank(List<WebResourceEnhanced> objects) throws RankerException {
        Collections.sort(objects, Collections.reverseOrder(new WebResourceEnhancedComparator()));
        List<WebResourceEnhanced> limitedResources = new ArrayList<WebResourceEnhanced>();
        for(int i=0; (i < limit) && (i < objects.size()); i++)
            limitedResources.add(objects.get(i));
        return limitedResources;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

}
