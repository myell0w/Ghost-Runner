package at.ac.tuwien.hci.ghost.ui;

import android.os.Bundle;
import at.ac.tuwien.hci.ghost.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
