package db;

public class Cabin {
	private final String name;
	private final int size;
	private final int id;
	
	Cabin(String name,int size,int id) {
		this.name = name;
		this.size = size;
		this.id = id;
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

	public String toString() {
		return "Name: " + name + " " + "Size: " + size  +" " + "ID: " + id;
	}
}
