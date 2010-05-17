package at.ac.tuwien.hci.ghost.ui.route;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RouteAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Util;

public class RoutesActivity extends ListActivity {
	private static final int VIEW_ALL_RUNS = 1;

	/** all routes */
	private List<Route> routes = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private RouteAdapter adapter = null;
	private ListView listView = null;

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

		listView = (ListView) findViewById(android.R.id.list);

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(R.string.app_contextMenuTitle);
				menu.add(0, Constants.MENU_DELETE, 0, R.string.routes_delete);
			}
		});
	}

	private List<Route> getAllRoutes() {
		List<Entity> entites = dao.getAll();
		List<Route> routes = new ArrayList<Route>(entites.size());

		for (Entity e : entites) {
			routes.add((Route) e);
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
		
		switch (item.getItemId()) {
		case Constants.MENU_DELETE:
			dao.delete(routes.get(menuInfo.position).getID());
			routes.remove(menuInfo.position);
			
			adapter = new RouteAdapter(this, R.layout.routes_listitem, routes);
			this.setListAdapter(adapter);
			
			return true;
		}

		return false;
	}
}
