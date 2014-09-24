package db;

import java.sql.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.LocalDate;

import com.mysql.*;

public class DBConnect {
	static String userid ="sondrehj_it1901", password = "banan11";
	static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/sondrehj_it1901";	
	static Connection con = null;
	
	public static void main(String[] args) throws Exception {
		Connection con = getConnection();
		ArrayList<Cabin> cabins = getCabins();
		for (int i = 0; i < cabins.size(); i++) {
			System.out.println(cabins.get(i));
		}
		
		
		//makeReservation(2, "21.10.14", "19.10.14", "email@email.email", 15, 1);
		getReservations();
		
		if (con!=null) {
			System.out.println("Got Connection, ");
		    DatabaseMetaData meta = con.getMetaData();
		    System.out.println("Driver Name : " + meta.getDriverName());
		    System.out.println("Driver Version : " + meta.getDriverVersion());
		} else {
			System.out.println("Couldn't get connection");
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
		try {
			Statement stmt = con.createStatement();
			String strSelect = "select * from koie";
			System.out.println("The SQL Query is: " + strSelect);
			ResultSet rset = stmt.executeQuery(strSelect);
			
			System.out.println("The cabins are: ");
			int rowCount = 0;
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
		return cabins;
	}
	
	public static void getReservations() {
		Connection con = getConnection();
		ArrayList<Cabin> cabins = new ArrayList<Cabin>();
		try {
			Statement stmt = con.createStatement();
			String strSelect = "select * from reservation";
			System.out.println("The SQL Query is: " + strSelect);
			ResultSet rset = stmt.executeQuery(strSelect);
			
			System.out.println("The reservations are: ");
			int rowCount = 0;
			while(rset.next()) {
				int name = rset.getInt("num_persons");
				Date date_to = rset.getDate("date_to");
				Date date_from = rset.getDate("date_from");
				String email = rset.getString("email");
				int reservation_id = rset.getInt("reservation_id");
				int koie_id = rset.getInt("koie_id");
				System.out.println(name);
				System.out.println(date_to);
				System.out.println(date_from);
				System.out.println(email);
				System.out.println(reservation_id);
				System.out.println(koie_id);
				//int size = rset.getInt("size");
				//int id = rset.getInt("koie_id");
				//Cabin cabin = new Cabin(name, size, id);
				//cabins.add(cabin);
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
		//return cabins;
	}
	
	public static void makeReservation(int num_persons, String date_to, String date_from, String email, int reservation_id, int koie_id) {
		Connection con = getConnection();
		//Reservation res = new Reservation(num_persons, date_to, date_from, email, reservation_id, koie_id);
		try {
			Statement stmnt = con.createStatement();
			Date to_date = new Date(Calendar.getInstance().getTime().getTime());
			Date from_date = new Date((Calendar.getInstance().getTime().getTime()));
			String query = "INSERT INTO reservation"
			        + " VALUES (?, ?, ?, ?, ?, ?)"; 
		    PreparedStatement preparedStmt = con.prepareStatement(query);
		    preparedStmt.setInt 	(1, num_persons);
		    preparedStmt.setDate 	(2, to_date);
		    preparedStmt.setDate	(3, from_date);
		    preparedStmt.setString	(4, email);
		    preparedStmt.setInt		(5, reservation_id);
		    preparedStmt.setInt		(6, koie_id);
		 
		    preparedStmt.execute();
		    
		    //stmnt.executeUpdate("INSERT INTO reservation " + "VALUES (" + num_persons + ", " + date_to + 
						//", " + date_from + ", " + email + ", " + reservation_id + ", " + koie_id + ")");
		} catch (SQLException e) {
			System.err.println("SQLExceptoin: " + e.getMessage());
		}
	}
}
