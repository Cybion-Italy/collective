package com.collective.recommender.categories.persistence;

import com.collective.recommender.categories.exceptions.CategoriesMappingStorageException;
import com.collective.recommender.categories.exceptions.DaoException;
import com.collective.recommender.categories.model.MappedResource;
import com.collective.recommender.categories.persistence.dao.MappedResourceDao;

import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class MybatisCategoriesMappingStorage implements CategoriesMappingStorage {

    private MappedResourceDao mappedResourceDao;

    public MybatisCategoriesMappingStorage(Properties properties) {
        this.mappedResourceDao = new MappedResourceDao(properties);
    }

    @Override
    public List<MappedResource> getLatestMappedResources(Long userId, int amount) throws CategoriesMappingStorageException {
        try {
            return mappedResourceDao.getLatestMappedResources(userId, amount);  //To change body of implemented methods use File | Settings | File Templates.
        } catch (DaoException e) {
            final String emsg = "cant get mapped resource for user '" + userId + "'";
            throw new CategoriesMappingStorageException(emsg, e);
        }
    }
}
