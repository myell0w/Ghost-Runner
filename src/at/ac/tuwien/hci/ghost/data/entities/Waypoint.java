package at.ac.tuwien.hci.ghost.data.entities;

import android.location.Location;
import at.ac.tuwien.hci.ghost.util.Date;

import com.google.android.maps.GeoPoint;

public class Waypoint extends Entity {
	private static final long serialVersionUID = 906509558162487110L;

	private long id;
	private Location location;
	private float speed;
	private Date timestamp;

	public Waypoint(Location location) {
		this.location = location;
		timestamp = new Date(location.getTime());
	}

	public Waypoint(long id) {
		this(new Location("GhostRunner"));
	}

	public void calculateSpeed(Waypoint previousLocation) {
		if (location.hasSpeed())
			speed = location.getSpeed();
		else {
			double diff = (getTimestamp().getAsJavaDefaultDate().getTime() - 
									previousLocation.getTimestamp().getAsJavaDefaultDate().getTime());
			double distance = location.distanceTo(previousLocation.getLocation());
			
			speed = (float) (distance / (diff / 1000.0));
		}

	}

	public double getLongitudeDegrees() {
		return location.getLongitude();
	}

	public double getLongitudeMinutes() {
		double degree = getLongitudeDegrees();

		return ((degree - (int) degree)) * 60.0;
	}

	public double getLongitudeSeconds() {
		double minutes = getLongitudeMinutes();

		return ((minutes - (int) minutes)) * 60.0;
	}

	public double getLatitudeDegrees() {
		return location.getLatitude();
	}

	public double getLatitudeMinutes() {
		double degree = getLatitudeDegrees();

		return ((degree - (int) degree)) * 60.0;
	}

	public double getLatitudeSeconds() {
		double minutes = getLatitudeMinutes();

		return ((minutes - (int) minutes)) * 60.0;
	}

	public Location getLocation() {
		return location;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public long getUnixTime() {
		return timestamp.getAsJavaDefaultDate().getTime();
	}

	public double distanceTo(Waypoint dest) {
		return location.distanceTo(dest.getLocation());
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
	}

	public GeoPoint getGeoPoint() {
		return new GeoPoint((int) (getLatitudeDegrees() * 1E6), (int) (getLongitudeDegrees() * 1E6));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Waypoint) {
			return getID() == ((Waypoint)o).getID();
		}
		
		return false;
	}
	
	public boolean hasAccuracy() {
		return location.hasAccuracy();
	}

	public float getAccuracy() {
		return location.getAccuracy();
	}
}
