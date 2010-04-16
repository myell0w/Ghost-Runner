package at.ac.tuwien.hci.ghost.data.entities;

/**
 * encapsulates a route
 * 
 * @author Matthias
 *
 */
public class Route extends Entity {
	/** name of the route */
	private String name = null;
	/** distance of the route */
	private float distance = 0.0f;
	/** number of runs on this route */
	private int runCount = 0;
	
	public Route(String name, float distance, int runCount) {
		this.name = name;
		this.distance = distance;
		this.runCount = runCount;
	}
	
	public Route(String name, float distance) {
		this(name,distance,0);
	}
	
	public Route(String name) {
		this(name, 0.0f, 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public int getRunCount() {
		return runCount;
	}

	public void setRunCount(int runCount) {
		this.runCount = runCount;
	}
	
	public void increaseRunCount() {
		runCount++;
	}
}
