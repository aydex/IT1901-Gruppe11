package db;

import org.joda.time.*;


public class Reservation {
    private final int num_persons;
    private final DateTime date_to;
    private final DateTime date_from;
    private final String email;
    private final int reservation_id;
    private final int koie_id;
    private final String date_to_formatted, date_from_formatted;

    public Reservation(int num_persons, DateTime date_to, DateTime date_from,
                       String email, int reservation_id, int koie_id) {
        this.num_persons = num_persons;
        this.date_to = date_to;
        this.date_from = date_from;
        this.email = email;
        this.reservation_id = reservation_id;
        this.koie_id = koie_id;
        this.date_to_formatted = this.date_to.toString().substring(0, 10);
        this.date_from_formatted = this.date_from.toString().substring(0, 10);
    }

    public int getNum_persons() {
        return num_persons;
    }

    public DateTime getDate_to() {
        return date_to;
    }

    public DateTime getDate_from() {
        return date_from;
    }

    public String getEmail() {
        return email;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public int getKoie_id() {
        return koie_id;
    }

    public String getDate_to_formatted() {
        return date_to_formatted;
    }

    public String getDate_from_formatted() {
        return date_from_formatted;
    }

    @Override
    public String toString() {
        return "Reservation [num_persons=" + num_persons + ", date_to="
                + date_to + ", date_from=" + date_from + ", email=" + email
                + ", reservation_id=" + reservation_id + ", koie_id=" + koie_id
                + "]";
    }
}
