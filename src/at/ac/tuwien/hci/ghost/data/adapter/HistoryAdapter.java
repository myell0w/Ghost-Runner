package at.ac.tuwien.hci.ghost.data.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Run;

public class HistoryAdapter extends ArrayAdapter<Run> {
	private List<Run> runs;

	public HistoryAdapter(Context context, int textViewResourceId, List<Run> runs) {
		super(context, textViewResourceId, runs);
		this.runs = runs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.history_listitem, null);
		}

		Run r = runs.get(position);

		if (r != null) {
			ImageView runIcon = (ImageView) v.findViewById(R.id.runIcon);
			TextView runDate = (TextView) v.findViewById(R.id.runDate);
			TextView runStats = (TextView) v.findViewById(R.id.runStat);

			if (runIcon != null) {
				int imageId = r.getPerformanceImageResourceId();
				
				if (imageId != -1) {
					runIcon.setImageResource(imageId);
				}
			}
			
			if (runDate != null) {
				String date = r.getDate().toString() + " - ";

				if (r.hasRoute()) {
					date += r.getRoute().getName();
				} else {
					date += getContext().getString(R.string.history_noRoute);
				}

				runDate.setText(date);
			}

			if (runStats != null) {
				String stats = String.format("%.2f", r.getDistanceInKm()) + " " + getContext().getString(R.string.app_unitDistance) + ", " + r.getTimeString() + " "
						+ getContext().getString(R.string.app_unitTime) + ", " + r.getPaceString() + " " + getContext().getString(R.string.app_unitPace);
				runStats.setText(stats);
			}
		}
		return v;
	}
}
