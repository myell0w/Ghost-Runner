package at.ac.tuwien.hci.ghost.ui.goal;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.GoalAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.ui.route.AllRunsOfRouteActivity;

public class GoalsActivity extends ListActivity {
	private static final int VIEW_ALL_RUNS = 1;

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

		this.setContentView(R.layout.routes);        
		this.setListAdapter(adapter);
	}

	private List<Goal> getAllGoals() {
		List<Entity> entites = dao.search(null);
		List<Goal> goals = new ArrayList<Goal>(entites.size());

		for (Entity e : entites) {
			goals.add((Goal)e);
		}

		return goals;
	}
}
