package es.prodevelop.pui9.model.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Some useful database utilities.
 * 
 * @author Marc Gil (mgil@prodevelop.es)
 */
@Component
public class DatabaseUtils {

	private static final String PostgreSQL = "PostgreSQL";
	private static final String Oracle = "Oracle";
	private static final String SqlServer = "Microsoft SQL Server";
	private static final String HyperSQL = "HSQL Database Engine";

	@Autowired
	@Qualifier("dataSource")
	private DataSource datasource;

	private String databaseName;

	@PostConstruct
	private void postConstruct() throws SQLException {
		Connection conn = datasource.getConnection();
		databaseName = conn.getMetaData().getDatabaseProductName();
		conn.close();
	}

	/**
	 * Get the main datasource of the application
	 * 
	 * @return The datasource
	 */
	public DataSource getMainDatasource() {
		return datasource;
	}

	/**
	 * Check if the used database is Oracle
	 * 
	 * @return true if it's Oracle; false if not
	 */
	public boolean isOracle() {
		return databaseName.equals(Oracle);
	}

	/**
	 * Check if the used database is PostgreSql
	 * 
	 * @return true if it's PostgreSql; false if not
	 */
	public boolean isPostgreSql() {
		return databaseName.equals(PostgreSQL);
	}

	/**
	 * Check if the used database is SqlServer
	 * 
	 * @return true if it's SqlServer; false if not
	 */
	public boolean isSqlServer() {
		return databaseName.equals(SqlServer);
	}

	/**
	 * Check if the used database is HyperSql
	 * 
	 * @return true if it's HyperSql; false if not
	 */
	public boolean isHyperSql() {
		return databaseName.equals(HyperSQL);
	}

	/**
	 * Check if the used database supports ElasticSearch or not. Currently
	 * PostgreSql doesn't support it due to some errors on evaluating the Views
	 * 
	 * @return true if ElasticSearch is supported; false if not
	 */
	public boolean supportsElasticSearch() {
		return !isPostgreSql();
	}

}
