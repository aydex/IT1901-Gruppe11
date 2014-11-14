package db;

/**
 * A custom exception used throughout the program to ensure the validity of certain functions
 * @author Adrian Hundseth
 *
 */
public class KoieException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public KoieException(String message) {
		super(message);
	}
}
