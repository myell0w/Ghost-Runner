package at.ac.tuwien.hci.ghost.data.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Goal;

public class GoalAdapter extends ArrayAdapter<Goal> {
	private List<Goal> goals;

	public GoalAdapter(Context context, int textViewResourceId, List<Goal> goals) {
		super(context, textViewResourceId, goals);
		this.goals = goals;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.goals_listitem, null);
		}

		Goal g = goals.get(position);

		if (g != null) {
			TextView goalDescription = (TextView) v.findViewById(R.id.goalDescription);
			ProgressBar progress = (ProgressBar) v.findViewById(R.id.goalProgress);

			if (goalDescription != null) {
				goalDescription.setText(this.typeDescription(g));
			}

			if (progress != null) {
				progress.setProgress(g.getProgressPercentage());
			}
		}
		return v;
	}

	private String typeDescription(Goal goal) {
		switch (goal.getType()) {
		case CALORIES:
			return getContext().getResources().getString(R.string.goals_calories) + ": " + (int) goal.getGoalValue() + " "
					+ getContext().getResources().getString(R.string.app_unitCalories);

		case DISTANCE:
			return getContext().getResources().getString(R.string.goals_distance) + ": " + goal.getGoalValue() + " "
					+ getContext().getResources().getString(R.string.app_unitDistance);

		case RUNS:
			return getContext().getResources().getString(R.string.goals_runs) + ": " + (int) goal.getGoalValue();

		default:
			return getContext().getResources().getString(R.string.goals_none);
		}
	}
}
