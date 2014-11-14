package db;

import ui.WebMap.LatLong;

/**
 * This class contains the Cabin-class. It uses the nested class ui.WebMap.LatLong for storing the location of the cabin.
 * @author Adrian Hundseth
 * @see ui.WebMap.LatLong
 */
public class Cabin {

	private final String name;
	private final int size;
    private final int id;
    private final LatLong coords;
	
	Cabin(String name,int size,int id, LatLong coords) {
		this.name = name;
		this.size = size;
		this.id = id;
        this.coords = coords;
    }

    /**
     * This function returns the current Cabin name.
     * @return Cabin name.
     */
	public String getName() {
		return name;
	}

    /**
     * This function returns the current Cabin capacity.
     * @return Cabin capacity.
     */
	public int getSize() {
		return size;
	}

    /**
     * This function returns the current Cabin unique id.
     * @return Cabin id.
     */
	public int getId() {
		return id;
	}

    /**
     * This function returns the current Cabin coordinates.
     * @return Cabin coordinates.
     */
    public LatLong getCoords(){
        return coords;
    }

	public String toString() {
		return id + ". " + name;
	}
}
