package com.collective.usertests.persistence;

import com.collective.usertests.model.Reason;
import com.collective.usertests.model.UserFeedback;
import com.collective.usertests.persistence.mappers.ReasonMapper;
import com.collective.usertests.persistence.mappers.UserFeedbackMapper;
import com.collective.usertests.persistence.typehandlers.JodaTimeTypeHandler;
import org.joda.time.DateTime;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.log4j.Logger;
import java.util.Properties;


/**
 * @author Matteo Moci ( matteo.moci (at) gmail.com )
 */
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
            configuration.getTypeAliasRegistry().registerAlias("reason", Reason.class);
            configuration.getTypeAliasRegistry().registerAlias("userfeedback", UserFeedback.class);
            configuration.getTypeAliasRegistry().registerAlias("long", Long.class);
            configuration.getTypeAliasRegistry().registerAlias("datetime", DateTime.class);

            /* custom typeHandlers */
            configuration.getTypeHandlerRegistry().register(DateTime.class, JdbcType.BIGINT, new JodaTimeTypeHandler());
//            configuration.getTypeHandlerRegistry().register(URL.class, JdbcType.VARCHAR, new URLTypeHandler());

            /* mappers */
            configuration.addMapper(ReasonMapper.class);
            configuration.addMapper(UserFeedbackMapper.class);

            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
//        logger.debug("SqlSession correctly instantiated" + sqlMapper.toString());
        return sqlMapper;
    }

}