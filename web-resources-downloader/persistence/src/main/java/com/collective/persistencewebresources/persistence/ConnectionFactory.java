package com.collective.persistencewebresources.persistence;

import java.io.Reader;
import java.net.URL;
import java.util.Properties;

import com.collective.model.persistence.Source;
import com.collective.model.persistence.SourceRss;
import com.collective.model.persistence.WebResource;

import com.collective.persistencewebresources.persistence.mappers.SourceMapper;
import com.collective.persistencewebresources.persistence.mappers.SourceRssMapper;
import com.collective.persistencewebresources.persistence.mappers.WebResourceMapper;
import com.collective.persistencewebresources.persistence.utils.typehandlers.JodaTimeTypeHandler;
import com.collective.persistencewebresources.persistence.utils.typehandlers.URLTypeHandler;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class ConnectionFactory {

    private static SqlSessionFactory sqlMapper;
    private final static Logger logger = Logger.getLogger(ConnectionFactory.class);

    /* All mybatis configuration is programmatically done here */

    public static SqlSessionFactory getSession(Properties properties) {
        if (sqlMapper == null) {
            PooledDataSource pds = new PooledDataSource(
                    "com.mysql.jdbc.Driver",
                    properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password")
            );
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment =
                    new Environment("development", transactionFactory, pds);
            Configuration configuration = new Configuration(environment);

            /* aliases */
            configuration.getTypeAliasRegistry().registerAlias("webResource", WebResource.class);
            configuration.getTypeAliasRegistry().registerAlias("sourcerss", SourceRss.class);
            configuration.getTypeAliasRegistry().registerAlias("source", Source.class);
            configuration.getTypeAliasRegistry().registerAlias("datetime", DateTime.class);
            configuration.getTypeAliasRegistry().registerAlias("url", URL.class);

            /* custom typeHandlers */
            configuration.getTypeHandlerRegistry().register(DateTime.class, JdbcType.BIGINT, new JodaTimeTypeHandler());
            configuration.getTypeHandlerRegistry().register(URL.class, JdbcType.VARCHAR, new URLTypeHandler());

            /* mappers */
            configuration.addMapper(SourceMapper.class);
            configuration.addMapper(SourceRssMapper.class);
            configuration.addMapper(WebResourceMapper.class);

            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
//        logger.debug("SqlSession correctly instantiated" + sqlMapper.toString());
        return sqlMapper;
    }

}