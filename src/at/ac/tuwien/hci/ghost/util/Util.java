package at.ac.tuwien.hci.ghost.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class Util {

	public static boolean canReachIntent(Context context, Intent intent) {
		final PackageManager pm = context.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

		return list.size() > 0;
	}
}
