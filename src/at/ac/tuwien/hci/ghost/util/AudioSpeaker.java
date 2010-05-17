package at.ac.tuwien.hci.ghost.util;

import java.io.Serializable;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

/**
 * Text-To-Speech-Wrapper
 * 
 * @author Matthias
 *
 */
public class AudioSpeaker implements OnInitListener, Serializable {
	private static final long serialVersionUID = -5049952095081885738L;
	
	private TextToSpeech tts;
	private InitListener initListener;
	private boolean initialized = false;
	
	public interface InitListener {
		public void initFinished();
	}
	
	public AudioSpeaker(Context context, InitListener initListener) {
		tts = new TextToSpeech(context, this);
		this.initListener = initListener;
	}
	
	public void shutdown() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
	}
	
	public void speak(String text, int queueMode) {
		tts.speak(text,queueMode, null);
	}
	
	public void speak(String text) {
		speak(text, TextToSpeech.QUEUE_ADD);
	}

	@Override
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Lanuage data is missing or the language is not supported.
				Log.e(getClass().getName(), "Language is not available.");
			} else {
				if (initListener != null)
					initListener.initFinished();
				
				initialized = true;
			}
		} else {
			// Initialization failed.
			Log.e(getClass().getName(), "Could not initialize TextToSpeech.");
		}	
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
