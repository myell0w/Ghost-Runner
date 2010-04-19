package at.ac.tuwien.hci.ghost.ui.goal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import at.ac.tuwien.hci.ghost.R;

public class NewGoalActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.goals_newgoal);

		Spinner spinnerType=(Spinner)findViewById(R.id.selectedType);
		ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.goalTypes, android.R.layout.simple_spinner_item);
		adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerType.setAdapter(adapterType);

		Spinner spinnerPeriod = (Spinner) findViewById(R.id.selectedType);
		ArrayAdapter<CharSequence> adapterPeriod = ArrayAdapter.createFromResource(this, R.array.goalPeriod, android.R.layout.simple_spinner_item);
		adapterPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPeriod.setAdapter(adapterPeriod);		
	}
}
