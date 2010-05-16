package at.ac.tuwien.hci.ghost.ui.route;

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
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RouteAdapter;
import at.ac.tuwien.hci.ghost.data.adapter.RunAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RouteDAO;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.ui.run.RunDetailsActivity;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Util;

public class AllRunsOfRouteActivity extends ListActivity {
	private static final int VIEW_RUN_DETAIL = 1;
	

	/** the route */
	private Route route = null;
	/** all routes */
	private List<Run> runs = null;
	/** DAO for retrieving Routes */
	private DataAccessObject runDao = null;

	private DataAccessObject routeDao = null;
	/** Adapter for combining Entities and ListView */
	private RunAdapter adapter = null;
	private ListView listView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get Route from the Intent
		route = (Route) getIntent().getExtras().getSerializable(Constants.ROUTE);

		// create dao-objects
		runDao = new RunDAO(this);
		routeDao = new RouteDAO(this);
		// get all routes
		runs = getAllRuns();
		// create adapter
		adapter = new RunAdapter(this, R.layout.allrunsofroute_listitem, runs);

		this.setContentView(R.layout.allrunsofroute);
		this.setListAdapter(adapter);

		TextView heading = (TextView) this.findViewById(R.id.routeHeading);
		heading.setText(route.getName());
		
		listView = (ListView) findViewById(android.R.id.list);

		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(R.string.app_contextMenuTitle);
				menu.add(0, Constants.MENU_DELETE, 0, R.string.run_delete);
			}
		});
	}

	private List<Run> getAllRuns() {
		return ((RunDAO) runDao).getAllRunsOfRoute(route);
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
		menu.add(0, Constants.MENU_DELETE, 0, getResources().getString(R.string.routes_delete)).setIcon(android.R.drawable.ic_menu_delete);
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
		case Constants.MENU_DELETE:
			routeDao.delete(route.getID());
			finish();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 
		
		switch (item.getItemId()) {
		case Constants.MENU_DELETE:
			runDao.delete(runs.get(menuInfo.position).getID());
			runs.remove(menuInfo.position);
			
			adapter = new RunAdapter(this, R.layout.allrunsofroute_listitem, runs);
			this.setListAdapter(adapter);
			
			return true;
		}

		return false;
	}
}
