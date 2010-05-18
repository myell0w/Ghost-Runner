package at.ac.tuwien.hci.ghost.ui;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;
import at.ac.tuwien.hci.ghost.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference pref = (EditTextPreference) findPreference("speakInterval");
		EditText txt = (EditText)pref.getEditText(); 
		txt.setKeyListener(DigitsKeyListener.getInstance(false,true));
		
		pref = (EditTextPreference) findPreference("weight");
		txt = (EditText)pref.getEditText();
		txt.setKeyListener(DigitsKeyListener.getInstance(false,true));
		
		pref = (EditTextPreference) findPreference("age");
		txt = (EditText)pref.getEditText();
		txt.setKeyListener(DigitsKeyListener.getInstance(false,true));
		
		pref = (EditTextPreference) findPreference("size");
		txt = (EditText)pref.getEditText();
		txt.setKeyListener(DigitsKeyListener.getInstance(false,true));
	}
}
