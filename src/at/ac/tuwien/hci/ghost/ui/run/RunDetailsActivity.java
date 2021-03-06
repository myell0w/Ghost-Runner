package at.ac.tuwien.hci.ghost.ui.run;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.dao.WaypointDAO;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.gps.RouteOverlay;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Util;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RunDetailsActivity extends MapActivity {
	private MapView mapView = null;
	private Run run = null;
	private WaypointDAO waypointDAO = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rundetails);

		run = (Run) getIntent().getExtras().getSerializable(Constants.RUN);
		
		// load waypoints from db
		waypointDAO = new WaypointDAO();
		run.setWaypoints(waypointDAO.getAllWaypointsOfRun(run));
		run.getRoute().setWaypoints(waypointDAO.getAllWaypointsOfRoute(run.getRoute()));
		
		System.out.println("************** " + run.getRoute().getWaypoints().size());

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);
		mapView.getOverlays().add(new RouteOverlay(run.getRoute(), run, mapView));

		TextView heading = (TextView) findViewById(R.id.runHeading);
		heading.setText(run.getDate().toFullString());
		
		TextView detailTotalTime = (TextView) findViewById(R.id.detailTotalTime);
		detailTotalTime.setText(run.getTimeString() + " " + getResources().getString(R.string.app_unitTime));
		
		TextView detailTotalDistance = (TextView) findViewById(R.id.detailTotalDistance);
		detailTotalDistance.setText(String.format("%.2f",run.getDistanceInKm()) + " " + getResources().getString(R.string.app_unitDistance));
		
		TextView detailAveragePace = (TextView) findViewById(R.id.detailAveragePace);
		detailAveragePace.setText(run.getPaceString() + " " + getResources().getString(R.string.app_unitPace));
		
		TextView detailAverageSpeed = (TextView) findViewById(R.id.detailAverageSpeed);
		detailAverageSpeed.setText(String.format("%.2f",run.getSpeed()) + " " + getResources().getString(R.string.app_unitSpeed));
		
		TextView detailCalories = (TextView) findViewById(R.id.detailTotalCalories);
		detailCalories.setText(run.getCalories() + " " + getResources().getString(R.string.app_unitCalories));
		
		if (run.getWaypoints().size() > 0) {
			mapView.getController().animateTo(run.getWaypoints().get(0).getGeoPoint());
			mapView.postInvalidate();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Util.onCreateOptionsMenu(this, menu);

		return true;
	}

	/* Handles menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Util.onOptionsItemSelected(this, item)) {
			return true;
		}

		switch (item.getItemId()) {
		
		}

		return false;
	}
}