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
import at.ac.tuwien.hci.ghost.data.entities.Goal.Period;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Type;
import at.ac.tuwien.hci.ghost.util.Constants;

public class GoalDAO extends DataAccessObject {

	public GoalDAO(Context context) {
		super(context);
	}
	
	public Entity search(long id)
	{
		Goal goal = null;
		Cursor cursor = null;
		cursor = ghostDB.query(true,
							Constants.DB_TABLE_GOALS,
							new String[] {Constants.DB_GOALS_COLUMN_ID,
							Constants.DB_GOALS_COLUMN_PERIOD,
							Constants.DB_GOALS_COLUMN_PROGRESS,
							Constants.DB_GOALS_COLUMN_TYPE,
							Constants.DB_GOALS_COLUMN_GOALVALUE},
							Constants.DB_GOALS_COLUMN_ID + "=" + id,
							null, null, null, null, null);
		if(cursor != null)
		{
			if(cursor.moveToFirst())
			{
				goal = new Goal(cursor.getInt(0));
				goal.setPeriod(Period.Int2Period(cursor.getInt(1)));
				goal.setProgress(cursor.getFloat(2));
				goal.setType(Type.Int2Type(cursor.getInt(3)));
				goal.setGoalValue(cursor.getFloat(4));
			}
		}
		return goal;
	}

	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove Stub-Implementation
		List<Entity> goals = new ArrayList<Entity>();
		try
		{
			Cursor cursor = null;
			cursor = ghostDB.query(Constants.DB_TABLE_GOALS,
					               new String[] {Constants.DB_GOALS_COLUMN_ID,
					                             Constants.DB_GOALS_COLUMN_PERIOD,
					                             Constants.DB_GOALS_COLUMN_PROGRESS,
					                             Constants.DB_GOALS_COLUMN_TYPE,
					                             Constants.DB_GOALS_COLUMN_GOALVALUE},
					               null, null, null, null, null);
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					do
					{
						Goal goal = new Goal(cursor.getInt(0));
						goal.setPeriod(Period.Int2Period(cursor.getInt(1)));
						goal.setProgress(cursor.getFloat(2));
						goal.setType(Type.Int2Type(cursor.getInt(3)));
						goal.setGoalValue(cursor.getFloat(4));

						goals.add(goal);
					}
					while(cursor.moveToNext());
				}
				cursor.close();
			}
			goals.add(new Goal(1,Goal.Type.DISTANCE, 50.f, 0.4f));
			goals.add(new Goal(2,Goal.Type.CALORIES, 2000.f));
			goals.add(new Goal(3,Goal.Type.RUNS, 20));
		}
		catch(Exception e)
		{
			Log.e(GoalDAO.class.toString(),"Fuck3: " + e);
		}
		return goals;
	}
	
	
	public long insert(Entity entity)
	{
		Goal goal = (Goal)entity;
		long result;
		ContentValues values = new ContentValues();
		if(goal.getPeriod() != null)
			values.put(Constants.DB_GOALS_COLUMN_PERIOD, goal.getPeriod().ordinal());
		values.put(Constants.DB_GOALS_COLUMN_PROGRESS, goal.getProgress());
		values.put(Constants.DB_GOALS_COLUMN_GOALVALUE, goal.getGoalValue());
		if(goal.getType() != null)
			values.put(Constants.DB_GOALS_COLUMN_TYPE, goal.getType().ordinal());
		result = ghostDB.insert(Constants.DB_TABLE_GOALS, null, values);
		Log.e(GoalDAO.class.toString(),"Inserted feckin' value in table Goals");
		return result;
	}
	
	public boolean delete(long id)
	{
		return ghostDB.delete(Constants.DB_TABLE_GOALS, Constants.DB_GOALS_COLUMN_ID + "=" + id , null) > 0;
	}
	

	public boolean update(Entity entity)
	{
		Goal goal = (Goal)entity;
		ContentValues values = new ContentValues();
		if(goal.getPeriod() != null)
			values.put(Constants.DB_GOALS_COLUMN_PERIOD, goal.getPeriod().ordinal());
		values.put(Constants.DB_GOALS_COLUMN_PROGRESS, goal.getProgress());
		values.put(Constants.DB_GOALS_COLUMN_GOALVALUE, goal.getGoalValue());
		if(goal.getType() != null)
			values.put(Constants.DB_GOALS_COLUMN_TYPE, goal.getType().ordinal());
		return ghostDB.update(Constants.DB_TABLE_GOALS, values, Constants.DB_GOALS_COLUMN_ID + "=" + goal.getID(), null) > 0;
	}

}
