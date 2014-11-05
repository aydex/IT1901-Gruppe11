package db;

import ui.WebMap.LatLong;

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
        return coords;
    }

	public String toString() {
		return id + ". " + name;
	}
}
