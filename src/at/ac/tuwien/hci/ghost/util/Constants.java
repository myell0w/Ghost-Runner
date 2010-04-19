package at.ac.tuwien.hci.ghost.util;

public interface Constants {
	public static final String ROUTE = "at.ac.tuwien.hci.ghost.Route";
	public static final String RUN = "at.ac.tuwien.hci.ghost.Run";
	
	/** database constants */
	public static final String DB_TABLE_GOALS = "Goals";
	public static final String DB_GOALS_COLUMN_ID = "id";
	public static final String DB_GOALS_COLUMN_PROGRESS = "progress";
	public static final String DB_GOALS_COLUMN_GOALVALUE = "goalvalue";
	public static final String DB_GOALS_COLUMN_PERIOD = "period";
	public static final String DB_GOALS_COLUMN_TYPE = "type";
	public static final String DB_GOALS_COLUMN_TYPE_ID = "INTEGER PRIMARY KEY";
	public static final String DB_GOALS_COLUMN_TYPE_PROGRESS = "REAL";
	public static final String DB_GOALS_COLUMN_TYPE_GOALVALUE = "REAL";
	public static final String DB_GOALS_COLUMN_TYPE_PERIOD = "INTEGER";
	public static final String DB_GOALS_COLUMN_TYPE_TYPE = "INTEGER";
	
	/** menu constans */
	public static final int MENU_SWITCH_VIEW = 101;
	public static final int MENU_SETTINGS = 102;
	public static final int MENU_WEATHER = 103;
	public static final int MENU_NEW_GOAL = 104;
}
