package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
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
	private WaypointDAO waypointDAO = null; // used for getting the waypoints of a run

	public RunDAO() {
		super();
		routeDAO = new RouteDAO();
		waypointDAO = new WaypointDAO();
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

		if (runs == null)
			runs = new ArrayList<Entity>();

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
		Cursor cursor = null;

		try {
			cursor = DBConnection.ghostDB.query(Constants.DB_TABLE_RUNS, new String[] { Constants.DB_RUNS_COLUMN_ID, Constants.DB_RUNS_COLUMN_DATE,
					Constants.DB_RUNS_COLUMN_TIMEINSECONDS, Constants.DB_RUNS_COLUMN_DISTANCE, Constants.DB_RUNS_COLUMN_PACE, Constants.DB_RUNS_COLUMN_SPEED,
					Constants.DB_RUNS_COLUMN_CALORIES, Constants.DB_RUNS_COLUMN_ROUTEID }, selection, null, null, null, orderBy);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Run run = new Run(cursor.getLong(0));
						System.out.println("CATCHING RUN " + run.getID());
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
						
						// set performance indicator of this run
						run.setPerformance(calculatePerformance(run));

						runs.add(run);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception e) {
			runs = null;
			Log.e(RunDAO.class.toString(), "Error search(): " + e.toString());
		} finally {
			if(cursor != null)
			{
				System.out.println("CLOSING CURSOR RUNS");
				cursor.close();
				cursor = null;
			}
		}
		// now add waypoints
		/*if(runs != null) {
			for(int i=0;i<runs.size();i++)
			{
				// set waypoints of this run
				List<Waypoint> waypoints = waypointDAO.getAllWaypointsOfRun(runs.get(i).getID());
				if(waypoints != null && !waypoints.isEmpty())
					System.out.println("**** RUN " + runs.get(i).getID() + " HAS " + waypoints.size() + " WAYPOINTS");
				else
					System.out.println("**** RUN " + runs.get(i).getID() + " HAS NO WAYPOINTS");
				((Run)runs.get(i)).setWaypoints(waypoints);
			}
		}*/
		return runs;
	}

	/**
	 * Converts a list of entities to a list of Runs
	 * 
	 * @param entities
	 *            The list
	 * @return list of runs
	 */
	private List<Run> entitiesToRuns(List<Entity> entities) {
		List<Run> runs = new ArrayList<Run>();
		
		if(entities == null)
			return runs;
		
		for (Entity e : entities) {
			if (e instanceof Run) {
				runs.add((Run) e);
			}
		}
		
		return runs;
	}

	/**
	 * Returns all runs of a route
	 * 
	 * @param route
	 *            the route
	 * @return list of runs of the given <i>route</i>
	 */
	public List<Run> getAllRunsOfRoute(Route route) {
		if (route != null)
			return entitiesToRuns(search(Constants.DB_RUNS_COLUMN_ROUTEID + "=" + route.getID(), null));
		return null;
	}

	/**
	 * Returns all runs of one month from (including) first to last day of month
	 * 
	 * @param month
	 *            the month from 1 to 12
	 * @param year
	 * 			  the year
	 * @return list of runs
	 */
	public List<Run> getAllRunsInMonth(int month, int year) {
		Date dateStart = new Date(1, month, year, 0, 0);
		Date dateEnd = new Date(1, (month == 12 ? 1 : month + 1), (month == 12 ? year + 1 : year), 0, 0);
		long timeStart = dateStart.getAsJavaDefaultDate().getTime();
		long timeEnd = dateEnd.getAsJavaDefaultDate().getTime();
		List<Run> runs = entitiesToRuns(search(Constants.DB_RUNS_COLUMN_DATE + " BETWEEN " + timeStart + " AND " + timeEnd, null));
		Log.e(getClass().getName(),"******************** Runsize: " + runs.size() + " " + month + "." + year);
		return runs;
	}
	
	/**
	 * Returns the last completed run, i.e. the run with max date
	 * 
	 * @return the last completed run
	 */
	public Run getLastCompletedRun() {
		Run run = null;

		List<Entity> runs = search(Constants.DB_RUNS_COLUMN_DATE + " = (SELECT MAX(" + Constants.DB_RUNS_COLUMN_DATE + ") FROM " + Constants.DB_TABLE_RUNS + ")", null);
		if (runs != null && !runs.isEmpty())
			run = (Run) runs.get(0);

		return run;
	}
	
	/**
	 * Returns the best completed run for a route
	 * 
	 * @param route tha route
	 * @return the last completed run
	 */
	public Run getBestCompletedRun(Route route) {
		Run run = null;

		List<Entity> runs = search(Constants.DB_RUNS_COLUMN_PACE + " = (SELECT MIN(" + Constants.DB_RUNS_COLUMN_PACE + ") FROM " + Constants.DB_TABLE_RUNS + " WHERE " + Constants.DB_RUNS_COLUMN_ROUTEID + "=" + route.getID() +")" , null);
		if (runs != null && !runs.isEmpty())
			run = (Run) runs.get(0);

		return run;
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
			{
				values.put(Constants.DB_RUNS_COLUMN_ROUTEID, run.getRoute().getID());
				routeDAO.update(run.getRoute());
			}
			result = DBConnection.ghostDB.insert(Constants.DB_TABLE_RUNS, null, values);
			
			run.setID(result);
			if (run.getWaypoints() != null && !run.getWaypoints().isEmpty()) // do not insert empty waypoints
				waypointDAO.insertWaypointsOfRun(run.getID(), run.getWaypoints());
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
			waypointDAO.deleteWaypointsOfRun(id);
			result = DBConnection.ghostDB.delete(Constants.DB_TABLE_RUNS, Constants.DB_RUNS_COLUMN_ID + "=" + id, null) > 0;
		} catch (Exception e) {
			Log.e(RunDAO.class.toString(), "Error delete(): " + e.toString());
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
			if (run.getWaypoints() != null && !run.getWaypoints().isEmpty()) // do not update empty waypoints
				waypointDAO.updateWaypointsOfRun(run.getID(), run.getWaypoints());
			result = DBConnection.ghostDB.update(Constants.DB_TABLE_RUNS, values, Constants.DB_RUNS_COLUMN_ID + "=" + run.getID(), null) > 0;
		} catch (Exception e) {
			Log.e(RunDAO.class.toString(), "Error update(): " + e.toString());
			result = false;
		}
		return result;
	}
	
	/**
	 * Calculates the performance of this run, compared to other runs of the same route
	 * 
	 * @param run
	 *            the run
	 * @return Performance indicator
	 */
	private Performance calculatePerformance(Run run)
	{
		Performance performance = null;
		if (run.getRoute() != null && !run.getRoute().equals(Route.getEmptyRoute())) // do nothing for empty route
		{
			Cursor cursor = null;
			try
			{
				float avgPace = -1;
				
				cursor = DBConnection.ghostDB.rawQuery("SELECT AVG(" + Constants.DB_RUNS_COLUMN_PACE + ")" +
						"						FROM " + Constants.DB_TABLE_RUNS + "" +
								"				WHERE " + Constants.DB_RUNS_COLUMN_ROUTEID + "=" + run.getRoute().getID(), null);
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						avgPace = cursor.getFloat(0);
					}
				}
				
				if(avgPace == -1)
					throw new Exception("Database error: average pace could not be retrieved");
				
				performance = Run.calculatePerformance(avgPace, run.getPace());
			}
			catch(Exception e)
			{
				performance = null;
				Log.e(RunDAO.class.toString(), e.toString());
			}
			finally {
				if(cursor != null)
				{
					System.out.println("CLOSING CURSOR AVGPACE");
					cursor.close();
				}
			}
		}
		return performance;
	}
}
