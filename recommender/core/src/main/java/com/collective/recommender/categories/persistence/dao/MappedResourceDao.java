package com.collective.recommender.categories.persistence.dao;

import com.collective.recommender.categories.exceptions.DaoException;
import com.collective.recommender.categories.model.MappedResource;
import com.collective.recommender.categories.persistence.ConnectionFactory;
import com.collective.recommender.categories.persistence.mappers.MappedResourceMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class MappedResourceDao extends ConfigurableDao {

    private static final Logger LOGGER = Logger.getLogger(MappedResourceDao.class);

    public MappedResourceDao(Properties properties) {
        super(properties);
    }

    public List<MappedResource> getLatestMappedResources(Long userId, int limit) throws DaoException {

        LOGGER.debug("");
        SqlSession session = ConnectionFactory.getSession(super.properties).openSession();

        try {
            MappedResourceMapper mappedResourceMapper = session.getMapper(MappedResourceMapper.class);
            List<MappedResource> mappedResources = mappedResourceMapper.selectLatestMappingsByUserId(userId, limit);
            return mappedResources;
        } catch (Exception e) {
            String emsg = "error while getting latest mapped resources for userId " + userId;
            throw new DaoException(emsg, e);
        } finally {
            session.close();
        }
    }
}
