package at.ac.tuwien.hci.ghost.ui.route;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RouteAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.util.Constants;

public class RoutesActivity extends ListActivity {
	private static final int VIEW_ALL_RUNS = 1;
	
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
		dao = new RouteDAO(this);
		// get all routes
		routes = getAllRoutes();
		// create adapter
		adapter = new RouteAdapter(this, R.layout.routes_listitem, routes);

		this.setContentView(R.layout.routes);        
		this.setListAdapter(adapter);
	}

	private List<Route> getAllRoutes() {
		List<Entity> entites = dao.getAll();
		List<Route> routes = new ArrayList<Route>(entites.size());

		for (Entity e : entites) {
			routes.add((Route)e);
		}

		return routes;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Route r = routes.get(position);
		
		Intent allRunsIntent = new Intent(this, AllRunsOfRouteActivity.class); 
		allRunsIntent.putExtra(Constants.ROUTE, r);
		
		this.startActivityForResult(allRunsIntent, VIEW_ALL_RUNS);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) {
			//TODO
		}
	}
}
