package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;

/**
 * This class contains static functions used to input information to the database.
 * @author Adrian Hundseth
 * @see DBConnect
 */
public class MakeData {
	
	/**
	 * This function sends a report to the database.
	 * <p>
	 * Prints an error if the connection fails.
	 * </p>
	 * @param deficiency
	 * @param koie_id
	 * @see Report
	 */
    public static void makeReport(String deficiency, int koie_id) {
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
    
    /**
     * This function sends a reservation to the database using the supplied parameters.
     * <p>
     * All control of whether a reservation is legal and can be made is in this function. 
     * Throws exceptions if dates are wrong, too many or too few people are attempted. Also uses 
     * regex to make sure the email is valid.
     * </p>
     * @param num_persons
     * @param date_to
     * @param date_from
     * @param email
     * @param koie_id
     * @throws KoieException Throws exceptions if the reservations trying to be made are not allowed to make.
     * @see Reservation
     */
    public static void makeReservation(int num_persons, DateTime date_to, DateTime date_from, String email, int koie_id) throws KoieException {
        Connection con = DBConnect.getConnection();
        DateTime currentDate = new DateTime();
        @SuppressWarnings("unused")
		ArrayList<Reservation> reservations = GetData.getStatsByCabin(koie_id); // Used by Controller.java
        if (date_from.isBefore(currentDate) || date_to.isBefore(currentDate)) {
        	throw new KoieException("Can't reserve before current date");
        }
        if ((date_to).isBefore(date_from)) {
        	throw new KoieException("Date to can't be before date from");
        }
        if (GetData.getCabinById(koie_id).getSize() <= num_persons) {
        	throw new KoieException("Too many people");
        }
        if (GetData.getCabinById(koie_id).getSize() < 1) {
        	throw new KoieException("You must reserve for at least 1 person");
        }
        
        
        Matcher mat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
        if(!mat.find()){
            System.out.println("Email '" + email + "' is not valid.");
            throw new KoieException("Email is not valid");
        }
        
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
            preparedStmt.setDate(2, new java.sql.Date(date_to.getMillis()));
            preparedStmt.setDate(3, new java.sql.Date(date_from.getMillis()));
            preparedStmt.setString(4, email);
            preparedStmt.setInt(5, koie_id);
            preparedStmt.execute();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }
    
    /**
     * This function sends a lost item to the database.
	 * <p>
	 * Prints an error if the connection fails.
	 * </p>
     * @param item_name
     * @param koie_id
     * @see LostItem
     */
    public static void makeLostItem(String item_name, int koie_id) {
    	Connection con = DBConnect.getConnection();
    	
    	String query = "INSERT INTO lost_items ("
    			+ " item,"
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
