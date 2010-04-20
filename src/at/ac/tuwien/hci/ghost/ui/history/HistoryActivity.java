package at.ac.tuwien.hci.ghost.ui.history;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.HistoryAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.ui.run.RunDetailsActivity;
import at.ac.tuwien.hci.ghost.util.Constants;

public class HistoryActivity extends ListActivity {
	private static final int VIEW_RUN_DETAIL = 1;

	/** all goals */
	private List<Run> runs = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private HistoryAdapter adapter = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create dao-object
		dao = new RunDAO(this);
		// get all routes
		runs = getRunsInMonth(3, 2010);
		// create adapter
		adapter = new HistoryAdapter(this, R.layout.history_listitem, runs);

		this.setListAdapter(adapter);
		this.setContentView(R.layout.history);
	}

	private List<Run> getRunsInMonth(int month, int year) {
		return ((RunDAO) dao).getAllRunsInMonth(month, year);
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
}
