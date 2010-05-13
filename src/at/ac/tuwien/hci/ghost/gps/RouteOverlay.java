package at.ac.tuwien.hci.ghost.gps;

import java.util.List;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 * Displays an Overlay over the Map showing the Current selected Route and the current travelled Route
 * 
 * @author Matthias
 *
 */
public class RouteOverlay extends com.google.android.maps.Overlay {
	private Route route = null;
	private Run run = null;
	private List<GeoPoint> runPoints = new Vector<GeoPoint>();
	private List<GeoPoint> routePoints = new Vector<GeoPoint>();

	public RouteOverlay(Route route, Run run, MapView mv) {
		this.route = route;
		this.run = run;

		updateGeoPoints();
	}

	private void updateGeoPoints() {
		if (run != null && run.getWaypoints() != null) {
			runPoints.clear();

			for (Waypoint p : run.getWaypoints()) {
				runPoints.add(new GeoPoint((int) (p.getLatitudeDegrees() * 1E6), (int) (p.getLongitudeDegrees() * 1E6)));
			}
		} else if (route != null && route.getWaypoints() != null) {
			routePoints.clear();

			for (Waypoint p : route.getWaypoints()) {
				routePoints.add(new GeoPoint((int) (p.getLatitudeDegrees() * 1E6), (int) (p.getLongitudeDegrees() * 1E6)));
			}
		}
	}

	@Override
	public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
		super.draw(canvas, mv, shadow);

		updateGeoPoints();
		drawPath(mv, canvas, runPoints, Color.BLUE);
		drawPath(mv, canvas, routePoints, Color.RED);

		return true;
	}

	public void drawPath(MapView mv, Canvas canvas, List<GeoPoint> points, int color) {
		if (points != null && !points.isEmpty()) {
			Paint paint = new Paint();
			Path path = new Path();

			paint.setColor(color);
			paint.setStyle(Paint.Style.STROKE);
			
			// set width depending on zoom level
			if (mv.getZoomLevel() < 13) {
				paint.setStrokeWidth(3);
			} else if (mv.getZoomLevel() == 13) {
				paint.setStrokeWidth(5);
			} else if (mv.getZoomLevel() == 14) {
				paint.setStrokeWidth(7);
			} else if (mv.getZoomLevel() == 15) {
				paint.setStrokeWidth(9);
			} else if (mv.getZoomLevel() == 16) {
				paint.setStrokeWidth(11);
			} else if (mv.getZoomLevel() == 17) {
				paint.setStrokeWidth(13);
			} else {
				paint.setStrokeWidth(15);
			}

			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeJoin(Paint.Join.ROUND);

			for (int i = 0; i < points.size(); i++) {
				Point point = new Point();

				mv.getProjection().toPixels(points.get(i), point);
				
				// move to first position, the draw line to others
				if (i == 0) {
					path.moveTo(point.x, point.y);
				} else {
					path.lineTo(point.x, point.y);
				}
			}
			
			canvas.drawPath(path, paint);
		}
	}
}