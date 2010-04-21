package at.ac.tuwien.hci.ghost.data.entities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import at.ac.tuwien.hci.ghost.observer.Observer;

public class RunStatistics implements Observer<Waypoint> {
	private Context context = null;
	private float averageSpeed = 0.f;
	private float averagePace = 0.f;
	private float distance = 0;
	private float calories = 0;
	private RunTime time = new RunTime(0);
	private int numlocations = 0;
	private Waypoint lastlocation;

	public RunStatistics(Context context) {
		this.context = context;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public float getDistanceInKm() {
		return distance / 1000.f;
	}

	public RunTime getTime() {
		return time;
	}

	public long getDurationInSeconds() {
		return time.getDurationInSeconds();
	}

	public float getAverageSpeed() {
		return averageSpeed;
	}

	public float getAveragePace() {
		return averagePace;
	}

	public float getCalories() {
		return calories;
	}

	@Override
	public void notify(Waypoint p) {
		if (numlocations > 0) {
			distance += p.distanceTo(lastlocation);
			
			averageSpeed = (distance * 1000.f) / (getDurationInSeconds() / 3600.f);
			averagePace = (getDurationInSeconds() / 60.f) / (distance * 1000.f);

			// get the xml/preferences.xml preferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			
			// TODO: make settings and read out settings
			calculateCalories(prefs.getInt("weight",75),prefs.getInt("size", 180),prefs.getInt("age", 30),getDurationInSeconds()/3600.);

		}
	}

	private void calculateCalories(int weightInKg, int sizeInCm, int ageInYears, double durationInHours) {
		calories = (float) ((66.47 + (13.7 * weightInKg) + (5 * sizeInCm) - (6.8 * ageInYears)) * (durationInHours / 24.) * 7.5);
	}
}
