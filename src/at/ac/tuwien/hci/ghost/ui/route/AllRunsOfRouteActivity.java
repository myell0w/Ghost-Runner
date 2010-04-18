package at.ac.tuwien.hci.ghost.ui.route;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RunAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.ui.run.RunDetailsActivity;
import at.ac.tuwien.hci.ghost.util.Constants;

public class AllRunsOfRouteActivity extends ListActivity {
	private static final int VIEW_RUN_DETAIL = 1;
	
	/** the route */
	private Route route = null;
	/** all routes */
	private List<Run> runs = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private RunAdapter adapter = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get Route from the Intent
        route = (Route)getIntent().getExtras().getSerializable(Constants.ROUTE);
        
        // create dao-object
        dao = new RunDAO();
        // get all routes
        runs = getAllRuns();
        // create adapter
        adapter = new RunAdapter(this, R.layout.allrunsofroute_listitem, runs);
        
        this.setContentView(R.layout.allrunsofroute);        
        this.setListAdapter(adapter);
        
        TextView heading = (TextView)this.findViewById(R.id.routeHeading);
        heading.setText(route.getName());
    }
    
    private List<Run> getAllRuns() {
    	return ((RunDAO)dao).getAllRunsOfRoute(route);
    }
    
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Run r = runs.get(position);
		
		Intent allRunsIntent = new Intent(this, RunDetailsActivity.class); 
		allRunsIntent.putExtra(Constants.RUN, r);
		
		this.startActivityForResult(allRunsIntent, VIEW_RUN_DETAIL);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) {
			//TODO
		}
	}
}
