package com.jbsoft.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

	private static final String url_template = "jdbc:<dbtype>://<host>/<database>";
	private static final String oracle_url_template = "jdbc:oracle:thin:@(description=(address=(host=<host>)(protocol=tcp)(port=<port>))(connect_data=(service_name=<database>)))";
	private static final String sqlserver_url_template = "jdbc:sqlserver://<host>;databaseName=<database>;encrypt=false;trustServerCertificate=false";
	private static final String mysql_url_template = "jdbc:mysql://<host>/<database>";

	public static void main(String[] args) throws ClassNotFoundException {
		String user = "";
		String password = "";
		String host = "";
		String dbtype = "";
		String table = "";
		String column = "";
		String database = "";
		int limit = 1;

		try {
			dbtype = args[0];
			host = args[1];
			database = args[2];
			user = args[3];
			password = args[4];
			table = args[5];
			column = args[6];
			limit = Integer.parseInt(args[7]);

		} catch (Exception e) {
			System.out.println(
					"Ex.: java -jar dbtest.jar <dbtype> <host> <database> <user> <password> <table> <column> <limit>");
			System.out.println("dbtypes: postgresql, oracle, mySql or sqlserver");
			return;
		}
		try {
			String url = "";
			if (dbtype.equalsIgnoreCase("oracle")) {
				Class.forName("oracle.jdbc.OracleDriver");
				String[] hostparts = host.split(":");
				url = oracle_url_template.replace("<host>", hostparts[0]).replace("<port>", hostparts[1]).replace("<database>", database);
			} if(dbtype.equalsIgnoreCase("sqlserver")){
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				url = sqlserver_url_template.replace("<host>", host).replace("<database>", database);
			} if(dbtype.equalsIgnoreCase("mysql")){
				Class.forName("com.mysql.cj.jdbc.Driver");
				url = mysql_url_template.replace("<host>", host).replace("<database>", database);
			} else if(dbtype.equalsIgnoreCase("postgresql")) {
				url = url_template.replace("<dbtype>", dbtype).replace("<host>", host).replace("<database>", database);
			}
			System.out.println("Connection URL: "+url);
			Connection connection = DriverManager.getConnection(url, user, password);
			

			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT " + column + " FROM " + table);
			System.out.println(preparedStatement);
			preparedStatement.setMaxRows(limit);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String value = rs.getObject(column).toString();
				System.out.println(value);
			}
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	public static void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
