package bank;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBActionBank {
	
	private static DBActionBank instance = new DBActionBank();
	Connection conn;
	
	public DBActionBank() {
		String driver = "com.mysql.cj.jdbc.Driver"; 
		String url = "jdbc:mysql://localhost:3306/app";
		try {
			Class.forName(driver); 
			conn = DriverManager.getConnection(url, "root", "kp23156385@");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DBActionBank getInstance() {
		if (instance == null) {
		 instance = new DBActionBank();
		}
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void close() {
		try {
			if (conn != null) conn.close();
		} catch (Exception e) {}
	}
}
