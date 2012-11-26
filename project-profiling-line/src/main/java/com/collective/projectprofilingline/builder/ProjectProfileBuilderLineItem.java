package com.collective.projectprofilingline.builder;

import com.collective.model.Project;
import com.collective.model.ProjectInvolvement;
import com.collective.model.profile.ProjectProfile;
import com.collective.profiler.line.ProfilingLineItem;
import com.collective.profiler.line.ProfilingLineItemException;
import com.collective.projectprofilingline.knowledge.ProjectManifestoKeywordsProfilingLineItem;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
public class ProjectProfileBuilderLineItem extends ProfilingLineItem {

    private final static String PROJECT_PROFILE_PREFIX = "http://collective.com/profile/project/";

    private final static String USER_PROFILE_PREFIX = "http://collective.com/profile/user/";

    private final static Logger logger = Logger.getLogger(ProjectProfileBuilderLineItem.class);

    public ProjectProfileBuilderLineItem(String name, String description) {
        super(name, description);
    }

    public void execute(Object o) throws ProfilingLineItemException {
        Map<String, Object> rawInput = (Map<String, Object>) o;
        ProjectProfile resultProjectProfile = new ProjectProfile();
        Project project = (Project) rawInput.get(ProjectManifestoKeywordsProfilingLineItem.PROJECT_KEY);
        try {
            resultProjectProfile.setId(project.getId());
        } catch (URISyntaxException e) {
            String errMsg = "Error. URI: '" + PROJECT_PROFILE_PREFIX + project.getId().toString()  + "' is not well-formed.";
            logger.error(errMsg, e);
            throw new ProfilingLineItemException(errMsg, e);
        }
        List<URI> manifestoConcepts =
                (List<URI>) rawInput.get(ProjectManifestoKeywordsProfilingLineItem.MANIFESTO_CONCEPTS_DBPEDIA_KEY);
        resultProjectProfile.setManifestoConcepts(manifestoConcepts);
        List<String> members = project.getMembersIds();
        List<ProjectInvolvement> projectInvolvements = new ArrayList<ProjectInvolvement>();
        for(String member : members) {
            ProjectInvolvement projectInvolvment = new ProjectInvolvement();
            projectInvolvment.setId(UniqueID.get());
            projectInvolvment.setRole("participant");
            try {
                projectInvolvment.setUserProfile(new URI(USER_PROFILE_PREFIX + member));
            } catch (URISyntaxException e) {
                String errMsg = "Error. URI: '" + USER_PROFILE_PREFIX + member + "' is not well-formed.";
                logger.error(errMsg, e);
                throw new ProfilingLineItemException(errMsg, e);
            }
            projectInvolvements.add(projectInvolvment);
        }
        resultProjectProfile.setProjectInvolvements(projectInvolvements);
        super.next.execute(resultProjectProfile);
    }

    private URI getProfileIdAsURI(UUID uuid) throws URISyntaxException {
        return new URI(PROJECT_PROFILE_PREFIX + uuid.toString());
    }

    private static class UniqueID {

        static long current = System.currentTimeMillis();

        static public synchronized long get() {
            return current++;
        }
    }
}
