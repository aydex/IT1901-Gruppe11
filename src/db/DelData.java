package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains static functions used to remove information from the database.
 * @author Adrian Hundseth
 * @see DBConnect
 */
public class DelData {
	
	/**
	 * Deletes a reservation from the database that matches the supplied id.
	 * <p>
	 * Queries the database with a delete query and an id. Prints an error if it fails.
	 * </p>
	 * @param id
	 */
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
	
	/**
	 * Deletes a report from the database that matches the supplied id.
	 * <p>
	 * Queries the database with a delete query and an id. Prints an error if it fails.
	 * </p>
	 * @param id
	 */
	public static void delReport(int id) {
		Connection con = DBConnect.getConnection();
		Statement stmnt = null;
		try {
			if (con!=null) {
				String query = "DELETE FROM report WHERE report_id = " + id;
				stmnt = con.createStatement();
				stmnt.executeUpdate(query);
				System.out.println("Deleted Report " + id);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}
	
	/**
	 * Deletes a lost item from the database that matches the supplied id.
	 * <p>
	 * Queries the database with a delete query and an id. Prints an error if it fails.
	 * </p>
	 * @param id
	 */
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
