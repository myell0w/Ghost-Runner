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

	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove Stub-Implementation
		List<Entity> goals = new ArrayList<Entity>();
		try
		{
			Cursor cursor = null;
			open();
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
			close();
		}
		catch(Exception e)
		{
			Log.e(GoalDAO.class.toString(),"Fuck3: " + e);
		}
		return goals;
	}
	
	public void insert(Goal goal)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(Constants.DB_GOALS_COLUMN_PERIOD, goal.getPeriod().ordinal());
		initialValues.put(Constants.DB_GOALS_COLUMN_PROGRESS, goal.getProgress());
		initialValues.put(Constants.DB_GOALS_COLUMN_GOALVALUE, goal.getGoalValue());
		initialValues.put(Constants.DB_GOALS_COLUMN_TYPE, goal.getType().ordinal());
		open();
		ghostDB.insert(Constants.DB_TABLE_GOALS, null, initialValues);
		Log.e(GoalDAO.class.toString(),"Inserted feckin' value in table Goals");
		close();
	}

}
