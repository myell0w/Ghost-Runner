package at.ac.tuwien.hci.ghost.ui.run;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Util;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class StartRunActivity extends MapActivity {
	private DataAccessObject dao = null;
	private List<Entity> routes = null;
	private ArrayAdapter<Route> routeAdapter = null;
	private ArrayAdapter<String> trainingAdapter = null;
	private Spinner selectedRoute = null;
	private Spinner selectedGoal = null;
	private MapView mapView = null;
	private Button startButton = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startrun);
		
		dao = new RouteDAO(this);

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);
		
		selectedRoute = (Spinner)findViewById(R.id.selectedRoute);
		selectedGoal = (Spinner)findViewById(R.id.selectedGoal);
		
		routes = dao.getAll();
		routeAdapter = new ArrayAdapter<Route>(this, android.R.layout.simple_spinner_item);
		routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for (Entity e : routes) {
			routeAdapter.add((Route)e);
		}
		
		selectedRoute.setAdapter(routeAdapter);
		
		trainingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		trainingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		trainingAdapter.add("5 km Workout");
		trainingAdapter.add("30 min Run");
		trainingAdapter.add("The 500kcal-Burner");
		
		selectedGoal.setAdapter(trainingAdapter);

		startButton = (Button) findViewById(R.id.startRunButton);
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startRun();
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
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

	private void startRun() {
		Route r = (Route)selectedRoute.getSelectedItem();
		Intent runningInfoIntent = new Intent(this, RunningInfoActivity.class);
		
		runningInfoIntent.putExtra(Constants.ROUTE, r);
		
		this.startActivity(runningInfoIntent);
	}

}