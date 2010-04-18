package at.ac.tuwien.hci.ghost.ui.route;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RunAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;

public class AllRunsOfRouteActivity extends ListActivity {
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
        route = (Route)getIntent().getExtras().getSerializable("at.ac.tuwien.hci.ghost.Route");
        
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
}
