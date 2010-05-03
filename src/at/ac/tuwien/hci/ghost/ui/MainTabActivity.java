package at.ac.tuwien.hci.ghost.ui;

import java.util.Locale;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Button;
import android.widget.TabHost;
import at.ac.tuwien.hci.ghost.R;
import at.ac.tuwien.hci.ghost.ui.goal.GoalsActivity;
import at.ac.tuwien.hci.ghost.ui.history.HistoryActivity;
import at.ac.tuwien.hci.ghost.ui.route.RoutesActivity;
import at.ac.tuwien.hci.ghost.ui.run.StartRunActivity;

public class MainTabActivity extends android.app.TabActivity implements OnInitListener {
	private static final String TAG = "TextToSpeechDemo";

	private TextToSpeech mTts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize text-to-speech. This is an asynchronous operation.
        // The OnInitListener (second argument) is called after initialization completes.
        mTts = new TextToSpeech(this,
            this  // TextToSpeech.OnInitListener
            );

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
	            // Note that a language may not be available, and the result will indicate this.
	            int result = mTts.setLanguage(Locale.US);
	            // Try this someday for some interesting results.
	            // int result mTts.setLanguage(Locale.FRANCE);
	            if (result == TextToSpeech.LANG_MISSING_DATA ||
	                result == TextToSpeech.LANG_NOT_SUPPORTED) {
	               // Lanuage data is missing or the language is not supported.
	                Log.e(TAG, "Language is not available.");
	            } else {
	                // Check the documentation for other possible result codes.
	                // For example, the language may be available for the locale,
	                // but not for the specified country and variant.

	                // The TTS engine has been successfully initialized.
	                // Allow the user to press the button for the app to speak again.
	              
	                // Greet the user.
	                sayHello();
	            }
	        } else {
	            // Initialization failed.
	            Log.e(TAG, "Could not initialize TextToSpeech.");
	        }
	    }

	    private static final Random RANDOM = new Random();
	    private static final String[] HELLOS = {
	      "Hello",
	      "Salutations",
	      "Greetings",
	      "Howdy",
	      "What's crack-a-lackin?",
	      "That explains the stench!" 
	    };

	    private void sayHello() {
	        // Select a random hello.
	        int helloLength = HELLOS.length;
	        String hello = HELLOS[RANDOM.nextInt(helloLength)];
	        mTts.speak(hello,
	            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
	            null);
	    }
}
