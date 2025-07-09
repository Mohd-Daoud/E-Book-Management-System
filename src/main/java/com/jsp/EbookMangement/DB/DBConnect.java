package com.jsp.EbookMangement.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

	private static Connection conn;

	public static Connection getConn() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = "jdbc:mysql://127.0.0.1:3306/ebook";
			String user = "root";
			String pass = "root";

			conn = DriverManager.getConnection(url, user, pass);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return conn;

	}

}
