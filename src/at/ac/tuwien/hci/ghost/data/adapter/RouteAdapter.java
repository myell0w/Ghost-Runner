package at.ac.tuwien.hci.ghost.data.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Route;

public class RouteAdapter extends ArrayAdapter<Route> {
	private List<Route> routes;

	public RouteAdapter(Context context, int textViewResourceId, List<Route> routes) {
		super(context, textViewResourceId, routes);
		this.routes = routes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.routes_listitem, null);
		}

		Route r = routes.get(position);

		if (r != null) {
			TextView routeName = (TextView) v.findViewById(R.id.routeName);
			TextView runCount = (TextView) v.findViewById(R.id.routeRunCount);

			if (routeName != null) {
				routeName.setText(r.getName());
			}

			if (runCount != null) {
				runCount.setText(String.format("%.2f",r.getDistance()) + " " + getContext().getResources().getString(R.string.app_unitDistance) + ", " + r.getRunCount() + " "
						+ getContext().getResources().getString(R.string.routes_runs));
			}
		}
		return v;
	}
}
