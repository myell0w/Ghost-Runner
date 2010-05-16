package at.ac.tuwien.hci.ghost.ui.run;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.TimeManager;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.gps.CurrentLocationOverlay;
import at.ac.tuwien.hci.ghost.gps.GPSListener;
import at.ac.tuwien.hci.ghost.gps.GPSManager;
import at.ac.tuwien.hci.ghost.gps.RouteOverlay;
import at.ac.tuwien.hci.ghost.gps.RunStatistics;
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.ui.run.SaveRunDialog.ReadyListener;
import at.ac.tuwien.hci.ghost.util.AudioSpeaker;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;
import at.ac.tuwien.hci.ghost.util.Util;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RunningInfoActivity extends MapActivity implements Observer<TimeManager>, ReadyListener {
	private Run currentRun = null;
	/** statistics for current run */
	private RunStatistics statistics = null;
	private Route route = null;
	private TimeManager timeManager = null;
	private GPSManager gpsManager = null;
	private GPSListener gpsListener = null;
	private RunDAO runDAO = null;
	private RouteDAO routeDAO = null;

	private LinearLayout layout = null;
	private TextView textPace = null;
	private TextView textElapsedTime = null;
	private TextView textDistance = null;
	private TextView textRoute = null;
	private TextView textCalories = null;
	private MapView mapView = null;
	private Button buttonStop = null;
	private Button buttonPause = null;
	private ProgressDialog progressDialog = null;
	private AudioSpeaker speaker = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runninginfo);

		speaker = new AudioSpeaker(this,null);
		
		runDAO = new RunDAO(this);
		routeDAO = new RouteDAO(this);
		route = (Route) getIntent().getExtras().get(Constants.ROUTE);

		// get view outlets
		layout = (LinearLayout) findViewById(R.id.runningInfo);
		textPace = (TextView) findViewById(R.id.pace);
		textElapsedTime = (TextView) findViewById(R.id.elapsedTime);
		textDistance = (TextView) findViewById(R.id.distance);
		textRoute = (TextView) findViewById(R.id.route);
		textCalories = (TextView) findViewById(R.id.calories);
		buttonStop = (Button) findViewById(R.id.stopRunButton);
		buttonPause = (Button) findViewById(R.id.pauseRunButton);

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom(Constants.DEFAULT_ZOOM_LEVEL);

		if (route != null) {
			textRoute.setText(getResources().getString(R.string.app_route) + ": " + route.getName());
		} else {
			textRoute.setText(getResources().getString(R.string.app_route) + ": " + getResources().getString(R.string.app_none));
		}

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
		currentRun = new Run(1, new Date(), 0, 0, 0, route, null);
		statistics = new RunStatistics(this);
		
		mapView.getOverlays().add(new RouteOverlay(route, currentRun, mapView));
		mapView.getOverlays().add(new CurrentLocationOverlay(this, mapView));

		gpsListener = new GPSListener(currentRun, mapView);
		gpsManager = new GPSManager(this);
		gpsManager.addObserver(gpsListener);
		gpsManager.addObserver(statistics);

		timeManager = new TimeManager(this);
		
		updateUI();

		if (getIntent().getExtras().getFloat(Constants.GPS_SIGNAL) < Constants.GPS_ACCURACY_BAD) {
			startTracking();
		} else {
			showWaitingDialog();
		}
	}

	private void startTracking() {
		// remove temporary observer
		gpsManager.removeObserver(0);
		
		statistics.getTime().start();
		timeManager.setEnabled(true);
		timeManager.execute();
		
		speaker.speak(getResources().getString(R.string.audio_startRun), TextToSpeech.QUEUE_FLUSH);
	}

	/**
	 * Shows a progressdialog, as long as there is no acceptable gps-signal strength
	 */
	private void showWaitingDialog() {
		progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.run_gpsWait), true);
		// user can cancel with back-button
		progressDialog.setCancelable(true);
		
		// if the user cancels, finish activity
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				gpsManager.stop();
				
				RunningInfoActivity.this.finish();
			}	
		});
		
		// listen for new gps-waypoints and check current accuracy
		gpsManager.addObserver(new Observer<Waypoint>() {
			@Override
			public void notify(Waypoint p) {
				if (progressDialog.isShowing()) {
					Log.i(getClass().getName(), "Waiting for GPS: current Accuracy: " + p.getAccuracy());
					
					// acceptable accuracy found?
					if (p.hasAccuracy() && p.getAccuracy() < Constants.GPS_ACCURACY_BAD) {
						// close dialog
						progressDialog.dismiss();
						// start gps-tracking
						startTracking();
					}	
				}
			}
			
		});
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

	@Override
	public void onPause() {
		super.onPause();

		//pauseRun();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			stopRun();
		}

		return super.onKeyDown(keyCode, event);
	}

	protected void buttonPauseClicked(View v) {
		if (statistics.getTime().isPaused()) {
			continueRun();
		} else {
			pauseRun();
		}
	}

	protected void buttonStopClicked(View v) {
		stopRun();
	}

	private void continueRun() {
		statistics.getTime().start();
		buttonPause.setText(getResources().getString(R.string.run_pause));
	}

	private void pauseRun() {
		statistics.getTime().pause();
		buttonPause.setText(getResources().getString(R.string.run_continue));
	}

	private void stopRun() {
		statistics.getTime().pause();
		updateRunStatistics();
		gpsManager.stop();

		SaveRunDialog dialog = new SaveRunDialog(this, currentRun, this);
		dialog.show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * updates the statistic-values in the run-class
	 */
	private void updateRunStatistics() {
		currentRun.setTimeInSeconds(statistics.getTime().getDurationInSeconds());
		currentRun.setCalories((int) statistics.getCalories());
		currentRun.setDistanceInKm(statistics.getDistanceInKm());
		currentRun.setPace(statistics.getAveragePace());
		currentRun.setSpeed(statistics.getAverageSpeed());
	}

	/**
	 * updates the User-Interface to show new values
	 */
	private void updateUI() {
		int hours = (int) statistics.getTime().getDisplayHours();
		int minutes = (int) statistics.getTime().getDisplayMinutes();
		int seconds = (int) statistics.getTime().getDisplaySeconds();
		double distance = statistics.getDistanceInKm();
		double calories = statistics.getCalories();
		double pace = statistics.getAveragePace();

		textElapsedTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		textPace.setText(String.format("%02.2f", pace) + "\n" + getResources().getString(R.string.app_unitPace));
		textDistance.setText(String.format("%2.2f", distance) + "\n" + getResources().getString(R.string.app_unitDistance));
		textCalories.setText(getResources().getString(R.string.app_calories) + ": " + String.format("%2.0f", calories) + " "
				+ getResources().getString(R.string.app_unitCalories));
		
		// update progress of run, change image
		if (currentRun.hasRoute()) {
			float progress =  currentRun.getDistanceInKm() / currentRun.getRoute().getDistanceInKm();
			
			if (progress > 0.95f) {
				// 100 %
				layout.setBackgroundResource(R.drawable.runninginfo_4);
			} else if (progress > 0.75f) {
				// 75 %
				layout.setBackgroundResource(R.drawable.runninginfo_3);
			} else if (progress > 0.5f) {
				// 50 %
				layout.setBackgroundResource(R.drawable.runninginfo_2);
			} else if (progress > 0.25f) {
				// 25 %
				layout.setBackgroundResource(R.drawable.runninginfo_1);
			} else {
				// 0 %
				layout.setBackgroundResource(R.drawable.runninginfo);
			}
			
			layout.invalidate();
		}
	}

	@Override
	public void notify(TimeManager param) {
		updateRunStatistics();
		updateUI();
	}

	@Override
	public void readyToFinishActivity(boolean saveRun, boolean saveRunAsRoute, String routeName) {
		int toastId = -1;
		Route r = null;
		
		// save the route
		if (saveRunAsRoute && routeName != null) {
			r = new Route(1, routeName, currentRun.getDistanceInKm(), 1);
			
			r.setWaypoints(currentRun.getWaypoints());
			r.setID(routeDAO.insert(r));
		}
		
		// save the run
		if (saveRun) {
			currentRun.setRoute(r);
			currentRun.setID(runDAO.insert(currentRun));
			toastId = R.string.run_saved;
		}
		
		if (saveRunAsRoute && routeName != null) {
			toastId = R.string.runAndRoute_saved;
		}
		
		// Show notification
		if (toastId != -1) {
			Toast toast = Toast.makeText(this, getResources().getString(toastId), Toast.LENGTH_LONG);
			toast.show();
		}

		finish();
	}
}