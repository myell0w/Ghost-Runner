package at.ac.tuwien.hci.ghost.gps;

import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.observer.Observer;

public class GPSListener implements Observer<Waypoint> {
	private Run run = null;
	
	
	public GPSListener(Run run) {
		this.run = run;
	}
	
	@Override
	public void notify(Waypoint p) {
		run.addWaypoint(p);
		
		Log.i(getClass().getName(), "New Waypoint: " + p);
	}

}
