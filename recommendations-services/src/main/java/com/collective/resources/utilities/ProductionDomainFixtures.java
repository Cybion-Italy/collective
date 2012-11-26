package com.collective.resources.utilities;

import com.collective.dynamicprofile.model.DynamicUserProfile;
import com.collective.dynamicprofile.model.Interest;
import com.collective.permanentsearch.model.LabelledURI;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ProductionDomainFixtures
{
    public static DynamicUserProfile getDynamicUserProfile(String profileUri,
                                                           DateTime startDate,
                                                           DateTime endDate,
                                                           DateTime lastUpdatedAt,
                                                           int interestsAmount)
            throws URISyntaxException
    {
        Set<Interest> interests = new HashSet<Interest>();

        for (int i = 1; i <= interestsAmount; i++) {
            Interest interest = createAnInterest(i);
            interests.add(interest);
        }

        String entityId = profileUri;
        Interval longTermInterval = new Interval(startDate, endDate);
        DynamicUserProfile dup = new DynamicUserProfile(entityId, interests, longTermInterval);
        dup.setLastUpdatedAt(lastUpdatedAt);

        return dup;

    }

    private static Interest createAnInterest(int i) throws URISyntaxException
    {
        LabelledURI concept = new LabelledURI();
        concept.setLabel("concept " + i);
        concept.setUrl(new URI("http://www.concept.it/" + i));
        return new Interest(new Float(i).floatValue(), concept);
    }


}
