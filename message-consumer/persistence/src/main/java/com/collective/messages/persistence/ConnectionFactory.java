package com.collective.messages.persistence;

import com.collective.messages.persistence.mappers.MessageMapper;
import com.collective.messages.persistence.model.Message;
import com.collective.messages.persistence.utils.typehandlers.JodaTimeTypeHandler;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Properties;

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
            Environment environment =
                    new Environment("development", transactionFactory, pds);
            Configuration configuration = new Configuration(environment);

            /* aliases */
            configuration.getTypeAliasRegistry().registerAlias("datetime", DateTime.class);
            configuration.getTypeAliasRegistry().registerAlias("message", Message.class);

            /* custom typeHandlers */
            configuration.getTypeHandlerRegistry().register(DateTime.class, JdbcType.BIGINT, new JodaTimeTypeHandler());

            /* mappers */
            configuration.addMapper(MessageMapper.class);

            sqlMapper = new SqlSessionFactoryBuilder().build(configuration);
        }
//        logger.info("SqlSession correctly instantiated");
        return sqlMapper;
    }

}