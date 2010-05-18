package at.ac.tuwien.hci.ghost.ui.goal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Type;

public class NewGoalActivity extends Activity {
	public static final int STATE_EDIT = 0;
	public static final int STATE_INSERT = 1;

	private int state;
	private Goal goal;
	private GoalDAO goalDAO;
	private boolean justCreated = true;

	/** menu constants */
	private final int MENU_GOALEDIT_DISCARD = 101;
	private final int MENU_GOALEDIT_DELETE = 102;

	private final int SEEKBAR_WIDTH = 100;

	private final int MIN_CALORIES = 500;
	private final int MAX_CALORIES = 20000;

	private final int MIN_DISTANCE = 10;
	private final int MAX_DISTANCE = 500;

	private final int MIN_RUNS = 1;
	private final int MAX_RUNS = 50;
	private SeekBar seekGoal;
	private Spinner spinnerType;
	private TextView textGoalValue;
	private TextView textHeader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.newgoal);

		justCreated = true;
		// get ui elements
		textHeader = (TextView) findViewById(R.id.newgoalHeading);
		spinnerType = (Spinner) findViewById(R.id.selectedType);
		textGoalValue = (TextView) findViewById(R.id.textGoalValue);
		seekGoal = (SeekBar) findViewById(R.id.seekGoal);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle.containsKey("actionType"))
			state = bundle.getInt("actionType");
		if (bundle.containsKey("newGoal"))
			goal = (Goal) bundle.get("newGoal");

		goalDAO = new GoalDAO();

		ArrayAdapter<Type> adapterType = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Goal.Type.values());
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(adapterType);

		if (state == STATE_EDIT) {
			textHeader.setText("Edit Goal");
			spinnerType.setSelection(goal.getType().ordinal());

			// setSeekBar
			float span = 1;

			if (goal.getType().equals(Goal.Type.CALORIES)) {
				span = MAX_CALORIES - MIN_CALORIES;
			} else if (goal.getType().equals(Goal.Type.DISTANCE)) {
				span = MAX_DISTANCE - MIN_DISTANCE;
			} else if (goal.getType().equals(Goal.Type.RUNS)) {
				span = MAX_RUNS - MIN_RUNS;
			}

			seekGoal.setProgress((int) (goal.getGoalValue() / span * SEEKBAR_WIDTH));

		} else {
			textHeader.setText("New Goal");
			seekGoal.setProgress(SEEKBAR_WIDTH / 2);
		}

		seekGoal.setMax(SEEKBAR_WIDTH);
		seekGoal.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekBarValueChanged(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});

		spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				goal.setType((Type) spinnerType.getSelectedItem());
				seekBarValueChanged(seekGoal.getProgress());
				// FIXED: commented out, was always set to half value
				// seekGoal.setProgress((int) SEEKBAR_WIDTH / 2);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		updateUI();

		/*
		 * Spinner spinnerPeriod = (Spinner) findViewById(R.id.selectedPeriod);
		 * ArrayAdapter<Period> adapterPeriod = new ArrayAdapter<Period>(this,
		 * android.R.layout.simple_spinner_item, Goal.Period.values());
		 * adapterPeriod
		 * .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
		 * ); spinnerPeriod.setAdapter(adapterPeriod);
		 * spinnerPeriod.setOnItemSelectedListener( new
		 * AdapterView.OnItemSelectedListener() { public void onItemSelected(
		 * AdapterView<?> parent, View view, int position, long id) {
		 * saveGoal(); }
		 * 
		 * public void onNothingSelected(AdapterView<?> parent) { } } );
		 */
	}

	protected void updateUI() {
		String goalDesc = null, goalUnit = null;

		if (goal != null && goal.getType() != null) {
			if (goal.getType().equals(Goal.Type.CALORIES)) {
				goalDesc = getResources().getString(R.string.goals_calories);
				goalUnit = getResources().getString(R.string.app_unitCalories);

			} else if (goal.getType().equals(Goal.Type.DISTANCE)) {
				goalDesc = getResources().getString(R.string.goals_distance);
				goalUnit = getResources().getString(R.string.app_unitDistance);
			} else if (goal.getType().equals(Goal.Type.RUNS)) {
				goalDesc = getResources().getString(R.string.goals_runs);
				goalUnit = getResources().getString(R.string.routes_runs);
			}

			textGoalValue.setText(String.format("%s: %d %s", goalDesc, (int) goal.getGoalValue(), goalUnit));
		}
	}

	public void saveGoal() {
		goal.setType((Type) spinnerType.getSelectedItem());

		// Spinner spinnerPeriod=(Spinner)findViewById(R.id.selectedPeriod);
		// goal.setPeriod((Period)spinnerPeriod.getSelectedItem());

		goal.print();
		goalDAO.update(goal);
	}

	@Override
	public void onPause() {
		super.onPause();
		saveGoal();
		if (state == STATE_EDIT) {
			goalDAO.update(goal);
		} else if (state == STATE_INSERT) {
			goalDAO.update(goal);
		}
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (state == STATE_INSERT)
			menu.add(0, MENU_GOALEDIT_DISCARD, 0, getResources().getString(R.string.goals_discard)).setIcon(android.R.drawable.ic_menu_delete);
		else
			menu.add(0, MENU_GOALEDIT_DELETE, 0, getResources().getString(R.string.goals_delete)).setIcon(android.R.drawable.ic_menu_delete);

		return true;
	}

	/* Handles menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_GOALEDIT_DISCARD:
			goalDAO.delete(goal.getID());
			super.finish();
			return true;
		case MENU_GOALEDIT_DELETE:
			goalDAO.delete(goal.getID());
			super.finish();
			return true;
		}
		return false;
	}

	private void seekBarValueChanged(int progress) {
		float span = 1;
		int goalValue = 0;

		if (goal.getType().equals(Goal.Type.CALORIES)) {
			span = MAX_CALORIES - MIN_CALORIES;
			goalValue = (int) (span / SEEKBAR_WIDTH * progress + MIN_CALORIES);
		} else if (goal.getType().equals(Goal.Type.DISTANCE)) {
			span = MAX_DISTANCE - MIN_DISTANCE;
			goalValue = (int) (span / SEEKBAR_WIDTH * progress + MIN_DISTANCE);
		} else if (goal.getType().equals(Goal.Type.RUNS)) {
			span = MAX_RUNS - MIN_RUNS;
			goalValue = (int) (span / SEEKBAR_WIDTH * progress + MIN_RUNS);
		}

		Log.i("TEST", "before: " + goal.getGoalValue() + " after: " + goalValue);
		if (!justCreated) {
			goal.setGoalValue(goalValue);
		} else {
			justCreated = false;
		}

		updateUI();
	}
}
