package at.ac.tuwien.hci.ghost.ui.history;

import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.HistoryAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.ui.goal.NewGoalActivity;
import at.ac.tuwien.hci.ghost.ui.run.RunDetailsActivity;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;
import at.ac.tuwien.hci.ghost.util.Util;

public class HistoryActivity extends ListActivity {
	private static final int VIEW_RUN_DETAIL = 1;

	/** all goals */
	private List<Run> runs = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private HistoryAdapter adapter = null;

	/** selected Month */
	private Date month = new Date();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.history);
		
		// create dao-object
		dao = new RunDAO(this);

		// get all routes and set Header
		month = new Date();
		Log.e(this.getClass().toString(), "INFO: " + month.getMonth() + " " + month.getYear());
		runs = getRunsInMonth(month.getMonth(), month.getYear());

		// create adapter
		adapter = new HistoryAdapter(this, R.layout.history_listitem, runs);

		// add ActionListener to Buttons
		((ImageButton) findViewById(R.id.prevMonth)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				month.add(Calendar.MONTH, -1);
				runs = getRunsInMonth(month.getMonth(), month.getYear());
				adapter = new HistoryAdapter(HistoryActivity.this, R.layout.history_listitem, runs);
				HistoryActivity.this.setListAdapter(adapter);
			}
		});

		((ImageButton) findViewById(R.id.nextMonth)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				month.add(Calendar.MONTH, 1);
				runs = getRunsInMonth(month.getMonth(), month.getYear());
				adapter = new HistoryAdapter(HistoryActivity.this, R.layout.history_listitem, runs);
				HistoryActivity.this.setListAdapter(adapter);
			}
		});

		this.setListAdapter(adapter);
	}

	private List<Run> getRunsInMonth(int month, int year) {
		List<Run> ret=((RunDAO) dao).getAllRunsInMonth(month, year);

		TextView monthName = (TextView) findViewById(R.id.monthName);
		switch(month) {
		case 1: monthName.setText(getResources().getString(R.string.history_jan)); break;
		case 2: monthName.setText(getResources().getString(R.string.history_feb)); break;
		case 3: monthName.setText(getResources().getString(R.string.history_mar)); break;
		case 4: monthName.setText(getResources().getString(R.string.history_apr)); break;
		case 5: monthName.setText(getResources().getString(R.string.history_may)); break;
		case 6: monthName.setText(getResources().getString(R.string.history_jun)); break;
		case 7: monthName.setText(getResources().getString(R.string.history_jul)); break;
		case 8: monthName.setText(getResources().getString(R.string.history_aug)); break;
		case 9: monthName.setText(getResources().getString(R.string.history_sep)); break;
		case 10: monthName.setText(getResources().getString(R.string.history_oct)); break;
		case 11: monthName.setText(getResources().getString(R.string.history_nov)); break;
		case 12: monthName.setText(getResources().getString(R.string.history_dec)); break;
		default: monthName.setText("Error");
		}
		monthName.setText(monthName.getText() + " " + String.valueOf(year));

		//TODO: set Distance, avg Pace & Calories
		float totalDistance=0, totalTimeInSeconds=0;
		int totalCalories=0;
		for(Run r:ret) {
			totalDistance += r.getDistanceInKm();
			totalTimeInSeconds += r.getTime();
			totalCalories += r.getCalories();
		}
		
		TextView detailTotalDistance = (TextView) findViewById(R.id.detailTotalDistance);
		detailTotalDistance.setText(String.valueOf(totalDistance) + " " + getResources().getString(R.string.app_unitDistance));
		
		TextView detailAveragePace = (TextView) findViewById(R.id.detailAveragePace);
		if(totalDistance == 0)
			detailAveragePace.setText("0.00 " + getResources().getString(R.string.app_unitPace));
		else
			detailAveragePace.setText(Run.getPaceString(totalTimeInSeconds/60/totalDistance) + " " + getResources().getString(R.string.app_unitPace));
		
		TextView detailTotalCalories = (TextView) findViewById(R.id.detailTotalCalories);
		detailTotalCalories.setText(String.valueOf(totalCalories) + " " + getResources().getString(R.string.app_unitCalories));
		
		return ret;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Run r = runs.get(position);

		Intent allRunsIntent = new Intent(this, RunDetailsActivity.class);
		allRunsIntent.putExtra(Constants.RUN, r);

		this.startActivityForResult(allRunsIntent, VIEW_RUN_DETAIL);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		// TODO
		}
	}
	
	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Util.onCreateOptionsMenu(this, menu);

		return true;
	}

	/* Handles menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Util.onOptionsItemSelected(this, item)) {
			return true;
		}

		switch (item.getItemId()) {
		
		}

		return false;
	}
}
