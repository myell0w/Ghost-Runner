package at.ac.tuwien.hci.ghost.ui.run;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import at.ac.tuwien.hci.ghost.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class StartRunActivity extends MapActivity {
	private MapView mapView = null;
	private Button startButton = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startrun);

		mapView = (MapView) findViewById(R.id.overviewMap);
		mapView.setBuiltInZoomControls(true);
		
		startButton = (Button)findViewById(R.id.startRunButton);
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent runningInfoIntent = new Intent(StartRunActivity.this, RunningInfoActivity.class); 
				
				StartRunActivity.this.startActivity(runningInfoIntent);
				
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}