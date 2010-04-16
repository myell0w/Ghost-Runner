package at.ac.tuwien.hci.ghost.data.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Run;

public class RunAdapter extends ArrayAdapter<Run> {
	private List<Run> runs;

	public RunAdapter(Context context, int textViewResourceId, List<Run> runs) {
		super(context, textViewResourceId, runs);
		this.runs = runs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.allrunsofroute_listitem, null);
		}
		
		Run r = runs.get(position);
		
		if (r != null) {
			TextView runStats = (TextView) v.findViewById(R.id.runStat);
			TextView runDate = (TextView) v.findViewById(R.id.runDate);
			
			if (runStats != null) {
				String stats = r.getTimeString() + " " + getContext().getString(R.string.app_unitTime) + ", " +
							   r.getPaceString() + " " + getContext().getString(R.string.app_unitPace);
				runStats.setText(stats);
			}
			
			if (runDate != null) {
				runDate.setText(r.getDate().toFullString());
			}
		}
		return v;
	}
}
