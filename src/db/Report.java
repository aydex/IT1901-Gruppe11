package db;

public class Report {
	private final String deficiency;
	private final int koie_id;
	private final int report_id;
	
	public Report(String deficiency, int koie_id, int report_id) {
		this.deficiency = deficiency;
		this.koie_id = koie_id;
		this.report_id = report_id;
	}

	public String getDeficiency() {
		return deficiency;
	}

	public int getKoie_id() {
		return koie_id;
	}
	
	public int getReport_id() {
		return report_id;
	}

	@Override
	public String toString() {
		return "ID: " + getReport_id() + " \n\n" + "Rapport: " + "\n" + getDeficiency() +
                "\n" + "------------------------" + "\n";
	}
}
