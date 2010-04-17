package at.ac.tuwien.hci.ghost.ui.run;

import android.os.Bundle;
import at.ac.tuwien.hci.ghost.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class StartRunActivity extends MapActivity {
	private MapView mapView = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startrun);

		//mapView = (MapView) findViewById(R.id.overviewMap);
		//mapView.setBuiltInZoomControls(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}