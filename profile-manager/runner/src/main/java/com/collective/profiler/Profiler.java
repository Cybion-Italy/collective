package com.collective.profiler;

import com.collective.model.profile.Profile;
import com.collective.profiler.container.ProfilingLineContainer;
import com.collective.profiler.container.ProfilingLineContainerException;
import com.collective.profiler.data.DataManager;
import com.collective.profiler.data.DataManagerException;
import com.collective.profiler.data.RawDataSet;
import com.collective.profiler.line.ProfilingInput;
import com.collective.profiler.line.ProfilingResult;
import com.collective.profiler.storage.ProfileStore;
import com.collective.profiler.storage.ProfileStoreException;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Main class orchestrating all the profiling process.
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Profiler {

    private static Logger logger = Logger.getLogger(Profiler.class);

    private DataManager dataManager;

    private ProfilingLineContainer profilingLineContainer;

    private ProfileStore profileStore;

    public Profiler(
            DataManager dataManager,
            ProfilingLineContainer profilingLineContainer,
            ProfileStore profileStore,
            ProfilerConfiguration profilerConfiguration
    ) {
        this.dataManager = dataManager;
        this.profilingLineContainer = profilingLineContainer;
        this.profileStore = profileStore;
    }

    public void run() throws ProfilerException {
        logger.info("Profiling process started");
        Map<String, List<String>> registeredDataKeys;
        try {
            registeredDataKeys = dataManager.getRegisteredKeys();
        } catch (DataManagerException e) {
            throw new ProfilerException("Error while accessing to the registered keys", e);
        }
        for (String key : registeredDataKeys.keySet()) {
            RawDataSet rawDataSet;
            logger.debug("processing key: " + key);
            try {
                rawDataSet = dataManager.getRawData(key);
            } catch (DataManagerException e) {
                throw new ProfilerException("Error while accessing raw data", e);
            }
            while (rawDataSet.hasNext()) {
                Object objectToProfile = rawDataSet.getNext();

                logger.info("registeredDataKeys.get(key) list of profiling lines for key: " + key + ": " +
                        "" + registeredDataKeys.get(key));

                for (String profilingLine : registeredDataKeys.get(key)) {
                    logger.info("Sending data marked with key: '" + key + "' towards '" + profilingLine + "'.");
                    ProfilingResult profilingResult;
                    try {
                        profilingResult
                                = profilingLineContainer.profile(
                                new ProfilingInput(objectToProfile),
                                profilingLine
                        );
                    } catch (ProfilingLineContainerException e) {
                        final String errMsg = "Error while profiling object: '" +
                                objectToProfile + "' on profiling line '" + profilingLine + "'";
                        logger.error(errMsg, e);
                        throw new ProfilerException(errMsg, e);
                    }

                    Profile profileToStore = null;
                    try {
                        profileToStore = (Profile) profilingResult.getValue();
                        URI graphNamespace = null;
                        try {
                            graphNamespace = profileStore.getConfiguration().getNameSpacesConfiguration().get(key);
                            logger.debug("going to write graphs using namespace: " + graphNamespace.toString());

                            /* delete the uri from its "graph index" */
                            profileStore.deleteProfileFromGraphIndex(
                                    graphNamespace,
                                    new URI(graphNamespace.toString() + profileToStore.getId())
                            );

                            profileStore.deleteGraph(new URI(graphNamespace.toString() + profileToStore.getId()));
                            profileStore.storeProfile(profileToStore, new URI(graphNamespace.toString() + profileToStore.getId()));

                            /* save the uri in the index */
                            profileStore.storeProfileToGraphIndex(
                                    graphNamespace,
                                    new URI(graphNamespace.toString() + profileToStore.getId())
                            );

                        } catch (URISyntaxException e) {
                            final String errMsg = "UriSyntax error while storing profile: '" + profileToStore + "' on profile-store "
                                    + graphNamespace.toString() + " " +
                                    "using uri: " + graphNamespace.toString() + "" + profileToStore.getId();
                            logger.error(errMsg, e);
                            throw new ProfilerException(errMsg, e);
                        }
                    } catch (ProfileStoreException e) {
                        final String errMsg = "Error while storing profile: '" + profileToStore + "' on ProfileStore";
                        logger.error(errMsg, e);
                        throw new ProfilerException(errMsg, e);
                    }
                }
            }
        }
    }
}
