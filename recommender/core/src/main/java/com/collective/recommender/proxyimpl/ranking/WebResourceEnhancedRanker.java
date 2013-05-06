package com.collective.recommender.proxyimpl.ranking;

import com.collective.model.persistence.enhanced.WebResourceEnhanced;
import com.collective.recommender.proxy.ranking.Ranker;
import com.collective.recommender.proxy.ranking.RankerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class WebResourceEnhancedRanker implements Ranker<WebResourceEnhanced> {

    private int limit = 10;

    @Override
    public List<WebResourceEnhanced> rank(List<WebResourceEnhanced> objects) throws
            RankerException {
        Collections.sort(objects, Collections.reverseOrder(new WebResourceEnhancedComparator()));
        List<WebResourceEnhanced> limitedResources = new ArrayList<WebResourceEnhanced>();
        for(int i=0; (i < limit) && (i < objects.size()); i++)
            limitedResources.add(objects.get(i));
        return limitedResources;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int getLimit() {
        return limit;
    }

}
