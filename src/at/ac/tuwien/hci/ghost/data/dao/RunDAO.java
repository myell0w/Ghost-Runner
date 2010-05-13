package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Run.Performance;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;

public class RunDAO extends DataAccessObject {

	private RouteDAO routeDAO = null; // used for getting the route of a run

	public RunDAO(Context context) {
		super(context);
		routeDAO = new RouteDAO(context);
	}

	/**
	 * Returns a spefific run identified by id. Not found or error: null
	 * 
	 * @param id
	 *            the id of the run
	 * @return the run or null if failed
	 */
	@Override
	public Entity search(long id) {
		Run run = null;

		List<Entity> runs = search(Constants.DB_RUNS_COLUMN_ID + "=" + id, null);
		if (runs != null && !runs.isEmpty())
			run = (Run) runs.get(0);

		return run;
	}

	/**
	 * Returns all stored runs. Not found: empty list Error: null
	 * 
	 * @return list of runs
	 */
	@Override
	public List<Entity> getAll() {
		List<Entity> runs = null;
		runs = search(null, null);

		/* TODO remove stub shit */
		if (runs == null)
			runs = new ArrayList<Entity>();
		runs.add(new Run(3, new Date(2, 3, 2010, 14, 20), 5600, 12.56f, 423, null, Performance.WORSE_THAN_AVERAGE));
		runs.add(new Run(4, new Date(3, 3, 2010, 17, 22), 7600, 23.56f, 523, null, Performance.AVERAGE));
		runs.add(new Run(5, new Date(4, 3, 2010, 12, 00), 1600, 2.56f, 623, null, Performance.BETTER_THAN_AVERAGE));

		return runs;
	}

	/**
	 * Returns all stored runs that meet the criteria <i>selection</i>, order by
	 * <i>orderBy</i> Not found: empty list Error: null
	 * 
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @return list of runs
	 */
	@Override
	protected List<Entity> search(String selection, String orderBy) {
		List<Entity> runs = new ArrayList<Entity>();

		// TODO gerdschi: das feld performance im Run befüllen (besser/durchschnitt/schlechter)
		try {
			Cursor cursor = null;
			cursor = ghostDB.query(Constants.DB_TABLE_RUNS, new String[] { Constants.DB_RUNS_COLUMN_ID, Constants.DB_RUNS_COLUMN_DATE,
					Constants.DB_RUNS_COLUMN_TIMEINSECONDS, Constants.DB_RUNS_COLUMN_DISTANCE, Constants.DB_RUNS_COLUMN_PACE, Constants.DB_RUNS_COLUMN_SPEED,
					Constants.DB_RUNS_COLUMN_CALORIES, Constants.DB_RUNS_COLUMN_ROUTEID }, selection, null, null, null, orderBy);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Run run = new Run(cursor.getLong(0));
						run.setDate(new Date(cursor.getLong(1)));
						run.setTimeInSeconds(cursor.getLong(2));
						run.setDistanceInKm(cursor.getFloat(3));
						run.setPace(cursor.getFloat(4));
						run.setSpeed(cursor.getFloat(5));
						run.setCalories(cursor.getInt(6));
						if (!cursor.isNull(7)) {
							run.setRoute((Route) routeDAO.search(cursor.getLong(7)));
						}
						else { // set route object to empty route
							run.setRoute(Route.getEmptyRoute());
						}

						runs.add(run);
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		} catch (Exception e) {
			runs = null;
			Log.e(RunDAO.class.toString(), "Error search(): " + e.toString());
		}
		return runs;
	}

	public List<Run> entitiesToRuns(List<Entity> entities) {
		List<Run> runs = new ArrayList<Run>();
		for (Entity e : entities) {
			if (e instanceof Run) {
				runs.add((Run) e);
			}
		}
		return runs;
	}

	public List<Run> getAllRunsOfRoute(Route route) {
		if (route != null)
			return entitiesToRuns(search(Constants.DB_RUNS_COLUMN_ROUTEID + "=" + route.getID(), null));
		return null;
	}

	public List<Run> getAllRunsInMonth(int month, int year) {
		Date dateStart = new Date(1, month, year, 0, 0);
		Date dateEnd = new Date(1, (month == 12 ? 1 : month + 1), (month == 12 ? year + 1 : year), 0, 0);
		long timeStart = dateStart.getAsJavaDefaultDate().getTime();
		long timeEnd = dateEnd.getAsJavaDefaultDate().getTime();
		return entitiesToRuns(search(Constants.DB_RUNS_COLUMN_DATE + " BETWEEN " + timeStart + " AND " + timeEnd, null));
	}
	
	public Run getLastCompletedRun() {
		// TODO gerdschi
		return null;
	}

	/**
	 * Inserts a specific run
	 * 
	 * @param entity
	 *            the run to insert
	 * @return the generated autoincrement id or -1 if failed
	 */
	@Override
	public long insert(Entity entity) {
		long result;
		
		// TODO gerdschi: das feld performance speichern (besser/durchschnitt/schlechter)
		
		try {
			Run run = (Run) entity;
			ContentValues values = new ContentValues();
			if (run.getDate() != null)
				values.put(Constants.DB_RUNS_COLUMN_DATE, run.getDate().getAsJavaDefaultDate().getTime());
			values.put(Constants.DB_RUNS_COLUMN_TIMEINSECONDS, run.getTime());
			values.put(Constants.DB_RUNS_COLUMN_DISTANCE, run.getDistanceInKm());
			values.put(Constants.DB_RUNS_COLUMN_PACE, run.getPace());
			values.put(Constants.DB_RUNS_COLUMN_SPEED, run.getSpeed());
			values.put(Constants.DB_RUNS_COLUMN_CALORIES, run.getCalories());
			if (run.getRoute() != null && !run.getRoute().equals(Route.getEmptyRoute())) // do not insert empty route
				values.put(Constants.DB_RUNS_COLUMN_ROUTEID, run.getRoute().getID());
			result = ghostDB.insert(Constants.DB_TABLE_RUNS, null, values);
		} catch (Exception e) {
			Log.e(RunDAO.class.toString(), "Error insert(): " + e.toString());
			result = -1;
		}
		return result;
	}

	/**
	 * Deletes a spefific run identified by id. Not found or error: false
	 * 
	 * @param id
	 *            the id of the run
	 * @return true, false if failed
	 */
	@Override
	public boolean delete(long id) {
		boolean result = false;
		try {
			result = ghostDB.delete(Constants.DB_TABLE_RUNS, Constants.DB_RUNS_COLUMN_ID + "=" + id, null) > 0;
		} catch (Exception e) {
			Log.e(RunDAO.class.toString(), "Error delete(): " + e.toString());
			result = false;
		}
		return result;
	}
	
	public boolean deleteAll() {
		boolean result = false;
		try {
			result = ghostDB.delete(Constants.DB_TABLE_RUNS, "1=1", null) > 0;
			System.out.println("ERROR'S HERE");
		} catch(Exception e) {
			System.out.println("ERROR: " + e.toString());
			Log.e(RunDAO.class.toString(), "Error deleteAll(): " + e.toString());
			result = false;
		}
		return result;
	}

	/**
	 * Updates a spefific run identified by id of entity with the data stored in
	 * entity. Not found or error: false
	 * 
	 * @param entity
	 *            the entity to update
	 * @return true, false if failed
	 */
	@Override
	public boolean update(Entity entity) {
		boolean result = false;
		
		// TODO gerdschi: das feld performance updaten (besser/durchschnitt/schlechter)
		
		try {
			Run run = (Run) entity;
			ContentValues values = new ContentValues();
			if (run.getDate() != null)
				values.put(Constants.DB_RUNS_COLUMN_DATE, run.getDate().getAsJavaDefaultDate().getTime());
			values.put(Constants.DB_RUNS_COLUMN_TIMEINSECONDS, run.getTime());
			values.put(Constants.DB_RUNS_COLUMN_DISTANCE, run.getDistanceInKm());
			values.put(Constants.DB_RUNS_COLUMN_PACE, run.getPace());
			values.put(Constants.DB_RUNS_COLUMN_SPEED, run.getSpeed());
			values.put(Constants.DB_RUNS_COLUMN_CALORIES, run.getCalories());
			if (run.getRoute() != null && !run.getRoute().equals(Route.getEmptyRoute())) // do not update empty route
				values.put(Constants.DB_RUNS_COLUMN_ROUTEID, run.getRoute().getID());
			result = ghostDB.update(Constants.DB_TABLE_RUNS, values, Constants.DB_RUNS_COLUMN_ID + "=" + run.getID(), null) > 0;
		} catch (Exception e) {
			Log.e(RunDAO.class.toString(), "Error update(): " + e.toString());
			result = false;
		}
		return result;
	}
}
