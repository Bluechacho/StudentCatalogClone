package com.fdmgroup.businesslogic;

import java.sql.*;
import java.util.ArrayList;

public abstract class ServerConnector {

	private static String dbName;
	private static String userName;
	private static String password;
	private static String hostname;
	private static String port;
	private static String jdbcUrl;
	private static ArrayList<QueryResult> extractedStudents;
	private static String first = "$def";
	private static String last = "$def";
	private static String ssn = "$def";
	private static boolean searchMode = false;
	private static boolean writeMode = false;

	public static ArrayList<QueryResult> query() {
		if (System.getenv("RDS_HOSTNAME") != null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				extractedStudents = new ArrayList<QueryResult>();
				dbName = System.getenv("RDS_DB_NAME");
				userName = System.getenv("RDS_USERNAME");
				password = System.getenv("RDS_PASSWORD");
				hostname = System.getenv("RDS_HOSTNAME");
				port = System.getenv("RDS_PORT");
				jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password="
						+ password;
				Connection conn = DriverManager.getConnection(jdbcUrl);
				Statement stmt = conn.createStatement();
				String sql;
				ResultSet rs = null;

				if (writeMode) {
					sql = "INSERT INTO ebdb.Students (firstname, lastname, ssn) VALUES ('" + first + "', '" + last
							+ "', '" + ssn + "')";
					stmt.executeUpdate(sql);
					sql = "SELECT * FROM ebdb.Students";
					rs = stmt.executeQuery(sql);

				} else {
					if (searchMode) {
						first = first.concat("%");
						last = last.concat("%");
						ssn = ssn.concat("%");

						if (first.equals("$%")) {
							first = "%";
						}
						if (last.equals("$%")) {
							last = "%";
						}
						if (ssn.equals("$%")) {
							ssn = "%";
						}

						sql = "SELECT * FROM ebdb.Students WHERE firstname LIKE '" + first + "' AND lastname LIKE '"
								+ last + "' AND ssn LIKE '" + ssn + "'";
						rs = stmt.executeQuery(sql);

					} else {
						sql = "SELECT * FROM ebdb.Students WHERE firstname LIKE '%' AND lastname LIKE '%' AND ssn LIKE '%'";
						rs = stmt.executeQuery(sql);

					}
				}

				if (rs != null) {
					while (rs.next()) {
						// Retrieve by column name
						String first = rs.getString("firstname");
						String last = rs.getString("lastname");
						String ssn = rs.getString("ssn");
						extractedStudents.add(new QueryResult(first, last, ssn));
					}
				}

				rs.close();
				stmt.close();
				conn.close();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return extractedStudents;
	}

	public static void setFirst(String first) {
		ServerConnector.first = first;
	}

	public static void setLast(String last) {
		ServerConnector.last = last;
	}

	public static void setSSN(String ssn) {
		ServerConnector.ssn = ssn;
	}

	public static void setsearchMode(boolean searchMode) {
		ServerConnector.searchMode = searchMode;
	}

	public static void setwriteMode(boolean writeMode) {
		ServerConnector.writeMode = writeMode;

	}
}