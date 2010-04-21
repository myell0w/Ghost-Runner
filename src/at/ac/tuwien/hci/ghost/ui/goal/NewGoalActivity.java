package at.ac.tuwien.hci.ghost.ui.goal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Type;

public class NewGoalActivity extends Activity implements TextWatcher {
	public static final int STATE_EDIT = 0;
	public static final int STATE_INSERT = 1;

	private int mState;
	private Goal goal;
	private GoalDAO goalDAO;

	/** menu constants */
	private final int MENU_GOALEDIT_DISCARD = 101;
	private final int MENU_GOALEDIT_DELETE = 102;
	
	private final int SEEKBAR_WIDTH = 100;
	
	private final int MIN_CALORIES = 500;
	private final int MAX_CALORIES = 50000;
	
	private final int MIN_DISTANCE = 10;
	private final int MAX_DISTANCE = 1000;

	private final int MIN_RUNS = 1;
	private final int MAX_RUNS = 50;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.newgoal);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle.containsKey("actionType"))
			mState = bundle.getInt("actionType");
		if (bundle.containsKey("newGoal"))
			goal = (Goal) bundle.get("newGoal");

		goalDAO = new GoalDAO(this);

		Spinner spinnerType = (Spinner) findViewById(R.id.selectedType);
		ArrayAdapter<Type> adapterType = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Goal.Type.values());
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(adapterType);
		
		SeekBar seekGoal = (SeekBar) findViewById(R.id.seekGoal);
		seekGoal.setMax(SEEKBAR_WIDTH);
		seekGoal.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int span=1;
				float value = 0;
				
				TextView textGoalValue = (TextView) findViewById(R.id.textGoalValue);
				
				if(goal.getType() == Goal.Type.CALORIES) {
					span = MAX_CALORIES - MIN_CALORIES;
					value = span/SEEKBAR_WIDTH*progress + MIN_CALORIES;
					textGoalValue.setText(String.format("%s: %.0f %s",getResources().getString(R.string.goals_calories), value, getResources().getString(R.string.app_unitCalories)));
				}
				if(goal.getType() == Goal.Type.DISTANCE) {
					span = MAX_DISTANCE - MIN_DISTANCE;
					value = span/SEEKBAR_WIDTH*progress + MIN_DISTANCE;
					textGoalValue.setText(String.format("%s: %.0f %s",getResources().getString(R.string.goals_distance), value, getResources().getString(R.string.app_unitDistance)));
				}
				if(goal.getType() == Goal.Type.RUNS){
					span = MAX_RUNS - MIN_RUNS;
					value = span/SEEKBAR_WIDTH*progress + MIN_RUNS;
					textGoalValue.setText(String.format("%s: %.0f",getResources().getString(R.string.goals_runs), value));
				}
				
				goal.setGoalValue(value);
				
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
				Spinner spinnerType = (Spinner) findViewById(R.id.selectedType);
				goal.setType((Type) spinnerType.getSelectedItem());
				
				SeekBar seekGoal = (SeekBar) findViewById(R.id.seekGoal);
				seekGoal.setProgress((int) SEEKBAR_WIDTH/2);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		TextView header = (TextView) findViewById(R.id.newgoalHeading);
		if (mState == STATE_EDIT) {
			header.setText("Edit Goal");
			spinnerType.setSelection(goal.getType().ordinal());
			EditText textGoal = (EditText) findViewById(R.id.textGoal);
			textGoal.setText(String.valueOf(goal.getGoalValue()));
			
			//setSeekBar
			int span=1;
			float value = 0;
			
			if(goal.getType() == Goal.Type.CALORIES) {
				span = MAX_CALORIES - MIN_CALORIES;
				value = goal.getGoalValue() - MIN_CALORIES;
			}
			if(goal.getType() == Goal.Type.DISTANCE) {
				span = MAX_DISTANCE - MIN_DISTANCE;
				value = goal.getGoalValue() - MIN_DISTANCE;
			}
			if(goal.getType() == Goal.Type.RUNS){
				span = MAX_RUNS - MIN_RUNS;
				value = goal.getGoalValue() - MIN_RUNS;
			}
			
			seekGoal.setProgress((int)value/span*SEEKBAR_WIDTH);
			
		} else {
			header.setText("New Goal");
			seekGoal.setProgress((int) SEEKBAR_WIDTH/2);
		}

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

	public void saveGoal() {
		Spinner spinnerType = (Spinner) findViewById(R.id.selectedType);
		goal.setType((Type) spinnerType.getSelectedItem());

		// Spinner spinnerPeriod=(Spinner)findViewById(R.id.selectedPeriod);
		// goal.setPeriod((Period)spinnerPeriod.getSelectedItem());

		EditText textGoal = (EditText) findViewById(R.id.textGoal);

		try {
			goal.setGoalValue(Float.valueOf(textGoal.getText().toString()));
		} catch (Exception e) {
			Log.i(NewGoalActivity.class.toString(), "ERROR: during getText");
		}

		goal.print();
		goalDAO.update(goal);
	}

	@Override
	public void onPause() {
		super.onPause();
		saveGoal();
		if (mState == STATE_EDIT) {
			goalDAO.update(goal);
		} else if (mState == STATE_INSERT) {
			goalDAO.update(goal);
		}
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mState == STATE_INSERT)
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

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i(NewGoalActivity.class.toString(), "INFO: afterTextChanged");
		EditText textGoal = (EditText) findViewById(R.id.textGoal);
		try {
			goal.setGoalValue(Float.valueOf(textGoal.getText().toString()));
			Log.i(NewGoalActivity.class.toString(), "INFO: save Text");
		} catch (Exception e) {
			Log.i(NewGoalActivity.class.toString(), "ERROR: during getText");
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.i(NewGoalActivity.class.toString(), "INFO: afterTextChanged");
		EditText textGoal = (EditText) findViewById(R.id.textGoal);
		if (s.equals(textGoal.getText())) {
			try {
				goal.setGoalValue(Float.valueOf(textGoal.getText().toString()));
				Log.i(NewGoalActivity.class.toString(), "INFO: save Text");
			} catch (Exception e) {
				Log.i(NewGoalActivity.class.toString(), "ERROR: during getText");
			}
		}
	}
}
