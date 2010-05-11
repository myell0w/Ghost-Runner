package at.ac.tuwien.hci.ghost.ui;

import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.TabHost;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.data.dao.GoalDAO;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.ui.goal.GoalsActivity;
import at.ac.tuwien.hci.ghost.ui.history.HistoryActivity;
import at.ac.tuwien.hci.ghost.ui.route.RoutesActivity;
import at.ac.tuwien.hci.ghost.ui.run.StartRunActivity;
import at.ac.tuwien.hci.ghost.util.Date;

public class MainTabActivity extends android.app.TabActivity implements OnInitListener {
	private TextToSpeech mTts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize text-to-speech. This is an asynchronous operation.
		mTts = new TextToSpeech(this, this);

		final TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("TabRun").setIndicator(this.getResources().getText(R.string.app_tabRun),
				this.getResources().getDrawable(R.drawable.tab_run)).setContent(new Intent(this, StartRunActivity.class)));

		tabHost.addTab(tabHost.newTabSpec("TabRoutes").setIndicator(this.getResources().getText(R.string.app_tabRoutes),
				this.getResources().getDrawable(R.drawable.tab_routes)).setContent(new Intent(this, RoutesActivity.class)));

		// This tab sets the intent flag so that it is recreated each time the
		// tab is clicked
		tabHost.addTab(tabHost.newTabSpec("TabHistory").setIndicator(this.getResources().getText(R.string.app_tabHistory),
				this.getResources().getDrawable(R.drawable.tab_history)).setContent(
				new Intent(this, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

		tabHost.addTab(tabHost.newTabSpec("TabGoals").setIndicator(this.getResources().getText(R.string.app_tabGoals),
				this.getResources().getDrawable(R.drawable.tab_goal)).setContent(new Intent(this, GoalsActivity.class)));
	}

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}

		super.onDestroy();
	}

	// Implements TextToSpeech.OnInitListener.
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			int result = mTts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Lanuage data is missing or the language is not supported.
				Log.e(getClass().getName(), "Language is not available.");
			} else {
				// Greet the user.
				sayStatsAndMotivation();
			}
		} else {
			// Initialization failed.
			Log.e(getClass().getName(), "Could not initialize TextToSpeech.");
		}
	}

	private void sayStatsAndMotivation() {
		RunDAO runDAO = new RunDAO(this);
		GoalDAO goalDAO = new GoalDAO(this);
		Run lastCompletedRun = runDAO.getLastCompletedRun();
		String lastRunString = "";
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if (prefs.getBoolean("speakStats", true)) {
			if (lastCompletedRun != null) {
				long dayDifference = lastCompletedRun.getDate().getDayDifference(new Date());
				lastRunString = getResources().getString(R.string.audio_lastRun_1) + " " + dayDifference + " "
						+ getResources().getString(R.string.audio_lastRun_2);
			} else {
				lastRunString = getResources().getString(R.string.audio_lastRunNone);
			}

			mTts.speak(lastRunString, TextToSpeech.QUEUE_FLUSH, null);

			// TODO: speak goal progress
			List<Entity> goals = goalDAO.getAll();

			for (Entity e : goals) {
				if (e instanceof Goal) {
					Goal g = (Goal) e;

					switch (g.getType()) {
						case RUNS:
							mTts.speak(getResources().getString(R.string.audio_goalRuns_1) 
									   + " " +  g.getProgress() + " " + 
									   getResources().getString(R.string.audio_goalRuns_2), TextToSpeech.QUEUE_ADD, null);
							break;
						
						case DISTANCE:
							mTts.speak(getResources().getString(R.string.audio_goalDistance_1) 
									   + " " +  g.getProgress() + " " + 
									   getResources().getString(R.string.audio_goalDistance_2), TextToSpeech.QUEUE_ADD, null);
							break;
							
						case CALORIES:
							mTts.speak(getResources().getString(R.string.audio_goalCalories_1) 
									   + " " +  g.getProgress() + " " + 
									   getResources().getString(R.string.audio_goalCalories_2), TextToSpeech.QUEUE_ADD, null);
							break;
					}
				}
			}
		}
	}
}
