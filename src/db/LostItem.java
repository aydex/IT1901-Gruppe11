package db;

/**
 * This class contains the LostItem-class.
 * @author Adrian Hundseth
 * @see ui.WebMap.LatLong
 */
public class LostItem {
	private final String itemName;
	private final int koie_id;
	private final int lost_id;
	
	public LostItem(String itemName, int koie_id, int lost_id) {
		this.itemName = itemName;
		this.koie_id = koie_id;
		this.lost_id = lost_id;
	}

    /**
     * This function returns the current name of the LostItem object.
     * @return Name of the LostItem.
     */
	public String getItemName() {
		return itemName;
	}

    /**
     * This function returns the current id of which Cabin the LostItem belongs to.
     * @return Id of the cabin.
     */
	public int getKoie_id() {
		return koie_id;
	}

    /**
     * This function returns the current unique id of the LostItem object.
     * @return Id of the LostItem.
     */
	public int getLost_id() {
		return lost_id;
	}

	@Override
	public String toString() {
		return "Gjenstand: " + itemName + ", id: " + lost_id;
	}

}
