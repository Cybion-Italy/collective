package com.collective.persistencewebresources.persistence;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/*
import com.ibatis.common.resources.Resources;
import com.ibatis.dao.client.DaoManager;
*/



public class PersistenceFixture {
	
	/* change com.collective.persistencewebresources.properties ad hoc for database tests NOT USED */
	/*
	private static final String driver = "org.hsqldb.jdbcDriver";
	private static final String url = "jdbc:hsqldb:mem:testfixture";
	private static final String username = "sa";
	private static final String password = "";
	 */
	
//	private static final DaoManager daoManager;
	private static final SqlSessionFactory sqlSessionFactory;

	static {
		try {
			/* read com.collective.persistencewebresources.properties from file old mybatis */
			/*
			daoManager = DaoConfig.newDaoManager(null);
			Properties props = Resources.getResourceAsProperties("com.collective.persistencewebresources.properties/database.com.collective.persistencewebresources.properties");
			String url = props.getProperty("url");
			String driver = props.getProperty("driver");
			String username = props.getProperty("username");
			String password = props.getProperty("password");
			
			*/
			
			/* mybatis 3.0.1 */

			String resource = "it/cybion/collective/persistence/sqlmapdao/sql/sql-map-config.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			// ad hoc DAO Manager Configuration
			/*
			Properties props = new Properties();
			props.setProperty("driver", driver);
			props.setProperty("url", url);
			props.setProperty("username", username);
			props.setProperty("password", password);

			daoManager = DaoConfig.newDaoManager(props);
*/
			
			// Test Database Initialization, not used

			/*
	      Connection conn = DriverManager.getConnection(url, username, password);
	      try {
	        ScriptRunner runner = new ScriptRunner(conn, false, false);
	        runner.setErrorLogWriter(null);
	        runner.setLogWriter(null);
	        runner.runScript(Resources.getResourceAsReader("ddl/hsql/jpetstore-hsqldb-schema.sql"));
	        runner.runScript(Resources.getResourceAsReader("ddl/hsql/jpetstore-hsqldb-dataload.sql"));
	      } finally {
	        conn.close();
	      }
			 */

		} catch (Exception e) {
			throw new RuntimeException("Description.  Cause: " + e, e);
		}
	}

	/*ibatis 2.x oLd*/
	/*
	public static DaoManager getDaoManager() {
		return daoManager;
	}
	*/
	
	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

}