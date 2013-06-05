package com.collective.persistencewebresources.persistence.dao;

import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;
import com.collective.persistencewebresources.persistence.ConnectionFactory;
import com.collective.persistencewebresources.persistence.mappers.WebResourceMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WebResourceDao extends ConfigurableDao {

    private static final Logger LOGGER = Logger.getLogger(WebResourceDao.class);

    public WebResourceDao(Properties properties) {
        super(properties);
    }

    public List<WebResource> selectAll() {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            List<WebResource> webResources = webResourceMapper.selectAllUrls();
            return webResources;
        } finally {
            session.close();
        }
    }

    public WebResource select(Integer id) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            WebResource webResource = webResourceMapper.selectUrl(id);
            return webResource;
        } finally {
            session.close();
        }
    }

    public void insert(WebResource webResource) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            webResourceMapper.insertUrl(webResource);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void delete(WebResource webResource) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            webResourceMapper.deleteUrl(webResource.getId());
            session.commit();
        } finally {
            session.close();
        }
    }

    //TODO test
    public List<WebResource> selectLastExtractedFrom(SourceRss sourceRss) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        List<WebResource> webResources = new ArrayList<WebResource>();
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            webResources.addAll(webResourceMapper.selectLastExtractedUrls(sourceRss));
        } finally {
            session.close();
        }
        return webResources;
    }

    public List<WebResource> selectByExample(WebResource notAnalyzedWebResource, int maxUrls) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
//        RowBounds rowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, maxUrls);
//        LOGGER.debug("rowBounds: " + rowBounds.getLimit());
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            List<WebResource> webResources = webResourceMapper.selectSomeUrlsByExample(
                    notAnalyzedWebResource,
                    maxUrls
            );
            return webResources;
        } finally {
            session.close();
        }
    }

    public void update(WebResource webResource) {
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();
        try {
            WebResourceMapper webResourceMapper = session.getMapper(WebResourceMapper.class);
            webResourceMapper.updateUrl(webResource);
            session.commit();
        } finally {
            session.close();
        }
    }
}
