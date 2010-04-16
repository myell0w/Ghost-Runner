package at.ac.tuwien.hci.ghost.data.entities;

public class Goal {
	public enum Period { WEEK, MONTH, YEAR };
	public enum Type { DISTANCE, CALORIES, RUNS };
	
	/** the progress of the goal, between 0 and 1 */
	private float progress = 0.f;
	/** the value of the goal */
	private float goal = 0.f;
	/** the period of the goal */
	private Period period = Period.MONTH;
	/** the type of the goal */
	private Type type = null;
	
	public Goal(Type type, float goal) {
		this.type = type;
		this.goal = goal;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public float getGoal() {
		return goal;
	}

	public void setGoal(float goal) {
		this.goal = goal;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public float getProgress() {
		return progress;
	}
	
	public float getProgressFraction() {
		return progress * goal;
	}
	
	public int getProgressPercentage() {
		return Math.round(progress * 100);
	}
	
	public boolean finished() {
		return progress >= 1.f;
	}
}
