package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Period;

import ui.WebMap.LatLong;

/**
 * This class contains static functions used to get information out of the database.
 * @author Adrian Hundseth
 * @see DBConnect
 *
 */
public class GetData {
	/**
	 * Retrieves all the cabins currently on the database.
	 * Establishes a connection with the database, then creates an <code>ArrayList</code> 
	 * with all the cabins currently in the database. Returns empty if a connection cannot be made.
	 * @return Returns an <code>ArrayList</code> with all the <code>Cabin</code> objects. 
	 * If the connection cannot be made, it returns an empty <code>ArrayList</code>
	 */
	public static ArrayList<Cabin> getCabins() {
        Connection con = DBConnect.getConnection();
        ArrayList<Cabin> cabins = new ArrayList<Cabin>();
        if (con != null) {
            try {
                Statement stmt = con.createStatement();
                String strSelect = "select * from koie";
                System.out.println("Performing SQL Query [" + strSelect + "]");
                ResultSet rset = stmt.executeQuery(strSelect);

                //System.out.println("The cabins are: ");
                while (rset.next()) {
                    String name = rset.getString("name");
                    int size = rset.getInt("size");
                    int id = rset.getInt("koie_id");
                    double corLat = rset.getDouble("latitude");
                    double corLong = rset.getDouble("longitude");
                    Cabin cabin = new Cabin(name, size, id, new LatLong((float)corLat, (float)corLong));
                    cabins.add(cabin);
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        } else {
            System.err.println("No Connection");
        }
        return cabins;
    }
	
	/**
	 * Retrieves the cabin matching the supplied <code>id</code>.
	 * <p>
	 * Uses the function <code>getCabins</code> and iterates through until
	 * it finds a <code>Cabin</code> with a matching id.
	 * </p>
	 * @param id
	 * @see getCabins
	 * @return A <code>Cabin</code> object.
	 */
	public static Cabin getCabinById(int id) {
        ArrayList<Cabin> cabins = getCabins();
        for (Cabin cabin : cabins) {
            if (cabin.getId() == id) {
                return cabin;
            }
        }
        return null;
	}
	
	/**
	 * Returns a <code>Reservation</code> object with the reservation that corresponds to
	 * the supplied parameter <code>id</code>
	 * @param id the unique ID of the reservation
	 * @return A <code>Reservation</code> object corresponding to the <code>id</code>
	 */
    public static Reservation getReservationById(int id) {
        ArrayList<Reservation> reservations = getReservations();
        for (Reservation reservation : reservations) {
            if (reservation.getReservation_id() == id) {
                return reservation;
            }
        }
        return null;
    }

    
    /**
	 * Returns all the reports in the database in the form 
	 * of an <code>ArrayList</code> with <code>Report</code> objects
	 * @return <code>ArrayList</code> with <code>Report</code> objects. Returns an empty <code>ArrayList</code>
	 * if connection cannot be made
	 */
    public static ArrayList<Report> getReports() {
        Connection con = DBConnect.getConnection();
        ArrayList<Report> reports = new ArrayList<Report>();
        if (con != null) {
            try {
                Statement stmt = con.createStatement();
                String strSelect = "select * from report";
                System.out.println("Performing SQL Query [" + strSelect + "]");
                ResultSet rset = stmt.executeQuery(strSelect);

                System.out.println("The reports are:");
                while (rset.next()) {
                    String deficiency = rset.getString("deficiency");
                    int koie_id = rset.getInt("koie_id");
                    int report_id = rset.getInt("report_id");

                    for (int i = 0; i < 20; i++) {
                        System.out.print("-");
                    }
                    Report report = new Report(deficiency, koie_id, report_id);
                    reports.add(report);
                }
            } catch (SQLException e) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }
        return reports;
    }
    
    /**
     * Retrieves the reservations for the previous six months.
     * <p>
     * Using <code>getReservations</code> this function creates a new <code>ArrayList</code> with <code>Reservation</code> objects 
     * that have a starting date no earlier than six months prior to the current date.
     * </p>
     * @return An <code>ArrayList</code> with <code>Reservation</code> objects
     * @see getReservations
     */
    public static ArrayList<Reservation> getStats() {
        ArrayList<Reservation> stats = new ArrayList<Reservation>();
        ArrayList<Reservation> reservations = getReservations();
        DateTime currentDate = new DateTime();
        DateTime statDate = currentDate.plus(Period.months(-6));
        System.out.println(statDate);

        for (Reservation reservation : reservations) {
            if ((reservation.getDate_from().monthOfYear().get() >= statDate.monthOfYear().get()) &&
                    (reservation.getDate_from().monthOfYear().get() <= currentDate.monthOfYear().get())) {
                stats.add(reservation);
            }
        }
        return stats;
    }
    
    /**
     * Retrieves all the reservations currently on the database.
	 * Establishes a connection with the database, then creates an <code>ArrayList</code> 
	 * with all the reservations currently in the database. Returns empty if a connection cannot be made.
	 * @return Returns an <code>ArrayList</code> with all the <code>Reservation</code> objects. 
	 * If the connection cannot be made, it returns an empty <code>ArrayList</code>
     */
    public static ArrayList<Reservation> getReservations() {
        Connection con = DBConnect.getConnection();
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        try {
            if (con != null) {
                Statement stmt = con.createStatement();
                String strSelect = "select * from reservation";
                System.out.println("The SQL Query is: " + strSelect);
                ResultSet rset = stmt.executeQuery(strSelect);

                while (rset.next()) {
                    int people = rset.getInt("num_persons");
                    DateTime date_to = new DateTime(rset.getDate("date_to"));
                    DateTime date_from = new DateTime(rset.getDate("date_from"));
                    String email = rset.getString("email");
                    int reservation_id = rset.getInt("reservation_id");
                    int koie_id = rset.getInt("koie_id");
                    Reservation reservation = new Reservation(people, date_to, date_from, email, reservation_id, koie_id);
                    reservations.add(reservation);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }

        return reservations;
    }
    
    /**
     * Retrieves all reservations made at most six months ago.
     * <p>
     * Connects to and queries the database for reservations with the supplied id. 
     * Any reservations newer than six months are added to the list and returned.
     * </p>
     * @param id - the id of the cabin in question
     * @return An <code>ArrayList</code> of <code>Reservation</code> objects
     */
    public static ArrayList<Reservation> getStatsByCabin(int id) {
    	Connection con = DBConnect.getConnection();
    	ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        DateTime currentDate = new DateTime();
        DateTime sixMonthsAgo = currentDate.plus(Period.months(-6));
    	if (con!=null) {
    		try {
    			Statement stmnt = con.createStatement();
    			String strSelect = "SELECT * FROM reservation WHERE koie_id = " + id;
    			ResultSet rset = stmnt.executeQuery(strSelect);
    			while (rset.next()) {
                    int people = rset.getInt("num_persons");
                    DateTime date_to = new DateTime(rset.getDate("date_to"));
                    DateTime date_from = new DateTime(rset.getDate("date_from"));
                    String email = rset.getString("email");
                    int reservation_id = rset.getInt("reservation_id");
                    int koie_id = rset.getInt("koie_id");
                    Reservation reservation = new Reservation(people, date_to, date_from, email, reservation_id, koie_id);
                    if (reservation.getDate_to().isAfter(sixMonthsAgo) && reservation.getDate_from().isAfter(sixMonthsAgo)) {
                        reservations.add(reservation);
                    }
    			}
    		} catch (SQLException e) {
    			System.err.println("SQLException: " + e.getMessage());
    		}
    	}
    	return reservations;
    }
    
    public static ArrayList<LostItem> getLostItems() {
        Connection con = DBConnect.getConnection();
        ArrayList<LostItem> lostitems = new ArrayList<LostItem>();
        try {
            if (con != null) {
                Statement stmt = con.createStatement();
                String strSelect = "select * from lost_items";
                System.out.println("The SQL Query is: " + strSelect);
                ResultSet rset = stmt.executeQuery(strSelect);

                while (rset.next()) {
                    String item = rset.getString("item");
                    int lost_id= rset.getInt("lost_id");
                    int koie_id = rset.getInt("koie_id");
                    LostItem reservation = new LostItem(item, koie_id, lost_id);
                    lostitems.add(reservation);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return lostitems;
    }
}

