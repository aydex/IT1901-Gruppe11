package db;

import java.util.Date;


public class Reservation {
	private final int num_persons;
	private final Date date_to;
	private final Date date_from;
	private final String email;
	private final int reservation_id;
	private final int koie_id;
	
	public Reservation(int num_persons, Date date_to, Date date_from,
			String email, int reservation_id, int koie_id) {
		this.num_persons = num_persons;
		this.date_to = date_to;
		this.date_from = date_from;
		this.email = email;
		this.reservation_id = reservation_id;
		this.koie_id = koie_id;
	}

	public int getNum_persons() {
		return num_persons;
	}

	public Date getDate_to() {
		return date_to;
	}

	public Date getDate_from() {
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

	@Override
	public String toString() {
		return "Reservation [num_persons=" + num_persons + ", date_to="
				+ date_to + ", date_from=" + date_from + ", email=" + email
				+ ", reservation_id=" + reservation_id + ", koie_id=" + koie_id
				+ "]";
	}
}
