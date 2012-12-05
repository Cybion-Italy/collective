package com.collective.recommender.dynamicprofiler;

import com.collective.model.profile.UserProfile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ShortTermUserProfile extends UserProfile {

    private List<URI> interests = new ArrayList<URI>();

    public ShortTermUserProfile(List<URI> interests) {
        if (interests == null) {
            throw new IllegalArgumentException("param cant be null");
        }
        this.interests = interests;
    }

    @Override
    public List<URI> getInterests() {
        return this.interests;
    }
}
