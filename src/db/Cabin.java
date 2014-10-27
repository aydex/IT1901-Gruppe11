package db;

import com.lynden.gmapsfx.javascript.object.LatLong;

public class Cabin {
	private final String name;
	private final int size;
	private final int id;
	private final LatLong coordinates;
	
	Cabin(String name,int size,int id, LatLong coordinates) {
		this.name = name;
		this.size = size;
		this.id = id;
		this.coordinates = coordinates;
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
	
	public LatLong getCoordinates() {
		return coordinates;
	}

	public String toString() {
		return id + ". " + name;
	}
}
