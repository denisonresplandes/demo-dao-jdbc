package db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DB {

	private static Connection connection = null;
	
	public static Connection getConnection() {
		if (Objects.isNull(connection)) {
			try {
				Properties props = loadProperties();
				
				String url = props.getProperty("dburl");

				connection = DriverManager.getConnection(url, props);
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return connection;
	}
	
	public static void closeConnection() {
		if (Objects.nonNull(connection)) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (Objects.nonNull(rs)) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void rollback() {
		if (Objects.nonNull(connection)) {
			try {
				connection.rollback();
			}
			catch (SQLException e) {
				throw new DbException("Error trying to rollback! Caused by: " + 
						e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		Properties props = new Properties();
		try (InputStream inputStream = Files
				.newInputStream(Paths.get("db.properties"))) {
			props.load(inputStream);
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
		return props;
	}
}