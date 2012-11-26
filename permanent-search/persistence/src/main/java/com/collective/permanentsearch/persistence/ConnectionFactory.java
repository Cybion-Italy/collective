package com.collective.permanentsearch.persistence;

import com.collective.permanentsearch.model.LabelledURI;
import com.collective.permanentsearch.model.Search;
import com.collective.permanentsearch.persistence.mappers.SearchMapper;
import com.collective.permanentsearch.persistence.typehandlers.JodaTimeTypeHandler;
import com.collective.permanentsearch.persistence.typehandlers.LabelledURIArrayListTypeHandler;
import com.google.gson.reflect.TypeToken;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
public class ConnectionFactory {

    private static SqlSessionFactory sqlMapper;

    private static final Logger logger = Logger.getLogger(ConnectionFactory.class);

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

            List<LabelledURI> t = new ArrayList<LabelledURI>();
            Class cOfT = t.getClass();

            /* aliases */
            configuration.getTypeAliasRegistry().registerAlias("datetime",
                    DateTime.class);
            configuration.getTypeAliasRegistry().registerAlias("search",
                    Search.class);

            configuration.getTypeAliasRegistry().registerAlias("labelledUrisList",
                                cOfT);

            /* custom typeHandlers */
            configuration.getTypeHandlerRegistry().register(DateTime.class,
                    JdbcType.BIGINT, new JodaTimeTypeHandler());

            configuration.getTypeHandlerRegistry().register(
                        cOfT,
                        JdbcType.VARCHAR,
                        new LabelledURIArrayListTypeHandler());

            /* mappers */
            configuration.addMapper(SearchMapper.class);

            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
//        logger.info("SqlSession correctly instantiated");
        return sqlMapper;
    }

}