package com.collective.recommender.dynamicprofiler;

import com.collective.model.profile.UserProfile;

import java.net.URI;
import java.util.List;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ShortTermUserProfile extends UserProfile {

    @Override
    public List<URI> getInterests() {
        throw new UnsupportedOperationException("NIY");
    }
}
