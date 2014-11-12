package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class manages the connection to the database
 * @author Adrian Hundseth
 *
 */
public class DBConnect {
    private static String userid = "sondrehj_it1901", password = "banan11";
    private static String url = "jdbc:mysql://mysql.stud.ntnu.no:3306/sondrehj_it1901";
    private static String driver = "com.mysql.jdbc.Driver";
    /**
	 * The Connection to the database.
	 * Connects using <code>DriverManager.getConnection</code>.
	 * Throws an exception if it fails.
	 * 
	 */
    private static Connection con = null;

    /**
	 * Connects to the database with the supplied static strings <code>url, userid and password</code>
	 * <p>
	 * If the connection fails to establish due to a missing MySQL driver or failure to reach the server, 
	 * the function prints the error and returns the empty <code>Connection</code> object.
	 * </p>  
	 * @return The connection in the form of the <code>Connection</code> object <code>con</code>
	 */
    protected static Connection getConnection() {
        try {
            Class.forName(driver); //Or any other driver
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
    
