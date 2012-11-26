package com.collective.recommender.proxy.ranking;

import com.collective.model.profile.ProjectProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ProjectProfileRanker implements Ranker<ProjectProfile> {

    private int limit = 10;

    public List<ProjectProfile> rank(List<ProjectProfile> objects) throws RankerException {
        Collections.sort(objects, Collections.reverseOrder(new ProjectProfileComparator()));
        List<ProjectProfile> limitedProjectProfiles = new ArrayList<ProjectProfile>();
        for(int i=0; (i < limit) && (i < objects.size()); i++)
            limitedProjectProfiles.add(objects.get(i));
        return limitedProjectProfiles;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

}
