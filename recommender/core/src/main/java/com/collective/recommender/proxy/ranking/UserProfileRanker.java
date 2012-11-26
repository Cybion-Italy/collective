package com.collective.recommender.proxy.ranking;

import com.collective.model.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class UserProfileRanker implements Ranker<UserProfile> {

    private int limit = 10;

    public List<UserProfile> rank(List<UserProfile> objects) throws RankerException {
        List<UserProfile> limitedUsers = new ArrayList<UserProfile>();
        for(int i=0; (i < limit) && (i < objects.size()); i++)
            limitedUsers.add(objects.get(i));
        return limitedUsers;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

}
