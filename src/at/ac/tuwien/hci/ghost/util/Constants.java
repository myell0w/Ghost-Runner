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

	public static final String DB_TABLE_ROUTES = "Routes";
	public static final String DB_ROUTES_COLUMN_ID = "id";
	public static final String DB_ROUTES_COLUMN_NAME = "name";
	public static final String DB_ROUTES_COLUMN_DISTANCE = "distance";
	public static final String DB_ROUTES_COLUMN_RUNCOUNT = "runcount";
	public static final String DB_ROUTES_COLUMN_TYPE_ID = "INTEGER PRIMARY KEY";
	public static final String DB_ROUTES_COLUMN_TYPE_NAME = "TEXT";
	public static final String DB_ROUTES_COLUMN_TYPE_DISTANCE = "REAL";
	public static final String DB_ROUTES_COLUMN_TYPE_RUNCOUNT = "INTEGER";

	public static final String DB_TABLE_RUNS = "Runs";
	public static final String DB_RUNS_COLUMN_ID = "id";
	public static final String DB_RUNS_COLUMN_DATE = "date";
	public static final String DB_RUNS_COLUMN_TIMEINSECONDS = "timeinseconds";
	public static final String DB_RUNS_COLUMN_DISTANCE = "distance";
	public static final String DB_RUNS_COLUMN_PACE = "pace";
	public static final String DB_RUNS_COLUMN_SPEED = "speed";
	public static final String DB_RUNS_COLUMN_CALORIES = "calories";
	public static final String DB_RUNS_COLUMN_ROUTEID = "routeid";
	public static final String DB_RUNS_COLUMN_TYPE_ID = "INTEGER PRIMARY KEY";
	public static final String DB_RUNS_COLUMN_TYPE_DATE = "INTEGER";
	public static final String DB_RUNS_COLUMN_TYPE_TIMEINSECONDS = "INTEGER";
	public static final String DB_RUNS_COLUMN_TYPE_DISTANCE = "REAL";
	public static final String DB_RUNS_COLUMN_TYPE_PACE = "REAL";
	public static final String DB_RUNS_COLUMN_TYPE_SPEED = "REAL";
	public static final String DB_RUNS_COLUMN_TYPE_CALORIES = "INTEGER";
	public static final String DB_RUNS_COLUMN_TYPE_ROUTEID = "INTEGER";
	
	public static final String DB_TABLE_WAYPOINTS = "Waypoints";
	public static final String DB_WAYPOINTS_COLUMN_ID = "id";
	public static final String DB_WAYPOINTS_COLUMN_TIME = "time";
	public static final String DB_WAYPOINTS_COLUMN_SPEED = "speed";
	public static final String DB_WAYPOINTS_COLUMN_LATITUDE = "latitude";
	public static final String DB_WAYPOINTS_COLUMN_LONGITUDE = "longitude";
	public static final String DB_WAYPOINTS_COLUMN_ALTITUDE = "altitude";
	public static final String DB_WAYPOINTS_COLUMN_ROUTEID = "routeid";
	public static final String DB_WAYPOINTS_COLUMN_RUNID = "runid";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_ID = "INTEGER PRIMARY KEY";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_TIME = "INTEGER";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_SPEED = "REAL";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_LATITUDE = "REAL";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_LONGITUDE = "REAL";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_ALTITUDE = "REAL";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_ROUTEID = "INTEGER";
	public static final String DB_WAYPOINTS_COLUMN_TYPE_RUNID = "INTEGER";

	/** menu constants */
	public static final int MENU_SWITCH_VIEW = 101;
	public static final int MENU_SETTINGS = 102;
	public static final int MENU_WEATHER = 103;
	public static final int MENU_NEW_GOAL = 104;
	
	/** Map Zoom Level */
	public static final int DEFAULT_ZOOM_LEVEL = 15;
	
	/** GPS Accuracy Levels */
	public static final float GPS_ACCURACY_BAD = 40;
	public static final float GPS_ACCURACY_MEDIUM = 20;
}
