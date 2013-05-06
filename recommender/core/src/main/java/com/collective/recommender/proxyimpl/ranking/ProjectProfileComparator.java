package com.collective.recommender.proxyimpl.ranking;

import com.collective.model.profile.ProjectProfile;

import java.util.Comparator;

public class ProjectProfileComparator implements Comparator<ProjectProfile> {

    public int compare(
            ProjectProfile projectProfile,
            ProjectProfile projectProfile1
    ) {
        if (projectProfile.getId() > projectProfile1.getId())
            return 1;
        if (projectProfile.getId() < projectProfile1.getId())
            return -1;
        return 0;
    }

    public boolean equals(Object o) {
        return false;
    }

}
