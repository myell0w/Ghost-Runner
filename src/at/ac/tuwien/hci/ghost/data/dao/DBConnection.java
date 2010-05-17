package at.ac.tuwien.hci.ghost.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBConnection {
	
	public static SQLiteDatabase ghostDB = null;
	
	public static boolean open(Context context)
	{
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		try
		{
			if(dbOpenHelper != null)
				ghostDB = dbOpenHelper.getWritableDatabase();
		}
		catch(Exception e)
		{
			Log.e(DBConnection.class.toString(), "Error while opening database");
			return false;
		}
		return true;
	}
	
	public static void close()
	{
		if(ghostDB != null)
		{
			ghostDB.close();
			ghostDB = null;
		}
	}
}
