package at.ac.tuwien.hci.ghost.data.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import at.ac.tuwien.hci.ghost.R;
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
		if (!time.isPaused()) {
			if (numlocations > 0) {
				distance += p.distanceTo(lastlocation);

				averageSpeed = getDistanceInKm() / (getDurationInSeconds() / 3600.f);

				averagePace = (getDurationInSeconds() / 60.f) / getDistanceInKm();

				// get the xml/preferences.xml preferences
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

				String sWeight = prefs.getString("weight", "75");
				String sSex = prefs.getString("sex", "Male");
				String sSize = prefs.getString("size", "180");
				String sAge = prefs.getString("age", "30");

				boolean male = sSex.equals("Male");
				int weight;
				int size;
				int age;

				try {
					weight = Integer.parseInt(sWeight);
				} catch (Exception ex) {
					weight = 75;
				}
				try {
					size = Integer.parseInt(sSize);
				} catch (Exception ex) {
					size = 180;
				}
				try {
					age = Integer.parseInt(sAge);
				} catch (Exception ex) {
					age = 30;
				}

				calculateCalories(male, weight, size, age, getDurationInSeconds() / 3600.);
			}
		}

		numlocations++;
		lastlocation = p;
	}

	private void calculateCalories(boolean male, int weightInKg, int sizeInCm, int ageInYears, double durationInHours) {
		if (male) {
			calories = (float) ((66.47 + (13.7 * weightInKg) + (5 * sizeInCm) - (6.8 * ageInYears)) * (durationInHours / 24.) * 7.5);
		} else {
			calories = (float) ((65.51 + (9.6 * weightInKg) + (1.8 * sizeInCm) - (4.7 * ageInYears)) * (durationInHours / 24.) * 7.5);
		}
	}
}
