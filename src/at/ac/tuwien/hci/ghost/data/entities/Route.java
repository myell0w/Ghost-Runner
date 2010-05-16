package at.ac.tuwien.hci.ghost.data.entities;

import java.util.List;
import java.util.Vector;


/**
 * encapsulates a route
 * 
 * @author Matthias
 * 
 */
public class Route extends Entity {
	private static final long serialVersionUID = -2108531992111612435L;

	private static Route emptyRoute = new Route(-999,"No Route",-1.f,-1);
	
	/** id of the route */
	private long id = -1;
	/** name of the route */
	private String name = null;
	/** distance of the route */
	private float distance = 0.0f;
	/** number of runs on this route */
	private int runCount = 0;
	/** all waypoints of the route */
	private List<Waypoint> waypoints = null;

	/** runs on this Route (optional) */
	// private List<Run> runs = null;
	
	public static Route getEmptyRoute() {
		return emptyRoute;
	}

	public Route(long id, String name, float distance, int runCount) {
		this.id = id;
		this.name = name;
		this.distance = distance;
		this.runCount = runCount;
		
		waypoints = new Vector<Waypoint>();
	}

	public Route(long id, String name, float distance) {
		this(id, name, distance, 0);
	}

	public Route(long id, String name) {
		this(id, name, 0.0f, 0);
	}

	public Route(long id) {
		this(id, "", 0.f, 0);
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

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}
	
	public String toString() {
		return getName();
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Route) {
			return getID() == ((Route)o).getID();
		}
		
		return false;
	}
}
