package com.collective.resources.utils;

import com.collective.concepts.core.Concept;
import com.collective.dynamicprofile.model.DynamicUserProfile;
import com.collective.dynamicprofile.model.Interest;
import com.collective.permanentsearch.model.LabelledURI;
import com.collective.resources.search.model.SearchCreationBean;
import com.collective.usertests.model.UserFeedback;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class DomainFixtures
{
    public static UserFeedback getUserFeedback(long userId)
    {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUrlResource("http://www.fake.com");
        userFeedback.setProjectId(4235262L);
        userFeedback.setLike(false);
        userFeedback.setReasonId(4);

        userFeedback.setUserId(userId);

        return userFeedback;
    }

    public static SearchCreationBean getSearchCreationBean(long userId)
    {
        SearchCreationBean searchCreationBean = new SearchCreationBean();
        searchCreationBean.setUserId(userId);
        searchCreationBean.setTitle("fake title created by user " + Long.toString(userId));

        ArrayList<LabelledURI> commonConcepts = new ArrayList<LabelledURI>();
        commonConcepts.add(getURILabel("common label"));

        ArrayList<LabelledURI> customConcepts = new ArrayList<LabelledURI>();
        customConcepts.add(getURILabel("custom label"));

        searchCreationBean.setCommonConcepts(commonConcepts);
        searchCreationBean.setCustomConcepts(customConcepts);
        return searchCreationBean;
    }

    private static LabelledURI getURILabel(String labelName)
    {
        LabelledURI labelledUri = new LabelledURI();
        labelledUri.setLabel(labelName);
        try {
            String noSpaces = labelName.replaceAll("\\s", "");
            labelledUri.setUrl(new URI("http://firstdomain.com/" + noSpaces));
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return labelledUri;
    }

    public static Concept getConcept(long userId)
    {
        Concept concept = new Concept("company" + Long.toString(userId),
                                      "owner" + Long.toString(userId),
                                      "name" + Long.toString(userId),
                                      "label" + Long.toString(userId));

        return concept;
    }

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
