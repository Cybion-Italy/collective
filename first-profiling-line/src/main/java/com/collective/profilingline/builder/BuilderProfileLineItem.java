package com.collective.profilingline.builder;

import com.collective.model.User;
import com.collective.model.profile.UserProfile;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.model.profile.Profile;
import com.collective.profilingline.knowledge.KeywordsProfilingLineItem;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This {@link com.collective.profiler.line.ProfilingLineItem} is responsible
 * to build a concrente instance of {@link com.collective.model.profile.Profile} and
 * should be the last item of a {@link com.collective.profiler.line.ProfilingLine}.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci@gmail.com )
 */
public class BuilderProfileLineItem extends ProfilingLineItem {

    private final static String PREFIX = "http://collective.com/profile/user/";

    private final static Logger logger = Logger.getLogger(BuilderProfileLineItem.class);

    public BuilderProfileLineItem(String name, String description) {
        super(name, description);
    }

    public void execute(Object o) throws ProfilingLineItemException {
        Map<String, Object> rawInput = (Map<String, Object>) o;
        UserProfile resultProfile = new UserProfile();
        User user = (User) rawInput.get(KeywordsProfilingLineItem.USER_KEY);
        try {
            resultProfile.setId(user.getId());
        } catch (URISyntaxException e) {
            String errMsg = "Error. URI: '" + PREFIX + user.getId().toString()  + "' is not well-formed.";
            logger.error(errMsg, e);
            throw new ProfilingLineItemException(errMsg, e);
        }
        List<URI> interests = (List<URI>) rawInput.get(KeywordsProfilingLineItem.INTERESTS_DBPEDIA_KEY);
        resultProfile.setInterests(interests);

        List<URI> skills = (List<URI>) rawInput.get(KeywordsProfilingLineItem.SKILLS_DBPEDIA_KEY);
        resultProfile.setSkills(skills);

        super.next.execute(resultProfile);
    }
}
