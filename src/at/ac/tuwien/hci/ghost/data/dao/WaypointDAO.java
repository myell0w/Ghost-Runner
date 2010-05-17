package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.util.Constants;
import at.ac.tuwien.hci.ghost.util.Date;

public class WaypointDAO extends DataAccessObject {

	public WaypointDAO() {
		super();
	}

	/**
	 * Not used for waypoints.
	 */
	@Override
	public Entity search(long id) {
		return null;
	}

	/**
	 * Not used for waypoints.
	 */
	@Override
	public List<Entity> getAll() {
		return null;
	}

	/**
	 * Returns all stored waypoints that meet the criteria <i>selection</i>, order
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
	 * @return list of waypoints
	 */
	@Override
	protected List<Entity> search(String selection, String orderBy) {
		List<Entity> waypoints = new ArrayList<Entity>();
		try {
			Cursor cursor = null;
			cursor = DBConnection.ghostDB.query(Constants.DB_TABLE_WAYPOINTS,
					               new String[] { Constants.DB_WAYPOINTS_COLUMN_ID,
												  Constants.DB_WAYPOINTS_COLUMN_TIME,
												  Constants.DB_WAYPOINTS_COLUMN_SPEED,
												  Constants.DB_WAYPOINTS_COLUMN_LATITUDE,
												  Constants.DB_WAYPOINTS_COLUMN_LONGITUDE,
												  Constants.DB_WAYPOINTS_COLUMN_ALTITUDE
												},
					               selection,
					               null, null, null,
					               orderBy);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Waypoint waypoint = new Waypoint(cursor.getLong(0));
						waypoint.setTimestamp(new Date(cursor.getLong(1)));
						waypoint.setSpeed(cursor.getFloat(2));
						waypoint.getLocation().setLatitude(cursor.getFloat(3));
						waypoint.getLocation().setLongitude(cursor.getFloat(4));
						waypoint.getLocation().setAltitude(cursor.getFloat(5));

						waypoints.add(waypoint);
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		} catch (Exception e) {
			waypoints = null;
			Log.e(WaypointDAO.class.toString(), "Error search(): " + e.toString());
		}
		return waypoints;
	}
	
	private List<Waypoint> entitiesToWaypoints(List<Entity> entities) {
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		for (Entity e : entities) {
			if (e instanceof Waypoint) {
				waypoints.add((Waypoint) e);
			}
		}
		return waypoints;
	}
	
	public List<Waypoint> getAllWaypointsOfRoute(Route route) {
		if (route != null)
			return getAllWaypointsOfRoute(route.getID());
		return null;
	}
	
	public List<Waypoint> getAllWaypointsOfRoute(long routeId) {
		return entitiesToWaypoints(search(Constants.DB_WAYPOINTS_COLUMN_ROUTEID + "=" + routeId, null));
	}
	
	public List<Waypoint> getAllWaypointsOfRun(Run run) {
		if(run != null)
			return getAllWaypointsOfRun(run.getID());
		return null;
	}
	
	public List<Waypoint> getAllWaypointsOfRun(long runId) {
		return entitiesToWaypoints(search(Constants.DB_WAYPOINTS_COLUMN_RUNID + "=" + runId, null));
	}
	
	public boolean  insertWaypointsOfRoute(Route route, List<Waypoint> waypoints) {
		if(route != null)
			return insertWaypointsOfRoute(route.getID(), waypoints);
		return false;
	}
	
	public boolean insertWaypointsOfRoute(long routeId, List<Waypoint> waypoints) {
		boolean result = false;
		long insertResult = -1;
		DBConnection.ghostDB.beginTransaction();
		try
		{
			for(Waypoint waypoint:waypoints)
			{
				ContentValues values = new ContentValues();
				if(waypoint.getTimestamp() != null)
					values.put(Constants.DB_WAYPOINTS_COLUMN_TIME, waypoint.getTimestamp().getAsJavaDefaultDate().getTime());
				values.put(Constants.DB_WAYPOINTS_COLUMN_SPEED, waypoint.getSpeed());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LATITUDE, waypoint.getLocation().getLatitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LONGITUDE, waypoint.getLocation().getLongitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_ALTITUDE, waypoint.getLocation().getAltitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_ROUTEID, routeId);
				values.putNull(Constants.DB_WAYPOINTS_COLUMN_RUNID);
				insertResult = DBConnection.ghostDB.insert(Constants.DB_TABLE_WAYPOINTS, null, values);
				if(insertResult == -1)
					throw new Exception("Database error while updating waypoints");
			}
			DBConnection.ghostDB.setTransactionSuccessful();
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			Log.e(WaypointDAO.class.toString(), "Error insertWaypointsOfRoute(): " + e.toString());
		}
		DBConnection.ghostDB.endTransaction();
		return result;
	}
	
	public boolean insertWaypointsOfRun(Run run, List<Waypoint> waypoints) {
		if(run != null)
			return insertWaypointsOfRun(run.getID(), waypoints);
		return false;
	}
	
	public boolean insertWaypointsOfRun(long runId, List<Waypoint> waypoints) {
		boolean result = false;
		long insertResult = -1;
		DBConnection.ghostDB.beginTransaction();
		try
		{
			for(Waypoint waypoint:waypoints)
			{
				ContentValues values = new ContentValues();
				if(waypoint.getTimestamp() != null)
					values.put(Constants.DB_WAYPOINTS_COLUMN_TIME, waypoint.getTimestamp().getAsJavaDefaultDate().getTime());
				values.put(Constants.DB_WAYPOINTS_COLUMN_SPEED, waypoint.getSpeed());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LATITUDE, waypoint.getLocation().getLatitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LONGITUDE, waypoint.getLocation().getLongitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_ALTITUDE, waypoint.getLocation().getAltitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_RUNID, runId);
				values.putNull(Constants.DB_WAYPOINTS_COLUMN_ROUTEID);
				insertResult = DBConnection.ghostDB.insert(Constants.DB_TABLE_WAYPOINTS, null, values);
				if(insertResult == -1)
					throw new Exception("Database error while inserting waypoints");
			}
			DBConnection.ghostDB.setTransactionSuccessful();
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			Log.e(WaypointDAO.class.toString(), "Error insertWaypointsOfRun(): " + e.toString());
		}
		DBConnection.ghostDB.endTransaction();
		return result;
	}
	
	public boolean deleteWaypointsOfRoute(Route route)
	{
		if(route != null)
			return deleteWaypointsOfRoute(route.getID());
		return false;
	}
	
	public boolean deleteWaypointsOfRoute(long routeId)
	{
		boolean result = false;
		try {
			result = DBConnection.ghostDB.delete(Constants.DB_TABLE_WAYPOINTS, Constants.DB_WAYPOINTS_COLUMN_ROUTEID + "=" + routeId, null) > 0;
		} catch (Exception e) {
			Log.e(WaypointDAO.class.toString(), "Error deleteWaypointsOfRoute(): " + e.toString());
			result = false;
		}
		return result;
	}
	
	public boolean deleteWaypointsOfRun(Run run)
	{
		if(run != null)
			return deleteWaypointsOfRun(run.getID());
		return false;
	}
	
	public boolean deleteWaypointsOfRun(long runId)
	{
		boolean result = false;
		try {
			result = DBConnection.ghostDB.delete(Constants.DB_TABLE_WAYPOINTS, Constants.DB_WAYPOINTS_COLUMN_RUNID + "=" + runId, null) > 0;
		} catch (Exception e) {
			Log.e(WaypointDAO.class.toString(), "Error deleteWaypointsOfRun(): " + e.toString());
			result = false;
		}
		return result;
	}
	
	public boolean updateWaypointsOfRun(Run run, List<Waypoint> waypoints)
	{
		if(run != null)
			return updateWaypointsOfRun(run.getID(), waypoints);
		return false;
	}
	
	public boolean updateWaypointsOfRun(long runId, List<Waypoint> waypoints)
	{
		boolean result = false;
		long insertResult = -1;
		DBConnection.ghostDB.beginTransaction();
		try
		{
			// 1. delete old waypoints
			result = DBConnection.ghostDB.delete(Constants.DB_TABLE_WAYPOINTS, Constants.DB_WAYPOINTS_COLUMN_RUNID + "=" + runId, null) > 0;
			if(!result)
				throw new Exception("Database error while updating waypoints: DELETE");
			
			// 2. insert new waypoints
			for(Waypoint waypoint:waypoints)
			{
				ContentValues values = new ContentValues();
				if(waypoint.getTimestamp() != null)
					values.put(Constants.DB_WAYPOINTS_COLUMN_TIME, waypoint.getTimestamp().getAsJavaDefaultDate().getTime());
				values.put(Constants.DB_WAYPOINTS_COLUMN_SPEED, waypoint.getSpeed());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LATITUDE, waypoint.getLocation().getLatitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LONGITUDE, waypoint.getLocation().getLongitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_ALTITUDE, waypoint.getLocation().getAltitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_RUNID, runId);
				values.putNull(Constants.DB_WAYPOINTS_COLUMN_ROUTEID);
				insertResult = DBConnection.ghostDB.insert(Constants.DB_TABLE_WAYPOINTS, null, values);
				if(insertResult == -1)
					throw new Exception("Database error while updating waypoints: INSERT");
			}
			DBConnection.ghostDB.setTransactionSuccessful();
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			Log.e(WaypointDAO.class.toString(), "Error insertWaypointsOfRun(): " + e.toString());
		}
		DBConnection.ghostDB.endTransaction();
		return result;
	}
	
	public boolean updateWaypointsOfRoute(Route route, List<Waypoint> waypoints)
	{
		if(route != null)
			return updateWaypointsOfRun(route.getID(), waypoints);
		return false;
	}
	
	public boolean updateWaypointsOfRoute(long routeId, List<Waypoint> waypoints)
	{
		boolean result = false;
		long insertResult = -1;
		DBConnection.ghostDB.beginTransaction();
		try
		{
			// 1. delete old waypoints
			result = DBConnection.ghostDB.delete(Constants.DB_TABLE_WAYPOINTS, Constants.DB_WAYPOINTS_COLUMN_ROUTEID + "=" + routeId, null) > 0;
			if(!result)
				throw new Exception("Database error while updating waypoints: DELETE");
			
			// 2. insert new waypoints
			for(Waypoint waypoint:waypoints)
			{
				ContentValues values = new ContentValues();
				if(waypoint.getTimestamp() != null)
					values.put(Constants.DB_WAYPOINTS_COLUMN_TIME, waypoint.getTimestamp().getAsJavaDefaultDate().getTime());
				values.put(Constants.DB_WAYPOINTS_COLUMN_SPEED, waypoint.getSpeed());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LATITUDE, waypoint.getLocation().getLatitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_LONGITUDE, waypoint.getLocation().getLongitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_ALTITUDE, waypoint.getLocation().getAltitude());
				values.put(Constants.DB_WAYPOINTS_COLUMN_ROUTEID, routeId);
				values.putNull(Constants.DB_WAYPOINTS_COLUMN_RUNID);
				insertResult = DBConnection.ghostDB.insert(Constants.DB_TABLE_WAYPOINTS, null, values);
				if(insertResult == -1)
					throw new Exception("Database error while updating waypoints: INSERT");
			}
			DBConnection.ghostDB.setTransactionSuccessful();
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			Log.e(WaypointDAO.class.toString(), "Error insertWaypointsOfRoute(): " + e.toString());
		}
		DBConnection.ghostDB.endTransaction();
		return result;
	}
	
	/**
	 * Not used for waypoints.
	 */
	@Override
	public long insert(Entity entity) {
		return -1;
	}

	/**
	 * Not used for waypoints
	 */
	@Override
	public boolean delete(long id) {
		return false;
	}

	/**
	 * Not used for waypoints.
	 */
	@Override
	public boolean update(Entity entity) {
		return false;
	}

}
