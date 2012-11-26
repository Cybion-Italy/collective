package com.collective.analyzer.storage;

import com.collective.model.persistence.WebResource;
import com.collective.persistencewebresources.persistence.dao.WebResourceDao;

import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class MyBatisWebResourceRepository implements WebResourceRepository {

    WebResourceDao webResourceDao;

    public MyBatisWebResourceRepository(Properties properties) {
        this.webResourceDao = new WebResourceDao(properties);
    }

    public List<WebResource> selectWebResourcesToAnalyse(int amount) {
        WebResource notAnalyzedWebResource = new WebResource();
        notAnalyzedWebResource.setAnalyzed(false);
        return this.webResourceDao.selectByExample(notAnalyzedWebResource,
                                                           amount);
    }

    public void update(WebResource webResource) {
        this.webResourceDao.update(webResource);
    }
}
