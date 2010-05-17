package at.ac.tuwien.hci.ghost.ui.goal;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.GoalAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Util;

public class GoalsActivity extends ListActivity {
	/** all goals */
	private List<Goal> goals = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private GoalAdapter adapter = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create dao-object
		dao = new GoalDAO();
		// get all routes
		goals = getAllGoals();
		// create adapter
		adapter = new GoalAdapter(this, R.layout.goals_listitem, goals);

		this.setContentView(R.layout.goals);
		this.setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("Resuming GOALS");
		goals = getAllGoals();
		adapter = new GoalAdapter(this, R.layout.goals_listitem, goals);
		this.setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Goal g = goals.get(position);

		Intent newGoalIntent = new Intent(this, NewGoalActivity.class);
		newGoalIntent.putExtra("newGoal", g);
		newGoalIntent.putExtra("actionType", NewGoalActivity.STATE_EDIT);
		this.startActivity(newGoalIntent);
	}

	private List<Goal> getAllGoals() {
		List<Entity> entites = dao.getAll();
		List<Goal> goals = new ArrayList<Goal>(entites.size());

		for (Entity e : entites) {
			goals.add((Goal) e);
		}

		return goals;
	}

	/* Creates the menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Constants.MENU_NEW_GOAL, 0, getResources().getString(R.string.goals_new)).setIcon(android.R.drawable.ic_menu_add);
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
		case Constants.MENU_NEW_GOAL:
			Intent newGoalIntent = new Intent(this, NewGoalActivity.class);
			Goal goal = new Goal(1);
			long newId = ((GoalDAO) dao).insert(goal);
			goal.setID(newId);
			newGoalIntent.putExtra("newGoal", goal);
			newGoalIntent.putExtra("actionType", NewGoalActivity.STATE_INSERT);
			this.startActivity(newGoalIntent);
			// onResume();
			return true;
		}

		return false;
	}
}
