package at.ac.tuwien.hci.ghost.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.MenuItem;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.ui.PreferenceActivity;
import at.ac.tuwien.hci.ghost.ui.WeatherActivity;

public class Util {

	public static boolean canReachIntent(Context context, Intent intent) {
		final PackageManager pm = context.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

		return list.size() > 0;
	}
	
	/* Creates the menu items */
	public static void onCreateOptionsMenu(Context context, Menu menu) {
		menu.add(0, Constants.MENU_SETTINGS, 1, context.getResources().getString(R.string.app_settings)).setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, Constants.MENU_WEATHER, 2, context.getResources().getString(R.string.app_weather)).setIcon(R.drawable.menu_weather);
	}

	/* Handles menu item selections */
	public static boolean onOptionsItemSelected(Context context, MenuItem item) {
		switch (item.getItemId()) {
		case Constants.MENU_SETTINGS:
			Intent prefIntent = new Intent(context, PreferenceActivity.class);
			context.startActivity(prefIntent);
			
			return true;

		case Constants.MENU_WEATHER:
			Intent weatherIntent = new Intent(context, WeatherActivity.class);
			context.startActivity(weatherIntent);

			return true;
		}
		
		return false;
	}
}
