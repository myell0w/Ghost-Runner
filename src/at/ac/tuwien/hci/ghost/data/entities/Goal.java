package at.ac.tuwien.hci.ghost.data.entities;

public class Goal extends Entity {
	public enum Period { WEEK, MONTH, YEAR };
	public enum Type { DISTANCE, CALORIES, RUNS };
	
	/** the id of the goal */
	private int id = 0;
	/** the progress of the goal, between 0 and 1 */
	private float progress = 0.f;
	/** the value of the goal */
	private float goal = 0.f;
	/** the period of the goal */
	private Period period = Period.MONTH;
	/** the type of the goal */
	private Type type = null;
	
	public Goal(int id, Type type, float goal, float progress) {
		this.id = id;
		this.type = type;
		this.goal = goal;
		this.progress = progress;
	}
	
	public Goal(int id, Type type, float goal) {
		this(id,type,goal,0.f);
	}
	
	public Goal(int id) {
		this.id = id;
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

	@Override
	public int getID() {
		return id;
	}
}
