package at.ac.tuwien.hci.ghost.ui.run;

import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.gps.CurrentLocationOverlay;
import at.ac.tuwien.hci.ghost.gps.GPSListener;
import at.ac.tuwien.hci.ghost.gps.GPSManager;
import at.ac.tuwien.hci.ghost.gps.RouteOverlay;
import at.ac.tuwien.hci.ghost.observer.Observer;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Util;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class StartRunActivity extends MapActivity implements OnInitListener, Observer<Waypoint> {
	private DataAccessObject dao = null;
	private List<Entity> routes = null;
	private ArrayAdapter<Route> routeAdapter = null;
	private ArrayAdapter<String> trainingAdapter = null;
	private Spinner selectedRoute = null;
	private Spinner selectedGoal = null;
	private Button startButton = null;
	private MapView mapView = null;
	private TextToSpeech tts = null;
	private GPSManager gpsManager = null;
	private float gpsAccuracy = Constants.GPS_ACCURACY_BAD + 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startrun);

		// always change Media Volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Initialize text-to-speech. This is an asynchronous operation.
		tts = new TextToSpeech(this, this);

		dao = new RouteDAO(this);

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom(Constants.DEFAULT_ZOOM_LEVEL);

		mapView.getOverlays().add(new CurrentLocationOverlay(this, mapView));
		
		gpsManager = new GPSManager(this);
		gpsManager.addObserver(this);

		selectedRoute = (Spinner) findViewById(R.id.selectedRoute);
		selectedGoal = (Spinner) findViewById(R.id.selectedGoal);

		routes = dao.getAll();
		routeAdapter = new ArrayAdapter<Route>(this, android.R.layout.simple_spinner_item);
		routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		routeAdapter.add(Route.getEmptyRoute());

		for (Entity e : routes) {
			routeAdapter.add((Route) e);
		}

		selectedRoute.setAdapter(routeAdapter);
		selectedRoute.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				List<Overlay> overlays = mapView.getOverlays();
				Route r = (Route) selectedRoute.getSelectedItem();

				for (Overlay o : overlays) {
					if (o instanceof RouteOverlay) {
						mapView.getOverlays().remove(o);
					}
				}

				if (!r.equals(Route.getEmptyRoute())) {
					mapView.getOverlays().add(new RouteOverlay(r, null, mapView));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

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
		Route r = (Route) selectedRoute.getSelectedItem();
		Intent runningInfoIntent = new Intent(this, RunningInfoActivity.class);

		runningInfoIntent.putExtra(Constants.ROUTE, r);
		runningInfoIntent.putExtra(Constants.GPS_SIGNAL, gpsAccuracy);

		gpsManager.stop();
		tts.speak(getResources().getString(R.string.audio_startRun), TextToSpeech.QUEUE_FLUSH, null);
		
		this.startActivity(runningInfoIntent);
	}

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}

		super.onDestroy();
	}

	// Implements TextToSpeech.OnInitListener.
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Lanuage data is missing or the language is not supported.
				Log.e(getClass().getName(), "Language is not available.");
			}
		} else {
			// Initialization failed.
			Log.e(getClass().getName(), "Could not initialize TextToSpeech.");
		}
	}

	@Override
	public void notify(Waypoint p) {
		mapView.getController().animateTo(p.getGeoPoint());
		
		if (p.getLocation().hasAccuracy()) {
			gpsAccuracy = p.getLocation().getAccuracy();
			
			Log.i(getClass().getName(),"New Location with Accuracy " + gpsAccuracy);
			
			if (gpsAccuracy > Constants.GPS_ACCURACY_BAD) {
				startButton.setTextColor(R.color.noGps);
				startButton.setText(R.string.run_gpsStartAndWait);
				//startButton.setEnabled(false);
			} else if (gpsAccuracy > Constants.GPS_ACCURACY_MEDIUM) {
				startButton.setTextColor(R.color.mediumGps);
				startButton.setText(R.string.run_start);
				//startButton.setEnabled(true);
			} else {
				startButton.setTextColor(R.color.goodGps);
				startButton.setText(R.string.run_start);
				//startButton.setEnabled(true);
			}		
		} else {
			gpsAccuracy = Constants.GPS_ACCURACY_BAD + 1;
			
			startButton.setTextColor(R.color.noGps);
			startButton.setText(R.string.run_gpsWait);
			//startButton.setEnabled(false);
		}
		
		// redraw button
		startButton.invalidate();
	}
}