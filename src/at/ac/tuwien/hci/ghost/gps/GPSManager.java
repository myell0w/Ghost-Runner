package at.ac.tuwien.hci.ghost.gps;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.observer.Subject;

public class GPSManager implements Subject<Waypoint>, Observer<Waypoint> {
	private Context context;

	/** The associated location manager */
	private LocationManager locationManager;
	/** Contains all recorded GPS points */
	private List<Waypoint> waypoints = new Vector<Waypoint>();
	/** Contains all registered status receivers */
	private List<Observer<Waypoint>> observers = new Vector<Observer<Waypoint>>();

	private LocationListener locationListener;

	public GPSManager(Context ctx) {
		this.context = ctx;

		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		locationListener = new MyLocationManager(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	public Waypoint getLastKnownLocation() {
		if (!waypoints.isEmpty())
			return waypoints.get(waypoints.size() - 1);
		else
			return null;
	}

	private class MyLocationManager implements LocationListener {
		private Observer<Waypoint> gpsRecorder;

		public MyLocationManager(Observer<Waypoint> gpsRecorder) {
			this.gpsRecorder = gpsRecorder;
		}

		@Override
		public void onLocationChanged(Location loc) {
			gpsRecorder.notify(new Waypoint(loc));
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void addObserver(Observer<Waypoint> o) {
		observers.add(o);
	}

	@Override
	public void notifyAll(Waypoint event) {
		for (Observer<Waypoint> o : observers) {
			o.notify(event);
		}
	}

	@Override
	public void removeObserver(Observer<Waypoint> o) {
		observers.remove(o);
	}

	@Override
	public void notify(Waypoint p) {
		if (waypoints.size() > 0)
			p.calculateSpeed(this.getLastKnownLocation());

		waypoints.add(p);

		notifyAll(p);
	}

}
