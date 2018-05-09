package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
	private String url = "jdbc:mysql://localhost:3306/db_stockmanage";
	private String user = "root";
	private String password = "root";
	private String jdbcName = "com.mysql.jdbc.Driver";
	
	public Connection getCon() throws Exception {
		Class.forName(jdbcName);
		Connection con = DriverManager.getConnection(url, user, password);
		return con;
	}
	
	public void closeCon(Connection con) throws Exception {
		if(con!=null) {
			con.close();
		}
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil = new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
