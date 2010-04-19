package at.ac.tuwien.hci.ghost.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "GHOST_DB";
	private static final int DATABASE_VERSION = 1;

	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Add create table statements
		db.execSQL("CREATE TABLE IF NOT EXISTS tb_ghost_test (name text, value integer);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Delete all tables
		Log.i(DBOpenHelper.class.toString(),"Upgrading database from version " + oldVersion + " to version " + newVersion + "." +
		         "Data tables will be truncated.");
		db.execSQL("DROP TABLE IF EXISTS tb_ghost_test;");
		onCreate(db);
	}

}
