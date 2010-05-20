package at.ac.tuwien.hci.ghost.gps;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.observer.Subject;

public class GPSManager implements Subject<Waypoint>, Observer<Waypoint> {
	/** Context of the GPSManager */
	private Context context;
	/** The associated location manager */
	private LocationManager locationManager;
	/** Contains all recorded GPS points */
	private List<Waypoint> waypoints = new Vector<Waypoint>();
	/** Contains all registered status receivers */
	private List<Observer<Waypoint>> observers = new Vector<Observer<Waypoint>>();
	/** listens for GPS Location updates */
	private LocationListener locationListener;

	public GPSManager(Context ctx) {
		this.context = ctx;

		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		locationListener = new MyLocationListener(this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	public Waypoint getLastKnownLocation() {
		if (!waypoints.isEmpty())
			return waypoints.get(waypoints.size() - 1);
		else
			return null;
	}

	public void stop() {
		locationManager.removeUpdates(locationListener);
	}

	private class MyLocationListener implements LocationListener {
		private Observer<Waypoint> gpsRecorder;

		public MyLocationListener(Observer<Waypoint> gpsRecorder) {
			this.gpsRecorder = gpsRecorder;
		}

		@Override
		public void onLocationChanged(Location loc) {
			gpsRecorder.notify(new Waypoint(loc));
		}

		@Override
		public void onProviderDisabled(String provider) {
		
		}

		@Override
		public void onProviderEnabled(String provider) {
		
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		
		}
	}

	@Override
	public synchronized void addObserver(Observer<Waypoint> o) {
		synchronized (observers) {
			observers.add(o);
		}
	}

	@Override
	public synchronized void notifyAll(Waypoint event) {
		synchronized (observers) {
			for (Observer<Waypoint> o : observers) {
				o.notify(event);
			}
		}
	}

	@Override
	public synchronized void removeObserver(Observer<Waypoint> o) {
		synchronized (observers) {
			observers.remove(o);
		}
	}

	@Override
	public synchronized void removeObserver(int index) {
		synchronized (observers) {
			if (index >= 0 && index < observers.size())
				observers.remove(index);
		}
	}

	@Override
	public synchronized void notify(Waypoint p) {
		if (!waypoints.isEmpty()) {
			p.calculateSpeed(this.getLastKnownLocation());
		}

		waypoints.add(p);
		notifyAll(p);

		Log.i(getClass().getName(), "New Waypoint: " + p);
	}
}
