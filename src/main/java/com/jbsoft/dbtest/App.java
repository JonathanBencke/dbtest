package com.jbsoft.dbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {

	private static final String url_template = "jdbc:<dbtype>://<host>/<database>";
	
	public static void main(String[] args) {
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
			
		}catch (Exception e) {
			System.out.println("Ex.: java -jar dbtest.jar <dbtype> <host> <database> <user> <password> <table> <column> <limit>");
			System.out.println("dbtypes: postgresql, oracle or sqlserver");
			return;
		}
		
		String url = url_template.replace("<dbtype>", dbtype).replace("<host>", host).replace("<database>", database);
		
		try (Connection connection = DriverManager.getConnection(url, user, password);
				
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT "+column+" FROM "+table+";")) {
			System.out.println(preparedStatement);
			preparedStatement.setMaxRows(limit);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String login = rs.getString("idlogin");
				System.out.println(login);
			}
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
