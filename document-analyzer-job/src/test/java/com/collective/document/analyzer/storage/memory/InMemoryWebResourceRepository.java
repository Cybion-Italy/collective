package com.collective.document.analyzer.storage.memory;

import com.collective.analyzer.storage.WebResourceRepository;
import com.collective.analyzer.storage.WebResourceRepositoryException;
import com.collective.document.analyzer.utils.DomainFixtures;
import com.collective.model.persistence.WebResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class InMemoryWebResourceRepository implements WebResourceRepository
{

    List<WebResource> webResources = new ArrayList<WebResource>();

    public List<WebResource> selectWebResourcesToAnalyse(int amount)
            throws WebResourceRepositoryException
    {
        //builds amount webResources
        List<WebResource> toAnalyseWebResources =
                DomainFixtures.getNotAnalysedWebResources(amount);
        //save it for external inspection
        webResources.addAll(toAnalyseWebResources);
        return toAnalyseWebResources;
    }

    public void update(WebResource webResource) {
        //from the webResources held in memory, update the webResource at key = id
        for (WebResource actualWebResource : webResources) {
            if (actualWebResource.getId().equals(webResource.getId())) {
                actualWebResource = webResource;
            }
        }
    }

    public List<WebResource> getWebResources() {
        return webResources;
    }
}
