package at.ac.tuwien.hci.ghost.gps;

import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.observer.Observer;

import com.google.android.maps.MapView;

public class GPSListener implements Observer<Waypoint> {
	private static final double MIN_DISTANCE_IN_METER_BETWEEN_2_WAYPOINTS = 10;
	
	private Run run = null;
	private MapView mapView = null;
	private Waypoint lastWaypoint = null;

	public GPSListener(Run run, MapView mapView) {
		this.run = run;
		this.mapView = mapView;
	}

	@Override
	public void notify(Waypoint p) {
		
		// run set, add new waypoint
		if (run != null) {
			if (lastWaypoint == null || p.distanceTo(lastWaypoint) >= MIN_DISTANCE_IN_METER_BETWEEN_2_WAYPOINTS) {
				run.addWaypoint(p);
				lastWaypoint = p;
			}
		}
		
		// map set, center new waypoint and redraw map to show new line
		if (mapView != null) {
			mapView.getController().animateTo(p.getGeoPoint());
			mapView.postInvalidate();
		}
	}

}
