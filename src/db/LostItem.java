package db;

public class LostItem {
	private final String itemName;
	private final int koie_id;
	private final int lost_id;
	
	LostItem(String itemName, int koie_id, int lost_id) {
		this.itemName = itemName;
		this.koie_id = koie_id;
		this.lost_id = lost_id;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public int getKoie_id() {
		return koie_id;
	}
	
	public int getLost_id() {
		return lost_id;
	}

	@Override
	public String toString() {
		return "LostItem [itemName=" + itemName + ", koie_id=" + koie_id
				+ ", lost_id=" + lost_id + "]";
	}

}
