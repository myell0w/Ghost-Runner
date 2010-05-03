package at.ac.tuwien.hci.ghost.ui.run;

import android.content.Context;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class CurrentLocationOverlay extends MyLocationOverlay {

	public CurrentLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		
		enableMyLocation();
	}

}
