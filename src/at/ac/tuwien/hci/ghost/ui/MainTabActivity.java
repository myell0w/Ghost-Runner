package at.ac.tuwien.hci.ghost.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.R.string;
import at.ac.tuwien.hci.ghost.ui.goal.GoalsActivity;
import at.ac.tuwien.hci.ghost.ui.history.HistoryActivity;
import at.ac.tuwien.hci.ghost.ui.route.RoutesActivity;
import at.ac.tuwien.hci.ghost.ui.run.StartRunActivity;


public class MainTabActivity extends android.app.TabActivity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        final TabHost tabHost = getTabHost();

	        tabHost.addTab(tabHost.newTabSpec("TabRun")
	                .setIndicator(this.getResources().getText(R.string.app_tabRun))
	                .setContent(new Intent(this, StartRunActivity.class)));

	        tabHost.addTab(tabHost.newTabSpec("TabRoutes")
	        		.setIndicator(this.getResources().getText(R.string.app_tabRoutes))
	                .setContent(new Intent(this, RoutesActivity.class)));
	        
	        // This tab sets the intent flag so that it is recreated each time
	        // the tab is clicked.
	        tabHost.addTab(tabHost.newTabSpec("TabHistory")
	        		.setIndicator(this.getResources().getText(R.string.app_tabHistory))
	                .setContent(new Intent(this, HistoryActivity.class)
	                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
	        
	        tabHost.addTab(tabHost.newTabSpec("TabGoals")
	        		.setIndicator(this.getResources().getText(R.string.app_tabGoals),this.getResources().getDrawable(R.drawable.tab_goal))
	                .setContent(new Intent(this, GoalsActivity.class)));
	    }
}
