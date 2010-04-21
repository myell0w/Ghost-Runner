package at.ac.tuwien.hci.ghost.data.entities;

import at.ac.tuwien.hci.ghost.observer.Observer;


public class RunStatistics implements Observer<Waypoint> {
	private double averageSpeed = 0.;
	private double averagePace = 0.;
	private double distance = 0;
	private double calories = 0;
	private RunTime time = new RunTime(0);
	private int numlocations = 0;
	private Waypoint lastlocation;

	
	public double getDistance() {
		return distance;
	}

	public RunTime getTime() {
		return time;
	}
	
	public long getDurationInSeconds() {
		return time.getDurationInSeconds();
	}

	public double getAverageSpeed() {
		return averageSpeed;
	}
	
	public double getAveragePace() {
		return averagePace;
	}

	public double getCalories() {
		return calories;
	}

	@Override
	public void notify(Waypoint p) {
		if (numlocations > 0) {
			distance += p.distanceTo(lastlocation);
			
			averageSpeed = (distance * 1000.) / (getDurationInSeconds() / 3600.0);
			averagePace = (getDurationInSeconds() / 60.) / (distance * 1000.);
			
			// TODO: make settings and read out settings
			calculateCalories(75,180,30,getDurationInSeconds()/3600.);
		}
		
		numlocations++;
		lastlocation = p;
	}

	private void calculateCalories(int weightInKg, int sizeInCm, int ageInYears, double durationInHours) {
		calories = (66.47 + (13.7 * weightInKg) + (5 * sizeInCm) - (6.8 * ageInYears)) * (durationInHours / 24.) * 7.5;
	}
}
