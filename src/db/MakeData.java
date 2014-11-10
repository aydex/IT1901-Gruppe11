package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

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

    public static void makeReservation(int num_persons, DateTime date_to, DateTime date_from, String email, int koie_id) throws KoieException {
        Connection con = DBConnect.getConnection();
        DateTime currentDate = new DateTime();
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
            preparedStmt.setDate(2, (Date) date_to.toDate());
            preparedStmt.setDate(3, (Date) date_from.toDate());
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
