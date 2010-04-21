package at.ac.tuwien.hci.ghost.ui.run;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.TimeManager;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.RunTime;
import at.ac.tuwien.hci.ghost.gps.GPSListener;
import at.ac.tuwien.hci.ghost.gps.GPSManager;
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.ui.WeatherActivity;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RunningInfoActivity extends MapActivity implements Observer<TimeManager> {
	private Run currentRun = null;
	private Route route = null;
	private TimeManager timeManager = null;
	private GPSManager gpsManager = null;
	private GPSListener gpsListener  = null;

	private TextView textPace = null;
	private TextView textElapsedTime = null;
	private TextView textDistance = null;
	private TextView textRoute = null;
	private TextView textCalories = null;
	private MapView map = null;
	private Button buttonStop = null;
	private Button buttonPause = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runninginfo);

		// get view outlets
		textPace = (TextView) findViewById(R.id.pace);
		textElapsedTime = (TextView) findViewById(R.id.elapsedTime);
		textDistance = (TextView) findViewById(R.id.distance);
		textRoute = (TextView) findViewById(R.id.route);
		textCalories = (TextView) findViewById(R.id.calories);
		buttonStop = (Button) findViewById(R.id.stopRunButton);
		buttonPause = (Button) findViewById(R.id.pauseRunButton);

		map = (MapView) findViewById(R.id.overviewMap);
		map.setBuiltInZoomControls(true);

		// add onClickListener
		buttonStop.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				buttonStopClicked(v);
			}
		});

		buttonPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				buttonPauseClicked(v);
			}
		});

		// initialize entities
		currentRun = new Run(1, new Date(), 0, 0, 0, null);

		gpsListener = new GPSListener(currentRun);
		gpsManager = new GPSManager(this);
		gpsManager.addObserver(gpsListener);
		gpsManager.addObserver(currentRun.getStatistics());
		
		timeManager = new TimeManager(this);

		currentRun.getStatistics().getTime().start();
		timeManager.setEnabled(true);
		timeManager.execute();
	}
	
	/* Handles menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Constants.MENU_SETTINGS:
			// TODO code for doing settings for goals
			return true;

		case Constants.MENU_WEATHER:
			Intent weatherIntent = new Intent(this, WeatherActivity.class);
			this.startActivity(weatherIntent);

			return true;
		}
		return false;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		pauseRun();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	protected void buttonPauseClicked(View v) {
		if (currentRun.getStatistics().getTime().isPaused()) {
			continueRun();
		} else {
			pauseRun();
		}
	}
	
	protected void buttonStopClicked(View v) {
		stopRun();
	}

	private void continueRun() {
		currentRun.getStatistics().getTime().start();
		buttonPause.setText(getResources().getString(R.string.run_pause));
	}

	private void pauseRun() {
		currentRun.getStatistics().getTime().pause();
		buttonPause.setText(getResources().getString(R.string.run_continue));
	}

	private void stopRun() {
		// TODO Auto-generated method stub
		
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Constants.MENU_SETTINGS, 1, getResources().getString(R.string.app_settings));
		menu.add(0, Constants.MENU_WEATHER, 2, getResources().getString(R.string.app_weather));

		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * updates the time-value in the run-class
	 */
	private void updateTime() {
		currentRun.setTime(currentRun.getStatistics().getTime().getDuration());
	}

	/**
	 * updates the User-Interface to show new values
	 */
	private void updateUI() {
		int hours = (int) currentRun.getStatistics().getTime().getDisplayHours();
		int minutes = (int) currentRun.getStatistics().getTime().getDisplayMinutes();
		int seconds = (int) currentRun.getStatistics().getTime().getDisplaySeconds();
		double distance = currentRun.getStatistics().getDistance();
		double calories = currentRun.getStatistics().getCalories();
		double pace = currentRun.getStatistics().getAveragePace();

		textElapsedTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		textPace.setText(String.format("%02.2f", pace) + "\n" + getResources().getString(R.string.app_unitPace));
		textDistance.setText(String.format("%2.2f", distance) + "\n" + getResources().getString(R.string.app_unitDistance));
		textCalories.setText(getResources().getString(R.string.app_calories) + ": " + String.format("%2.0f", calories) + " " + getResources().getString(R.string.app_unitCalories));
	}

	@Override
	public void notify(TimeManager param) {
		updateTime();
		updateUI();
	}
}