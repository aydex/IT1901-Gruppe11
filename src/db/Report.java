package db;

import java.sql.Date;

public class Report {
	private final String deficiency;
	private final Date date_to;
	private final Date date_from;
	private final String email;
	private final int koie_id;
	
	public Report(String deficiency, Date date_to, Date date_from,
			String email, int koie_id) {
		this.deficiency = deficiency;
		this.date_to = date_to;
		this.date_from = date_from;
		this.email = email;
		this.koie_id = koie_id;
	}

	public String getDeficiency() {
		return deficiency;
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

	public int getKoie_id() {
		return koie_id;
	}

	@Override
	public String toString() {
		return "Report [deficiency=" + deficiency + ", date_to=" + date_to
				+ ", date_from=" + date_from + ", email=" + email
				+ ", koie_id=" + koie_id + "]";
	}
}
