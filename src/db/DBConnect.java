package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Manages the connection to the database
 * @author Adrian Hundseth
 *
 */
public class DBConnect {
	
	private static String userid = null, password = null;
	private static String url = null;
	private static String driver = null;	
    /**
	 * The Connection to the database.
	 * Connects using <code>DriverManager.getConnection</code>.
	 * Throws an exception if it fails.
	 */
    private static Connection con = null;

    /**
	 * Connects to the database with the supplied static strings <code>url, userid and password</code>
	 * <p>
	 * If the connection fails to establish due to a missing MySQL driver or failure to reach the server, 
	 * the function catches the exception, and prints the error and returns the empty <code>Connection</code> object.
	 * </p>  
	 * @return The connection in the form of the <code>Connection</code> object <code>con</code>
	 */
    protected static Connection getConnection() {
    	Configuration config = null;
    	try {
    		config = DOMParser.loadConfig("default");
    	} catch (IOException e) {
    		System.err.println("IOException: " + e.getMessage());
    	} catch (SAXException f) {
    		System.err.println("SAXExceptino: " + f.getMessage());
    	} catch (ParserConfigurationException g) {
    		System.err.println("ParserConfigurationException: " + g.getMessage());
    	}
    	
    	if (config != null) {
    		userid = config.userid;
    		password = config.password;
    		url = config.url;
    		driver = config.driver;
    	}
    	
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
    
