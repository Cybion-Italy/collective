package com.collective.profiler.data;

import java.util.Properties;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public interface DataSource {

    public void init(Properties properties) throws DataSourceException;

    public void dispose() throws DataSourceException;

    public RawDataSet getRawData() throws DataSourceException;

}
