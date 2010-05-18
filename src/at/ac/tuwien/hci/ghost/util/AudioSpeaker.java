package at.ac.tuwien.hci.ghost.util;

import java.io.Serializable;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

/**
 * Text-To-Speech-Wrapper
 * 
 * @author Matthias
 *
 */
public class AudioSpeaker implements OnInitListener, Serializable, OnUtteranceCompletedListener {
	private static final long serialVersionUID = -5049952095081885738L;
	
	private static AudioSpeaker instance = null;
	
	private Context context;
	private TextToSpeech tts;
	private InitListener initListener;
	private SpeakingListener speakingListener;
	private boolean initialized = false;
	
	// Interfaces
	
	public interface InitListener {
		public void initFinished();
	}
	
	public interface SpeakingListener {
		public void speakingFinished();
	}
	
	// Singleton
	
	public static void createInstance(Context context, InitListener initListener, SpeakingListener speakingListener) {
		instance = new AudioSpeaker(context,initListener,speakingListener);
	}
	
	public static AudioSpeaker getInstance() {
		return instance;
	}
	
	private AudioSpeaker(Context context, InitListener initListener, SpeakingListener speakingListener) {
		tts = new TextToSpeech(context, this);
		
		tts.setOnUtteranceCompletedListener(this);
		
		this.context = context;
		this.initListener = initListener;
		this.speakingListener = speakingListener;
	}
	
	// normal methods
	
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
	
	public void speak(int resId, int queueMode) {
		tts.speak(context.getResources().getString(resId), queueMode, null);
	}
	
	public void speak(int resId) {
		speak(resId, TextToSpeech.QUEUE_ADD);
	}
	
	public boolean isSpeaking() {
		return tts.isSpeaking();
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

	@Override
	public void onUtteranceCompleted(String arg0) {
		if (speakingListener != null)
			speakingListener.speakingFinished();
	}

	public void setInitListener(InitListener initListener) {
		this.initListener = initListener;
	}

	public void setSpeakingListener(SpeakingListener speakingListener) {
		this.speakingListener = speakingListener;
	}
}
