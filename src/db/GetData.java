package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Period;

import ui.WebMap.LatLong;

public class GetData {
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
        Connection con = DBConnect.getConnection();
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

                    for (int i = 0; i < 20; i++) {
                        System.out.print("-");
                    }
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
        Connection con = DBConnect.getConnection();
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        try {
            if (con != null) {
                Statement stmt = con.createStatement();
                String strSelect = "select * from reservation";
                System.out.println("The SQL Query is: " + strSelect);
                ResultSet rset = stmt.executeQuery(strSelect);

                System.out.println("The reservations are: ");
                while (rset.next()) {
                    int people = rset.getInt("num_persons");
                    DateTime date_to = new DateTime(rset.getDate("date_to"));
                    DateTime date_from = new DateTime(rset.getDate("date_from"));
                    String email = rset.getString("email");
                    int reservation_id = rset.getInt("reservation_id");
                    int koie_id = rset.getInt("koie_id");

                    for (int i = 0; i < 20; i++) {
                        System.out.print("-");
                    }
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
}
