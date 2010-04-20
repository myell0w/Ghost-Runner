package at.ac.tuwien.hci.ghost.data.entities;

import at.ac.tuwien.hci.ghost.util.Date;

public class Run extends Entity {
	/** the id of the run */
	private long id = -1;
	/** date of the run */
	private Date date = null;
	/** total time of the run in seconds */
	private long timeInSeconds = 0L;
	/** total distance of the run */
	private float distance = 0.f;
	/** average pace of the run */
	private float pace = 0.f;
	/** average speed of the run */
	private float speed = 0.f;
	/** total calories burned */
	private int calories = 0;
	/** the route of the run, or null */
	private Route route = null;
	
	public Run(long id, Date date, long timeInSeconds, float distance, int calories, Route route) {
		this.id = id;
		this.date = date;
		this.calories = calories;
		this.route = route;
		
		this.timeInSeconds = timeInSeconds;
		this.distance = distance;
		
		updateSpeedAndPace();
	}
	
	public Run(long id) {
		this.id = id;
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
	
	public void setTime(long time) {
		this.timeInSeconds = time;
	}
	
	public String getTimeString() {
		int seconds = (int)(timeInSeconds % 60);
		int minutes = (int)(timeInSeconds / 60) % 60;
		int hours = (int)(timeInSeconds / 3600);
		
		String minutesString = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
		String secondsString = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
		
		return hours + ":" + minutesString + ":" + secondsString;
	}

	public float getDistance() {
		return distance;
	}
	
	public void setDistance(float distance)
	{
		this.distance = distance;
	}
	
	public float getPace() {
		return pace;
	}
	
	public void setPace(float pace)
	{
		this.pace = pace;
	}
	
	public String getPaceString() {
		int minutes = (int)pace;
		float secondsFactor = pace - minutes;
		int seconds = Math.round(secondsFactor * 60);
		String secondsString = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
		
		return minutes + ":" + secondsString;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public int getCalories() {
		return calories;
	}
	
	public void setCalories(int calories) {
		this.calories = calories;
	}
	
	public Route getRoute() {
		return route;
	}
	
	public void setRoute(Route route) {
		this.route = route;
	}
	
	private void updateSpeedAndPace() {
		this.speed = distance / (timeInSeconds / 3600.f);
		this.pace = (timeInSeconds / 60.f) / distance;
		
	}
	
	@Override
	public long getID() {
		return id;
	}
	
	public void setID(long id)
	{
		this.id = id;
	}
}
