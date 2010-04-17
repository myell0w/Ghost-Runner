package at.ac.tuwien.hci.ghost.ui.history;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.adapter.RunAdapter;
import at.ac.tuwien.hci.ghost.data.dao.DataAccessObject;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Run;

public class HistoryActivity extends ListActivity {
	/** all goals */
	private List<Run> runs = null;
	/** DAO for retrieving Routes */
	private DataAccessObject dao = null;
	/** Adapter for combining Entities and ListView */
	private RunAdapter adapter = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// create dao-object
		dao = new RunDAO();
		// get all routes
		runs = getRunsInMonth(3);
		// create adapter
		adapter = new RunAdapter(this, R.layout.history_listitem, runs);
		
		this.setListAdapter(adapter);
		this.setContentView(R.layout.history);        
	}

	private List<Run> getRunsInMonth(int month) {
		return ((RunDAO)dao).getAllRunsInMonth(month);
	}
}
