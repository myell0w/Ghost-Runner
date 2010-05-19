package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Period;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Type;
import at.ac.tuwien.hci.ghost.util.Constants;

public class GoalDAO extends DataAccessObject {

	public GoalDAO() {
		super();
	}

	/**
	 * Returns a spefific goal identified by id. Not found or error: null
	 * 
	 * @param id
	 *            the id of the goal
	 * @return the goal or null if failed
	 */
	@Override
	public Entity search(long id) {
		Goal goal = null;

		List<Entity> goals = search(Constants.DB_GOALS_COLUMN_ID + "=" + id, null);
		if (goals != null && !goals.isEmpty())
			goal = (Goal) goals.get(0);

		return goal;
	}

	/**
	 * Returns all stored goals. Not found: empty list Error: null
	 * 
	 * @return list of goals
	 */
	@Override
	public List<Entity> getAll() {
		return search(null, null);
	}

	/**
	 * Returns all stored goals that meet the criteria <i>selection</i>, order
	 * by <i>orderBy</i> Not found: empty list Error: null
	 * 
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @return list of goals
	 */
	@Override
	protected List<Entity> search(String selection, String orderBy) {
		List<Entity> goals = new ArrayList<Entity>();
		Cursor cursor = null;
		try {
			cursor = DBConnection.ghostDB.query(Constants.DB_TABLE_GOALS, new String[] { Constants.DB_GOALS_COLUMN_ID, Constants.DB_GOALS_COLUMN_PERIOD,
					Constants.DB_GOALS_COLUMN_PROGRESS, Constants.DB_GOALS_COLUMN_TYPE, Constants.DB_GOALS_COLUMN_GOALVALUE }, selection, null, null, null,
					orderBy);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Goal goal = new Goal(cursor.getLong(0));
						goal.setPeriod(Period.Int2Period(cursor.getInt(1)));
						goal.setProgress(cursor.getFloat(2));
						goal.setType(Type.Int2Type(cursor.getInt(3)));
						goal.setGoalValue(cursor.getFloat(4));

						goals.add(goal);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception e) {
			goals = null;
			Log.e(GoalDAO.class.toString(), "Error search(): " + e.toString());
		} finally {
			if(cursor != null)
				cursor.close();
		}
		return goals;
	}

	/**
	 * Inserts a specific goal
	 * 
	 * @param entity
	 *            the goal to insert
	 * @return the generated autoincrement id or -1 if failed
	 */
	@Override
	public long insert(Entity entity) {
		long result;
		try {
			Goal goal = (Goal) entity;
			ContentValues values = new ContentValues();
			if (goal.getPeriod() != null)
				values.put(Constants.DB_GOALS_COLUMN_PERIOD, goal.getPeriod().ordinal());
			values.put(Constants.DB_GOALS_COLUMN_PROGRESS, goal.getProgress());
			values.put(Constants.DB_GOALS_COLUMN_GOALVALUE, goal.getGoalValue());
			if (goal.getType() != null)
				values.put(Constants.DB_GOALS_COLUMN_TYPE, goal.getType().ordinal());
			result = DBConnection.ghostDB.insert(Constants.DB_TABLE_GOALS, null, values);
		} catch (Exception e) {
			Log.e(GoalDAO.class.toString(), "Error insert(): " + e.toString());
			result = -1;
		}
		return result;
	}

	/**
	 * Deletes a spefific goal identified by id. Not found or error: false
	 * 
	 * @param id
	 *            the id of the goal
	 * @return true, false if failed
	 */
	@Override
	public boolean delete(long id) {
		boolean result = false;
		try {
			result = DBConnection.ghostDB.delete(Constants.DB_TABLE_GOALS, Constants.DB_GOALS_COLUMN_ID + "=" + id, null) > 0;
		} catch (Exception e) {
			Log.e(GoalDAO.class.toString(), "Error delete(): " + e.toString());
			result = false;
		}
		return result;
	}

	/**
	 * Updates a spefific goal identified by id of entity with the data stored
	 * in entity. Not found or error: false
	 * 
	 * @param entity
	 *            the entity to update
	 * @return true, false if failed
	 */
	@Override
	public boolean update(Entity entity) {
		boolean result = false;
		try {
			Goal goal = (Goal) entity;
			System.out.println("update:");
			goal.print();
			ContentValues values = new ContentValues();
			if (goal.getPeriod() != null)
				values.put(Constants.DB_GOALS_COLUMN_PERIOD, goal.getPeriod().ordinal());
			values.put(Constants.DB_GOALS_COLUMN_PROGRESS, goal.getProgress());
			values.put(Constants.DB_GOALS_COLUMN_GOALVALUE, goal.getGoalValue());
			if (goal.getType() != null)
				values.put(Constants.DB_GOALS_COLUMN_TYPE, goal.getType().ordinal());
			result = DBConnection.ghostDB.update(Constants.DB_TABLE_GOALS, values, Constants.DB_GOALS_COLUMN_ID + "=" + goal.getID(), null) > 0;
		} catch (Exception e) {
			Log.e(GoalDAO.class.toString(), "Error update(): " + e.toString());
			result = false;
		}
		return result;
	}

}
