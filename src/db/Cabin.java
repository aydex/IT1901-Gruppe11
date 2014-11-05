package db;

import ui.WebMap.LatLong;

public class Cabin {
	private final String name;
	private final int size;
    private final int id;
    private final LatLong coords;
	
	Cabin(String name,int size,int id) {
		this.name = name;
		this.size = size;
		this.id = id;

        // TODO: Hvor ble det av støtte for koordinater!?
        this.coords = new LatLong((float) Math.random() + 63.4305f, (float) Math.random() + 10.4219f);

    }
	
	public String getName() {
		return name;
	}
	
	
	public int getSize() {
		return size;
	}

	public int getId() {
		return id;
	}

    public LatLong getCoords(){
        // TODO: Hvor ble det av støtte for koordinater!?

        return coords;
    }

	public String toString() {
		return id + ". " + name;
	}
}
