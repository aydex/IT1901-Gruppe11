package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DelData {
	public static void delReservation(int id) {
		Connection con = DBConnect.getConnection();
		Statement stmnt = null;
		try {
			if (con!=null) {
				String query = "DELETE FROM reservation WHERE reservation_id = " + id;
				stmnt = con.createStatement();
				stmnt.executeUpdate(query);
				System.out.println("Deleted reservation " + id);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}
	
	public static void delReport(int id) {
		Connection con = DBConnect.getConnection();
		Statement stmnt = null;
		try {
			if (con!=null) {
				String query = "DELETE FROM report WHERE report_id = " + id;
				stmnt = con.createStatement();
				stmnt.executeUpdate(query);
				System.out.println("Deleted reservation " + id);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}
	
	public static void delLostItem(int id) {
		Connection con = DBConnect.getConnection();
		Statement stmnt = null;
		try {
			if (con!=null) {
				String query = "DELETE FROM lost_items WHERE lost_id = " + id;
				stmnt = con.createStatement();
				stmnt.executeUpdate(query);
				System.out.println("Deleted lost item " + id);
			}
		} catch(SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}
}
