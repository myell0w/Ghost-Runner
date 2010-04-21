package at.ac.tuwien.hci.ghost.ui.run;

import android.os.Bundle;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.util.Constants;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RunDetailsActivity extends MapActivity {
	private MapView mapView = null;
	private Run run = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rundetails);

		run = (Run) getIntent().getExtras().getSerializable(Constants.RUN);

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);

		TextView heading = (TextView) findViewById(R.id.runHeading);
		heading.setText(run.getDate().toFullString());
		
		TextView detailTotalTime = (TextView) findViewById(R.id.detailTotalTime);
		detailTotalTime.setText(run.getTimeString() + " " + getResources().getString(R.string.app_unitTime));
		
		TextView detailTotalDistance = (TextView) findViewById(R.id.detailTotalDistance);
		detailTotalDistance.setText(run.getDistance() + " " + getResources().getString(R.string.app_unitDistance));
		
		TextView detailAveragePace = (TextView) findViewById(R.id.detailAveragePace);
		detailAveragePace.setText(run.getPaceString() + " " + getResources().getString(R.string.app_unitPace));
		
		TextView detailAverageSpeed = (TextView) findViewById(R.id.detailAverageSpeed);
		detailAverageSpeed.setText(run.getSpeed() + " " + getResources().getString(R.string.app_unitSpeed));
		
		TextView detailCalories = (TextView) findViewById(R.id.detailTotalCalories);
		detailCalories.setText(run.getCalories() + " " + getResources().getString(R.string.app_unitCalories));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}