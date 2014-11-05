package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class contains static functions used to input information to the database.
 * @author Adrian Hundseth
 *
 */
public class MakeData {
    public static void makeReport(String deficiency, int koie_id) throws KoieException {
        Connection con = DBConnect.getConnection();
        if (con != null) {

            String query = "INSERT INTO report ("
                    + " deficiency,"
                    + " koie_id) VALUES ("
                    + "?, ?)";
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setString(1, deficiency);
                preparedStmt.setInt(2, koie_id);
                preparedStmt.execute();
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }

    }

    public static void makeReservation(int num_persons, java.sql.Date date_to, java.sql.Date date_from, String email, int koie_id) throws KoieException {
        Connection con = DBConnect.getConnection();

        String query = "INSERT INTO reservation ("
                + " num_persons,"
                + " date_to,"
                + " date_from,"
                + " email,"
                + " koie_id ) VALUES ("
                + "?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, num_persons);
            preparedStmt.setDate(2, date_to);
            preparedStmt.setDate(3, date_from);
            preparedStmt.setString(4, email);
            preparedStmt.setInt(5, koie_id);

            preparedStmt.execute();

            //stmnt.executeUpdate("INSERT INTO reservation " + "VALUES (" + num_persons + ", " + date_to +
            //", " + date_from + ", " + email + ", " + reservation_id + ", " + koie_id + ")");
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }
    
    public static void makeLostItem(String item_name, int koie_id) {
    	Connection con = DBConnect.getConnection();
    	
    	String query = "INSERT INTO lost_items ("
    			+ " item_name,"
    			+ " koie_id ) VALUES ("
    			+ "?, ?)";
    	try {
    		PreparedStatement preparedStmt = con.prepareStatement(query);
    		preparedStmt.setString(1, item_name);
    		preparedStmt.setInt(2, koie_id);
    		
    		preparedStmt.execute();
    	} catch (SQLException e) {
    		System.err.println("SQLException: " + e.getMessage());
    	}
    }
}
