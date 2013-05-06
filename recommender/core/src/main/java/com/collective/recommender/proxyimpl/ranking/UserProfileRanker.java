package com.collective.recommender.proxyimpl.ranking;

import com.collective.model.profile.UserProfile;
import com.collective.recommender.proxy.ranking.Ranker;
import com.collective.recommender.proxy.ranking.RankerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserProfileRanker implements Ranker<UserProfile> {

    private int limit = 10;

    @Override
    public List<UserProfile> rank(List<UserProfile> objects) throws RankerException {
        List<UserProfile> limitedUsers = new ArrayList<UserProfile>();
        for(int i=0; (i < limit) && (i < objects.size()); i++)
            limitedUsers.add(objects.get(i));
        return limitedUsers;
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
