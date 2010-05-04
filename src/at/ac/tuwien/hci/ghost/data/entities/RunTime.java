package at.ac.tuwien.hci.ghost.data.entities;

public class RunTime {
	/** Initial duration without pause or something */
	private long duration;
	/** Local Timestamp when started in ms */
	private long startingTime;
	/** Indicates if the timer is started */
	private boolean isStarted = false;

	public RunTime(long startDurationMs) {
		this.duration = startDurationMs;
		startingTime = System.currentTimeMillis();
	}

	/** Gets the complete elapsed time in ms */
	public long getDurationInMilliSeconds() {
		if (isStarted)
			return duration + (System.currentTimeMillis() - startingTime);
		else
			return duration;
	}
	
	public long getDurationInSeconds() {
		return getDurationInMilliSeconds() / 1000;
	}

	public double getDisplayHours() {
		return getDurationInMilliSeconds() / 1000.0 / 60.0 / 60.0;
	}

	public double getDisplayMinutes() {
		double hours = getDisplayHours();

		return ((hours - (int) hours)) * 60;
	}

	public double getDisplaySeconds() {
		double minutes = getDisplayMinutes();

		return ((minutes - (int) minutes)) * 60;
	}

	/** Pauses the timer */
	public void pause() {
		duration = getDurationInMilliSeconds();
		isStarted = false;
	}

	public void start(long predefinedDuration) {
		duration = predefinedDuration;
		startingTime = System.currentTimeMillis();
		isStarted = true;
	}

	public void start() {
		start(duration);
	}

	public boolean isPaused() {
		return !isStarted;
	}

}
