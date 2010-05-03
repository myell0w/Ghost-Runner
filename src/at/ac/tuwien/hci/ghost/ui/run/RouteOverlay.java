package at.ac.tuwien.hci.ghost.ui.run;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class RouteOverlay extends com.google.android.maps.Overlay {
	Run run = null;
	ArrayList<GeoPoint> geopoints = new ArrayList<GeoPoint>();

	public RouteOverlay(Run run, MapView mv) {
		this.run = run;

		updateGeoPoints();
	}

	private void updateGeoPoints() {
		geopoints.clear();

		if (run.getWaypoints() != null) {
			for (Waypoint p : run.getWaypoints()) {
				geopoints.add(new GeoPoint((int) (p.getLatitudeDegrees() * 1E6), (int) (p.getLongitudeDegrees() * 1E6)));
			}
		}
	}

	@Override
	public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
		super.draw(canvas, mv, shadow);

		updateGeoPoints();
		drawPath(mv, canvas);

		return true;
	}

	public void drawPath(MapView mv, Canvas canvas) {
		int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
		Paint paint = new Paint();

		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);

		for (int i = 0; i < geopoints.size(); i++) {
			Point point = new Point();

			mv.getProjection().toPixels(geopoints.get(i), point);
			x2 = point.x;
			y2 = point.y;

			if (i > 0) {
				canvas.drawLine(x1, y1, x2, y2, paint);
			}

			x1 = x2;
			y1 = y2;
		}
	}
}