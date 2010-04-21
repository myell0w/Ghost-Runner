package at.ac.tuwien.hci.ghost.data.entities;

import android.location.Location;
import at.ac.tuwien.hci.ghost.util.Date;

public class Waypoint {
	private Location location;
	private float speed;
	private Date timestamp;

	public Waypoint(Location location) {
		this.location = location;
		timestamp = new Date(location.getTime());
	}

	public void calculateSpeed(Waypoint previousLocation) {
		if (location.hasSpeed())
			speed = location.getSpeed();
		else {
			double diff = (double) (getTimestamp().getAsJavaDefaultDate()
					.getTime() - previousLocation.getTimestamp()
					.getAsJavaDefaultDate().getTime());
			double distance = location.distanceTo(previousLocation
					.getLocation());
			speed = (float) (distance / diff * 3600.0);
		}

	}

	public double getLongitudeDegrees() {
		return location.getLongitude();
	}

	public double getLongitudeMinutes() {
		double degree = getLongitudeDegrees();

		return (double) ((degree - (int) degree)) * 60.0;
	}

	public double getLongitudeSeconds() {
		double minutes = getLongitudeMinutes();

		return (double) ((minutes - (int) minutes)) * 60.0;
	}

	public double getLatitudeDegrees() {
		return location.getLatitude();
	}

	public double getLatitudeMinutes() {
		double degree = getLatitudeDegrees();

		return (double) ((degree - (int) degree)) * 60.0;
	}

	public double getLatitudeSeconds() {
		double minutes = getLatitudeMinutes();

		return (double) ((minutes - (int) minutes)) * 60.0;
	}

	public Location getLocation() {
		return location;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public float getSpeed() {
		return speed;
	}

	public long getUnixTime() {
		return timestamp.getAsJavaDefaultDate().getTime();
	}

	public double distanceTo(Waypoint dest) {
		return location.distanceTo(dest.getLocation());
	}

	public String toString() {
		return "Latitude: " + location.getLatitude() + ", Longitude: "
				+ location.getLongitude();
	}
}