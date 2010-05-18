package at.ac.tuwien.hci.ghost.data.entities;

import java.util.List;
import java.util.Vector;

import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.util.Date;

public class Run extends Entity {
	private static final long serialVersionUID = 3050093983774457949L;
	private static final float MAX_PACE = 999;

	public enum Performance {
		WORSE_THAN_AVERAGE, AVERAGE, BETTER_THAN_AVERAGE;

		public static Performance Int2Performance(int value) {
			if (value == WORSE_THAN_AVERAGE.ordinal())
				return WORSE_THAN_AVERAGE;
			else if (value == AVERAGE.ordinal())
				return AVERAGE;

			return BETTER_THAN_AVERAGE;
		}
	};

	/** the id of the run */
	private long id = -1;
	/** date of the run */
	private Date date = null;
	/** total time of the run in seconds */
	private long timeInSeconds = 0L;
	/** total distance of the run */
	private float distanceInKm = 0.f;
	/** average pace of the run */
	private float pace = 0.f;
	/** average speed of the run */
	private float speed = 0.f;
	/** total calories burned */
	private int calories = 0;
	/** the route of the run, or null */
	private Route route = null;
	/** all waypoints of the run */
	private List<Waypoint> waypoints = null;
	/** the performance of the run */
	private Performance performance = null;

	public Run(long id, Date date, long timeInSeconds, float distanceInKm, int calories, Route route, Performance performance) {
		this.id = id;
		this.date = date;
		this.calories = calories;
		this.route = route;

		this.timeInSeconds = timeInSeconds;
		this.distanceInKm = distanceInKm;
		this.performance = performance;

		this.waypoints = new Vector<Waypoint>();

		updateSpeedAndPace();
	}

	public Run(long id) {
		this(id, null, 0, 0.f, 0, null, null);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getTime() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(long time) {
		this.timeInSeconds = time;
	}

	public String getTimeString() {
		int seconds = (int) (timeInSeconds % 60);
		int minutes = (int) (timeInSeconds / 60) % 60;
		int hours = (int) (timeInSeconds / 3600);

		String minutesString = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
		String secondsString = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

		return hours + ":" + minutesString + ":" + secondsString;
	}

	public float getDistanceInKm() {
		return distanceInKm;
	}

	public void setDistanceInKm(float distance) {
		this.distanceInKm = distance;
	}

	public float getPace() {
		return pace;
	}

	public void setPace(float pace) {
		this.pace = pace;
	}

	public String getPaceString() {
		return Run.getPaceString(this.pace);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public boolean hasRoute() {
		return route != null && !route.equals(Route.getEmptyRoute());
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public void addWaypoint(Waypoint p) {
		waypoints.add(p);
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	@Override
	public String toString() {
		return date.toFullString() + ", " + this.getTimeString() + ", " + this.getPaceString();
	}

	private void updateSpeedAndPace() {
		if (timeInSeconds / 3600.f > 0)
			this.speed = distanceInKm / (timeInSeconds / 3600.f);
		if (distanceInKm > 0)
			this.pace = (timeInSeconds / 60.f) / distanceInKm;
	}

	public static String getPaceString(float pace) {
		if (pace < MAX_PACE) {
			int minutes = (int) pace;
			float secondsFactor = pace - minutes;
			int seconds = Math.round(secondsFactor * 60);
			String secondsString = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

			return minutes + ":" + secondsString;
		}
		
		return "-";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Run) {
			return getID() == ((Run) o).getID();
		}

		return false;
	}

	public int getPerformanceImageResourceId() {
		if (performance != null) {
			switch (performance) {
				case WORSE_THAN_AVERAGE:
					return R.drawable.indicator_worse;
				
				case AVERAGE:
					return R.drawable.indicator_equal;
					
				case BETTER_THAN_AVERAGE:
					return R.drawable.indicator_better;
			}
		}

		return R.drawable.indicator_none;
	}
}
