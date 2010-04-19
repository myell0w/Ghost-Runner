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
import android.widget.Spinner;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Period;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Type;

public class NewGoalActivity extends Activity implements TextWatcher {
	public static final int STATE_EDIT = 0;
    public static final int STATE_INSERT = 1;

    private int mState;
    private Goal goal;
    
    /** menu constans */
	private final int MENU_GOALEDIT_DISCARD = 101;
	private final int MENU_GOALEDIT_DELETE = 102;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.newgoal);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle.containsKey("actionType"))
			mState = bundle.getInt("actionKey");
		if(bundle.containsKey("newGoal"))
			goal = (Goal) bundle.get("newGoal");
		
		Spinner spinnerType=(Spinner)findViewById(R.id.selectedType);
		ArrayAdapter<Type> adapterType = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Goal.Type.values());
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(adapterType);
		spinnerType.setOnItemSelectedListener(
	            new AdapterView.OnItemSelectedListener() {
	                public void onItemSelected(
	                        AdapterView<?> parent, 
	                        View view, 
	                        int position, 
	                        long id) {
	                    saveGoal();
	                }

	                public void onNothingSelected(AdapterView<?> parent) {
	                }
	            }
	        );

		Spinner spinnerPeriod = (Spinner) findViewById(R.id.selectedPeriod);
		ArrayAdapter<Period> adapterPeriod = new ArrayAdapter<Period>(this, android.R.layout.simple_spinner_item, Goal.Period.values());
		adapterPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPeriod.setAdapter(adapterPeriod);	
		spinnerPeriod.setOnItemSelectedListener(
	            new AdapterView.OnItemSelectedListener() {
	                public void onItemSelected(
	                        AdapterView<?> parent, 
	                        View view, 
	                        int position, 
	                        long id) {
	                    saveGoal();
	                }

	                public void onNothingSelected(AdapterView<?> parent) {
	                }
	            }
	        );
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		Log.i(NewGoalActivity.class.toString(),"INFO: afterTextChanged");
		EditText textGoal = (EditText)findViewById(R.id.textGoal);
		if(s.equals(textGoal.getText())) {
			try {
				goal.setGoalValue(Float.valueOf(textGoal.getText().toString()));
				Log.i(NewGoalActivity.class.toString(),"INFO: save Text");
			} catch(Exception e) {
				Log.i(NewGoalActivity.class.toString(),"ERROR: during getText");
			}
		}
	}
	
	public void saveGoal() {
		Spinner spinnerType=(Spinner)findViewById(R.id.selectedType);
		goal.setType((Type)spinnerType.getSelectedItem());
		
		Spinner spinnerPeriod=(Spinner)findViewById(R.id.selectedPeriod);
		goal.setPeriod((Period)spinnerPeriod.getSelectedItem());
		
		EditText textGoal = (EditText)findViewById(R.id.textGoal);
		try {
			goal.setGoalValue(Float.valueOf(textGoal.getText().toString()));
		} catch(Exception e) {
			Log.i(NewGoalActivity.class.toString(),"ERROR: during getText");
		}
		
		//TODO: save
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mState == STATE_EDIT) {
			//TODO: call update
		} else if (mState == STATE_INSERT) {
			//TODO: call update
		}
	}
	
	/* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
    	if(mState == STATE_EDIT)
    		menu.add(0, MENU_GOALEDIT_DISCARD, 0, getResources().getString(R.string.goals_discard))
    			.setIcon(android.R.drawable.ic_menu_delete);
    	else
    		menu.add(0, MENU_GOALEDIT_DELETE, 0, getResources().getString(R.string.goals_delete))
    			.setIcon(android.R.drawable.ic_menu_delete);
    	
        return true;
    }
	
	/* Handles menu item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_GOALEDIT_DISCARD:
            // TODO delete Goal
        	super.finish();
            return true;
        case MENU_GOALEDIT_DELETE:
        	// TODO delete Goal
        	super.finish();
        	return true;
        }
        return false;
    }

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
}
