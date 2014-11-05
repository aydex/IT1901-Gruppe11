package db;

import java.sql.*;

public class DBConnect {
    private static String userid = "sondrehj_it1901", password = "banan11";
    private static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/sondrehj_it1901";
    private static Connection con = null;

    protected static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //Or any other driver
        } catch (ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        try {
            con = DriverManager.getConnection(url, userid, password);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return con;
    }
}
    
