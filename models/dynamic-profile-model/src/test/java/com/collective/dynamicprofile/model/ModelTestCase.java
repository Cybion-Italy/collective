package com.collective.dynamicprofile.model;

import com.collective.permanentsearch.model.LabelledURI;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ModelTestCase
{
    @Test
    public void shouldTestAPI() throws URISyntaxException
    {
        Set<Interest> interests = new HashSet<Interest>();
        LabelledURI concept = new LabelledURI();
        concept.setLabel("google");
        concept.setUrl(new URI("http://www.google.it"));

        Interest interest = new Interest(5.3F, concept);
        interests.add(interest);

        String entityId = "http://collective.com/user/1";
        DateTime twoMonthsAgo = new DateTime().minusDays(60);
        DateTime oneWeekAgo = new DateTime().minusDays(7);
        Interval longTermInterval = new Interval(twoMonthsAgo, oneWeekAgo);
        DynamicUserProfile longTermInterests = new DynamicUserProfile(entityId, interests, longTermInterval);
        longTermInterests.setLastUpdatedAt(new DateTime());
        System.out.println(longTermInterests);

        DateTime twoWeeksAgo = new DateTime().minusDays(14);
        DateTime now = new DateTime();
        Interval shortTermInterval = new Interval(twoWeeksAgo, now);
        DynamicUserProfile shortTermInterests = new DynamicUserProfile(entityId, interests, shortTermInterval);
        shortTermInterests.setLastUpdatedAt(new DateTime());
        System.out.println(shortTermInterests);
    }
}
