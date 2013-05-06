package com.collective.recommender.proxyimpl.ranking;

import com.collective.model.profile.ProjectProfile;
import com.collective.recommender.proxy.ranking.Ranker;
import com.collective.recommender.proxy.ranking.RankerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProjectProfileRanker implements Ranker<ProjectProfile> {

    private int limit = 10;

    @Override
    public List<ProjectProfile> rank(List<ProjectProfile> objects) throws RankerException {
        Collections.sort(objects, Collections.reverseOrder(new ProjectProfileComparator()));
        List<ProjectProfile> limitedProjectProfiles = new ArrayList<ProjectProfile>();
        for(int i=0; (i < limit) && (i < objects.size()); i++)
            limitedProjectProfiles.add(objects.get(i));
        return limitedProjectProfiles;
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
