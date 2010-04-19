package at.ac.tuwien.hci.ghost.ui.goal;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.GoalAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.ui.WeatherActivity;

public class GoalsActivity extends ListActivity {
	
	/** menu constans */
	private final int MENU_GOAL_ADD = 101;
	private final int MENU_GOAL_SETTINGS = 102;
	private final int MENU_WEATHER = 103;
	
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
		System.out.println("FUCK");
		// create dao-object
		dao = new GoalDAO(this);
		// get all routes
		goals = getAllGoals();
		// create adapter
		adapter = new GoalAdapter(this, R.layout.goals_listitem, goals);

		this.setContentView(R.layout.goals);        
		this.setListAdapter(adapter);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		System.out.println("Resuming GOALS");
		goals = getAllGoals();
		adapter = new GoalAdapter(this, R.layout.goals_listitem, goals);
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
	
	/* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_GOAL_ADD, 0, getResources().getString(R.string.goals_new));
    	menu.add(0, MENU_GOAL_SETTINGS, 1, getResources().getString(R.string.app_settings));
    	menu.add(0, MENU_WEATHER, 2, getResources().getString(R.string.app_weather));
    	
        return true;
    }
    
    /* Handles menu item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_GOAL_ADD:
        	Intent newGoalIntent = new Intent(this, NewGoalActivity.class); 
    		this.startActivity(newGoalIntent);
            // TODO code for adding a goal
        	//((GoalDAO)dao).insert("new goal", 2);
        	//onResume();
            return true;
        case MENU_GOAL_SETTINGS:
        	// TODO code for doing settings for goals
        	return true;
        	
        case MENU_WEATHER:
        	Intent weatherIntent = new Intent(this, WeatherActivity.class); 
    		this.startActivity(weatherIntent);
    		
        	return true;
        }
        return false;
    }
}
