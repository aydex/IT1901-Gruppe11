package db;

import org.joda.time.DateTime;

/**
 * This class contains the Reservation-class. It uses the class DateTime from the library JodaTime.
 * @author Adrian Hundseth
 */
public class Reservation {
    private final int num_persons;
    private final DateTime date_to;
    private final DateTime date_from;
    private final String date_to_formatted;
    private final String date_from_formatted;
    private final String email;
    private final int reservation_id;
    private final int koie_id;

    public Reservation(int num_persons, DateTime date_to, DateTime date_from,
                       String email, int reservation_id, int koie_id) {
        this.num_persons = num_persons;
        this.date_to = date_to;
        this.date_from = date_from;
        this.date_to_formatted = date_to.toString("YYYY.MM.dd");
        this.date_from_formatted = date_from.toString("YYYY.MM.dd");
        this.email = email;
        this.reservation_id = reservation_id;
        this.koie_id = koie_id;
    }

    /**
     * This function returns the number of persons for the current reservation.
     * @return Number of persons in the reservation.
     */
    public int getNum_persons() {
        return num_persons;
    }

    /**
     * This function returns the end date of the reservation.
     * @return The end date of the reservation.
     */
    public DateTime getDate_to() {
        return date_to;
    }

    /**
     * This function returns a formatted version of the end date of the reservation.
     * @return A formatted version of the end date of the reservation.
     */
    public String getDate_to_formatted() {
		return date_to_formatted;
	}

    /**
     * This function returns the start date of the reservation.
     * @return The start date of the reservation.
     */
	public DateTime getDate_from() {
        return date_from;
    }

    /**
     * This function returns a formatted version of the start date of the reservation.
     * @return A formatted version of the start date of the reservation.
     */
    public String getDate_from_formatted() {
		return date_from_formatted;
	}

    /**
     * This function returns the email of the reservation holder.
     * @return The email of the reservation holder.
     */
	public String getEmail() {
        return email;
    }

    /**
     * This function returns the unique id of the reservation.
     * @return The reservation id.
     */
    public int getReservation_id() {
        return reservation_id;
    }

    /**
     * This function returns the id for the cabin for the current reservation.
     * @return The id for the cabin for the current reservation.
     */
    public int getKoie_id() {
        return koie_id;
    }

    @Override
    public String toString() {
        return "Reservation [num_persons=" + num_persons + ", date_to="
                + date_to + ", date_from=" + date_from + ", email=" + email
                + ", reservation_id=" + reservation_id + ", koie_id=" + koie_id
                + "]";
    }
}
