package com.collective.importer.indexer.persistence;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ConnectionFactory {

    private static SqlSessionFactory sqlMapper;

    private final static String resource = "sql-map-config-suggest.xml";

    static {
        InputStream stream = ConnectionFactory.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            throw new RuntimeException("sql-map-config-feed.xml not found");
        }
        InputStreamReader reader = new InputStreamReader(stream);
        sqlMapper = new SqlSessionFactoryBuilder().build(reader);
    }

    public static SqlSessionFactory getSession() {
        return sqlMapper;
    }

}