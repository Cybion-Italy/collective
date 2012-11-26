package com.collective.profiler.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci (matteo.moci (at) gmail.com )
 */
public class ModularDataManager extends AbstractDataManager {

    private Map<String, DataSource> datasources = new HashMap<String, DataSource>();

    public ModularDataManager(DataManagerConfiguration dataManagerConfiguration)
            throws DataManagerException {
        super(dataManagerConfiguration);
        for (String key : dataManagerConfiguration.getRegisteredDataSources().keySet()) {
            Class dataSourceClassName = dataManagerConfiguration.getDataSource(key);
            DataSource dataSource;
            try {
                dataSource = (DataSource) dataSourceClassName.newInstance();
            } catch (InstantiationException e) {
                throw new DataManagerException("Error while instantiating the proper DataSource", e);
            } catch (IllegalAccessException e) {
                throw new DataManagerException("Error while accessing the constructor of DataSource", e);
            }
            try {
                dataSource.init(dataManagerConfiguration.getMessagesPersistenceConfiguration().getProperties());
            } catch (DataSourceException e) {
                throw new DataManagerException(
                        "Error while initializing data source associated with key:'" + key + "'", 
                        e
                );
            }
            datasources.put(key, dataSource);
        }
    }

    @Override
    public RawDataSet getRawData(String key) throws DataManagerException {
        try {
            return datasources.get(key).getRawData();
        } catch (DataSourceException e) {
            throw new DataManagerException("Error while getting raw data for key: '" + key + "'");
        }
    }
}
