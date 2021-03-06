package at.ac.tuwien.hci.ghost.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "GHOST_DB";
	private static final int DATABASE_VERSION = 1;

	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTableGoals = null;
		String createTableRoutes = null;
		String createTableRuns = null;
		String createTableWaypoints = null;

		createTableGoals = "CREATE TABLE IF NOT EXISTS " + Constants.DB_TABLE_GOALS + " (" + Constants.DB_GOALS_COLUMN_ID + " "
				+ Constants.DB_GOALS_COLUMN_TYPE_ID + ", " + Constants.DB_GOALS_COLUMN_PROGRESS + " " + Constants.DB_GOALS_COLUMN_TYPE_PROGRESS + ", "
				+ Constants.DB_GOALS_COLUMN_TYPE + " " + Constants.DB_GOALS_COLUMN_TYPE_TYPE + ", " + Constants.DB_GOALS_COLUMN_GOALVALUE + " "
				+ Constants.DB_GOALS_COLUMN_TYPE_GOALVALUE + ", " + Constants.DB_GOALS_COLUMN_PERIOD + " " + Constants.DB_GOALS_COLUMN_TYPE_PERIOD + ");";
		createTableRoutes = "CREATE TABLE IF NOT EXISTS " + Constants.DB_TABLE_ROUTES + " (" + Constants.DB_ROUTES_COLUMN_ID + " "
				+ Constants.DB_ROUTES_COLUMN_TYPE_ID + ", " + Constants.DB_ROUTES_COLUMN_DISTANCE + " " + Constants.DB_ROUTES_COLUMN_TYPE_DISTANCE + ", "
				+ Constants.DB_ROUTES_COLUMN_NAME + " " + Constants.DB_ROUTES_COLUMN_TYPE_NAME + ", " + Constants.DB_ROUTES_COLUMN_RUNCOUNT + " "
				+ Constants.DB_ROUTES_COLUMN_TYPE_RUNCOUNT + ");";
		createTableRuns = "CREATE TABLE IF NOT EXISTS " + Constants.DB_TABLE_RUNS + " (" +
							Constants.DB_RUNS_COLUMN_ID + " " + Constants.DB_RUNS_COLUMN_TYPE_ID + ", " + 
							Constants.DB_RUNS_COLUMN_DATE + " " + Constants.DB_RUNS_COLUMN_TYPE_DATE + ", " + 
							Constants.DB_RUNS_COLUMN_TIMEINSECONDS + " " + Constants.DB_RUNS_COLUMN_TYPE_TIMEINSECONDS + ", " + 
							Constants.DB_RUNS_COLUMN_DISTANCE + " " + Constants.DB_RUNS_COLUMN_TYPE_DISTANCE + ", " + 
							Constants.DB_RUNS_COLUMN_PACE + " " + Constants.DB_RUNS_COLUMN_TYPE_PACE + ", " + 
							Constants.DB_RUNS_COLUMN_SPEED + " " + Constants.DB_RUNS_COLUMN_TYPE_SPEED + ", " + 
							Constants.DB_RUNS_COLUMN_CALORIES + " " + Constants.DB_RUNS_COLUMN_TYPE_CALORIES + ", " + 
							Constants.DB_RUNS_COLUMN_ROUTEID + " " + Constants.DB_RUNS_COLUMN_TYPE_ROUTEID + ");";
		createTableWaypoints = "CREATE TABLE IF NOT EXISTS " + Constants.DB_TABLE_WAYPOINTS + " (" +
							Constants.DB_WAYPOINTS_COLUMN_ID + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_ID + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_TIME + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_TIME + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_SPEED + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_SPEED + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_LATITUDE + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_LATITUDE + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_LONGITUDE + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_LONGITUDE + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_ALTITUDE + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_ALTITUDE + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_ROUTEID + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_ROUTEID + ", " + 
							Constants.DB_WAYPOINTS_COLUMN_RUNID + " " + Constants.DB_WAYPOINTS_COLUMN_TYPE_RUNID  + ");";
		

		Log.i(DBOpenHelper.class.toString(), createTableGoals);
		db.execSQL(createTableGoals);
		db.execSQL(createTableRoutes);
		db.execSQL(createTableRuns);
		db.execSQL(createTableWaypoints);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(DBOpenHelper.class.toString(), "Upgrading database from version " + oldVersion + " to version " + newVersion + "."
				+ "Data tables will be truncated.");
		db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_GOALS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_ROUTES + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_RUNS + ";");
		db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_WAYPOINTS + ";");
		onCreate(db);
	}

}
