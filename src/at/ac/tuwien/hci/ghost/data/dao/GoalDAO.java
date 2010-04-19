package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;

public class GoalDAO implements DataAccessObject {
	
	private SQLiteDatabase ghostDB = null;
	private DBOpenHelper dbOpenHelper = null;
	private Context context = null;
	
	public GoalDAO(Context context)
	{
		this.context = context;
		try
		{
			dbOpenHelper = new DBOpenHelper(context);
		}
		catch(Exception e)
		{
			Log.e(GoalDAO.class.toString(),"Fuck1: " + e);
		}
	}
	
	private void open()
	{
		try
		{
			ghostDB = dbOpenHelper.getWritableDatabase();
		}
		catch(Exception e)
		{
			Log.e(GoalDAO.class.toString(),"Fuck2: " + e);
		}
	}
	
	private void close()
	{
		ghostDB.close();
	}

	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove Stub-Implementation
		List<Entity> goals = new ArrayList<Entity>();
		try
		{
			Cursor cursor = null;
			open();
			cursor = ghostDB.query("tb_ghost_test", new String[] {"name","value"}, null, null, null, null, null);
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					do
					{
						String name = cursor.getString(0);
						int value = cursor.getInt(1);
						goals.add(new Goal(4,Goal.Type.RUNS,30));
						System.out.println("Fetched name=" + name + ", value=" + value);
					}
					while(cursor.moveToNext());
				}
				cursor.close();
			}
			goals.add(new Goal(1,Goal.Type.DISTANCE, 50.f, 0.4f));
			goals.add(new Goal(2,Goal.Type.CALORIES, 2000.f));
			goals.add(new Goal(3,Goal.Type.RUNS, 20));
			close();
		}
		catch(Exception e)
		{
			Log.e(GoalDAO.class.toString(),"Fuck3: " + e);
		}
		return goals;
	}
	
	public void insert(int value1, int value2)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put("name", "ma wurscht");
		initialValues.put("value", 2);
		open();
		ghostDB.insert("tb_ghost_test", null, initialValues);
		Log.e(GoalDAO.class.toString(),"Fuck the world");
		close();
	}

}
