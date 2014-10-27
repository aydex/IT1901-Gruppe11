package db;

import java.sql.*;
import java.util.ArrayList;

import org.joda.time.*;

public class DBConnect {
	private static String userid ="sondrehj_it1901", password = "banan11";
	private static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/sondrehj_it1901";	
	private static Connection con = null;
	
	public static void main(String[] args) {
		Connection con = getConnection();
		ArrayList<Cabin> cabins = getCabins();
		for (int i = 0; i < cabins.size(); i++) {
			System.out.println(cabins.get(i));
		}
		
		//String string_from = "20-10-14";
		//String string_to = "25-10-14";
		//SimpleDateFormat f = new SimpleDateFormat("dd-mm-yy");
		//Date from = f.parse(string_from);
		//Date to = f.parse(string_to);
		//makeReservation(2, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), "adrian.hundseth@hotmail.com", 5, 6);
		getReservations();
		
		//makeReport("Kakerlakker i kaffen", 7, 8);
		getReports();
		
		ArrayList<Reservation> stats = getStats();
		System.out.println("Reservations over the last six months:");
		for (Reservation reservation : stats) {
			System.out.println(reservation);
		}
		try {
			if (con!=null) {
				System.out.println("Got Connection, ");
				DatabaseMetaData meta = con.getMetaData();
				System.out.println("Driver Name : " + meta.getDriverName());
				System.out.println("Driver Version : " + meta.getDriverVersion());
			} else {
				System.out.println("Couldn't get connection");
			}			
		} catch(SQLException e) {
			System.err.print("SQLException" + e.getMessage());
		}
	}
	
	private static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); //Or any other driver
		}
		catch(Exception e){
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}
		
		try{
			 con = DriverManager.getConnection(url, userid, password);
			}
			catch( SQLException e ){
				System.err.println("SQLException: " + e.getMessage());
			}
		return con;
	}
	
	
	public static ArrayList<Cabin> getCabins() {
		Connection con = getConnection();
		ArrayList<Cabin> cabins = new ArrayList<Cabin>();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				String strSelect = "select * from koie";
				System.out.println("The SQL Query is: " + strSelect);
				ResultSet rset = stmt.executeQuery(strSelect);
				
				System.out.println("The cabins are: ");
				while(rset.next()) {
					String name = rset.getString("name");
					int size = rset.getInt("size");
					int id = rset.getInt("koie_id");
					Cabin cabin = new Cabin(name, size, id);
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
	
	public static Reservation getReservationById(int id) {
		ArrayList<Reservation> reservations = getReservations();
		for (Reservation reservation : reservations) {
			if (reservation.getReservation_id() == id) {
				return reservation;
			}
		}
		return null;
	}
	
	public static ArrayList<Report> getReports() {
		Connection con = getConnection();
		ArrayList<Report> reports = new ArrayList<Report>();
		if (con != null) {
			try {
				Statement stmt = con.createStatement();
				String strSelect = "select * from report";
				System.out.println("The SQL Query is: " + strSelect);
				ResultSet rset = stmt.executeQuery(strSelect);
				
				System.out.println("The reports are:");
				while (rset.next()) {
					String deficiency = rset.getString("deficiency");
					int koie_id = rset.getInt("koie_id");
					int report_id = rset.getInt("report_id");
					
					for (int i = 0; i < 20; i++) {System.out.print("-");}
					System.out.println();
					System.out.println("Deficiency: " + deficiency);
					System.out.println("Hytte nummer " + koie_id);
					System.out.println("Rapport nummer " + report_id);
					Report report = new Report(deficiency, koie_id, report_id);
					reports.add(report);
				}
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		return reports;			
	}
	
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
	
	public static ArrayList<Reservation> getReservations() {
		Connection con = getConnection();
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		try {
			if (con != null) {
				Statement stmt = con.createStatement();
				String strSelect = "select * from reservation";
				System.out.println("The SQL Query is: " + strSelect);
				ResultSet rset = stmt.executeQuery(strSelect);
				
				System.out.println("The reservations are: ");
				while(rset.next()) {
					int people = rset.getInt("num_persons");
					DateTime date_to = new DateTime(rset.getDate("date_to"));
					DateTime date_from = new DateTime(rset.getDate("date_from"));
					String email = rset.getString("email");
					int reservation_id = rset.getInt("reservation_id");
					int koie_id = rset.getInt("koie_id");
					
					for (int i = 0; i < 20 ; i++) {System.out.print("-");}
					System.out.println();
					System.out.println("Reservasjon nummer " + reservation_id);
					System.out.println("Hytte nummer " + koie_id);
					System.out.println("Antall personer: " + people);
					System.out.println("Fra: " + date_to);
					System.out.println("Til: " + date_from);
					System.out.println("E-post: " + email);
					//int size = rset.getInt("size");
					//int id = rset.getInt("koie_id");
					//Cabin cabin = new Cabin(name, size, id);
					//cabins.add(cabin);
					Reservation reservation = new Reservation(people, date_to, date_from, email, reservation_id, koie_id);
					reservations.add(reservation);
				}
			}
			
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}

		return reservations;
	}
	
	public static void makeReport(String deficiency, int koie_id, int report_id) throws KoieException {
		Connection con = getConnection();
		if (con != null) {
			ArrayList<Report> reports = getReports();
			for (Report report : reports) {
				if (report.getReport_id() == report_id) {
					throw new KoieException("Report already exists");
				}
			}
			
			String query = "INSERT INTO report ("
					+ " deficiency,"
					+ " koie_id,"
					+ " report_id ) VALUES ("
					+ "?, ?, ?)";
			
			try {
				PreparedStatement preparedStmt = con.prepareStatement(query);
				preparedStmt.setString(1, deficiency);
				preparedStmt.setInt(2, koie_id);
				preparedStmt.setInt(3, report_id);
				
				preparedStmt.execute();
			} catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}
		
	}
		
	public static void makeReservation(int num_persons, java.sql.Date date_to, java.sql.Date date_from, String email, int reservation_id, int koie_id) throws KoieException{
		Connection con = getConnection();
		
		ArrayList<Reservation> reservations = getReservations();
		for (Reservation reservation : reservations) {
			if (reservation.getReservation_id() == reservation_id) {
				throw new KoieException("Reservation already exists");
			}
		}
		
		String query = "INSERT INTO reservation (" 
				+ " num_persons,"
			    + " date_to,"
			    + " date_from,"
			    + " email,"
			    + " reservation_id,"
			    + " koie_id ) VALUES ("
			    + "?, ?, ?, ?, ?, ?)";

		try {
		    PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.setInt 	(1, num_persons);
		    preparedStmt.setDate 	(2, date_to);
		    preparedStmt.setDate	(3, date_from);
		    preparedStmt.setString	(4, email);
		    preparedStmt.setInt		(5, reservation_id);
		    preparedStmt.setInt		(6, koie_id);
		 
		    preparedStmt.execute();
		    
		    //stmnt.executeUpdate("INSERT INTO reservation " + "VALUES (" + num_persons + ", " + date_to + 
						//", " + date_from + ", " + email + ", " + reservation_id + ", " + koie_id + ")");
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
	}
}