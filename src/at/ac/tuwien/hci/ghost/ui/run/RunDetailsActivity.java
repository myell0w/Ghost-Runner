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
		
		run = (Run)getIntent().getExtras().getSerializable(Constants.RUN);

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);
		
		TextView heading = (TextView)findViewById(R.id.runHeading);
		heading.setText(run.getDate().toFullString());
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}