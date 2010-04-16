package at.ac.tuwien.hci.ghost.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RouteAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;

public class RoutesActivity extends ListActivity {
	/** all routes */
	private List<Route> routes = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private RouteAdapter adapter = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // create dao-object
        dao = new RouteDAO();
        // get all routes
        routes = getAllRoutes();
        // create adapter
        adapter = new RouteAdapter(this, R.layout.routes_listitem, routes);
        
        this.setContentView(R.layout.routes);        
        this.setListAdapter(adapter);
    }
    
    private List<Route> getAllRoutes() {
    	List<Entity> entites = dao.search(null);
    	List<Route> routes = new ArrayList<Route>(entites.size());
    	
    	for (Entity e : entites) {
    		routes.add((Route)e);
    	}
    	
    	return routes;
    }
}
