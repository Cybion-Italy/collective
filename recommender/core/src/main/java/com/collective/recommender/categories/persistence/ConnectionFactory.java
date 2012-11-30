package com.collective.recommender.categories.persistence;

import com.collective.recommender.categories.persistence.mappers.MappedResourceMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.Properties;

/**
 * @author Matteo Moci ( matteo (dot) moci (at) gmail (dot) com )
 */
public class ConnectionFactory {


    private static SqlSessionFactory sqlMapper;

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
            Environment environment = new Environment("development", transactionFactory, pds);
            Configuration configuration = new Configuration(environment);

            /* mappers */
            configuration.addMapper(MappedResourceMapper.class);

            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
        return sqlMapper;
    }
}
