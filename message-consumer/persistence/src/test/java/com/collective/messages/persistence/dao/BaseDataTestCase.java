package com.collective.messages.persistence.dao;

/* taken from http://code.google.com/p/mybatis/source/browse/trunk/src/test/java/org/apache/ibatis/BaseDataTest.java */

/*
 *    Copyright 2009-2012 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class BaseDataTestCase {

    public static final String DRIVER = "driver";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static UnpooledDataSource createUnpooledDataSource(String resource)
            throws IOException {
        Properties props = Resources.getResourceAsProperties(resource);
        UnpooledDataSource ds = createUnpooledDataSource(props);
        return ds;
    }

    private static UnpooledDataSource createUnpooledDataSource(Properties props) {
        UnpooledDataSource ds = new UnpooledDataSource();
        ds.setDriver(props.getProperty(DRIVER));
        ds.setUrl(props.getProperty(URL));
        ds.setUsername(props.getProperty(USERNAME));
        ds.setPassword(props.getProperty(PASSWORD));
        return ds;
    }

    public static PooledDataSource createPooledDataSource(String resource)
            throws IOException {
        Properties props = Resources.getResourceAsProperties(resource);
        PooledDataSource ds = createPooledDataSource(props);
        return ds;
    }

    public static PooledDataSource createPooledDataSource(Properties props) {
        PooledDataSource ds = new PooledDataSource();
        ds.setDriver(props.getProperty(DRIVER));
        ds.setUrl(props.getProperty(URL));
        ds.setUsername(props.getProperty(USERNAME));
        ds.setPassword(props.getProperty(PASSWORD));
        return ds;
    }

    public static void runScript(DataSource ds, String resource)
            throws IOException, SQLException {
        Connection connection = ds.getConnection();
        try {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runScript(runner, resource);
        } finally {
            connection.close();
        }
    }

    public static void runScript(ScriptRunner runner, String resource)
            throws IOException, SQLException {
        Reader reader = Resources.getResourceAsReader(resource);
        try {
            runner.runScript(reader);
        } finally {
            reader.close();
        }
    }
}