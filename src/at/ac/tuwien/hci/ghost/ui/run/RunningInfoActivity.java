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
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.ui.WeatherActivity;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RunningInfoActivity extends MapActivity implements Observer<TimeManager> {
	private Run currentRun = null;
	private Route route = null;
	private RunTime runTime = null;
	private TimeManager timeManager = null;

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

		runTime = new RunTime(0);
		timeManager = new TimeManager(this);

		runTime.start();
		timeManager.setEnabled(true);
		timeManager.execute();
	}

	protected void buttonPauseClicked(View v) {
		if (runTime.isPaused()) {
			runTime.start();
			buttonPause.setText(getResources().getString(R.string.run_pause));
		} else {
			runTime.pause();
			buttonPause.setText(getResources().getString(R.string.run_continue));
		}
	}

	protected void buttonStopClicked(View v) {
		// TODO Auto-generated method stub

	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Constants.MENU_SETTINGS, 1, getResources().getString(R.string.app_settings));
		menu.add(0, Constants.MENU_WEATHER, 2, getResources().getString(R.string.app_weather));

		return true;
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
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * updates the time-value in the run-class
	 */
	private void updateTime() {
		currentRun.setTime(runTime.getDuration());
	}

	/**
	 * updates the User-Interface to show new values
	 */
	private void updateUI() {
		int hours = (int) runTime.getDisplayHours();
		int minutes = (int) runTime.getDisplayMinutes();
		int seconds = (int) runTime.getDisplaySeconds();

		textElapsedTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
	}

	@Override
	public void notify(TimeManager param) {
		updateTime();
		updateUI();
	}
}