package at.ac.tuwien.hci.ghost.gps;

import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.observer.Observer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class GPSListener implements Observer<Waypoint> {
	private Run run = null;
	private MapView mapView = null;

	public GPSListener(Run run, MapView mapView) {
		this.run = run;
		this.mapView = mapView;
	}

	@Override
	public void notify(Waypoint p) {
		// calculate geopoint
		GeoPoint geopoint = new GeoPoint((int) (p.getLatitudeDegrees() * 1E6), (int) (p.getLongitudeDegrees() * 1E6));
		
		// run set, add new waypoint
		if (run != null) {
			run.addWaypoint(p);
		}
		
		// map set, center new waypoint and redraw map to show new line
		if (mapView != null) {
			mapView.getController().animateTo(geopoint);
			mapView.postInvalidate();
		}

		Log.i(getClass().getName(), "New Waypoint: " + p);
	}

}
