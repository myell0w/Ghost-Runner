package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Waypoint;
import at.ac.tuwien.hci.ghost.util.Constants;

public class RouteDAO extends DataAccessObject {
	
	private WaypointDAO waypointDAO = null; // used for getting the waypoints of a run

	public RouteDAO(Context context) {
		super(context);
		waypointDAO = new WaypointDAO(context);
	}

	/**
	 * Returns a spefific route identified by id. Not found or error: null
	 * 
	 * @param id
	 *            the id of the route
	 * @return the route or null if failed
	 */
	@Override
	public Entity search(long id) {
		Route route = null;

		List<Entity> routes = search(Constants.DB_ROUTES_COLUMN_ID + "=" + id, null);
		if (routes != null && !routes.isEmpty())
			route = (Route) routes.get(0);

		return route;
	}

	/**
	 * Returns all stored routes. Not found: empty list Error: null
	 * 
	 * @return list of routes
	 */
	@Override
	public List<Entity> getAll() {
		List<Entity> routes = null;
		routes = search(null, null);

		if (routes == null)
			routes = new ArrayList<Entity>();

		return routes;
	}

	/**
	 * Returns all stored routes that meet the criteria <i>selection</i>, order
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
	 * @return list of routes
	 */
	@Override
	protected List<Entity> search(String selection, String orderBy) {
		List<Entity> routes = new ArrayList<Entity>();

		try {
			Cursor cursor = null;
			cursor = ghostDB.query(Constants.DB_TABLE_ROUTES, new String[] { Constants.DB_ROUTES_COLUMN_ID, Constants.DB_ROUTES_COLUMN_DISTANCE,
					Constants.DB_ROUTES_COLUMN_NAME, Constants.DB_ROUTES_COLUMN_RUNCOUNT }, selection, null, null, null, orderBy);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						Route route = new Route(cursor.getLong(0));
						route.setDistanceInKm(cursor.getFloat(1));
						route.setName(cursor.getString(2));
						route.setRunCount(cursor.getInt(3));
						List<Waypoint> waypoints = waypointDAO.getAllWaypointsOfRoute(route.getID());
						route.setWaypoints(waypoints);

						routes.add(route);
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
		} catch (Exception e) {
			routes = null;
			Log.e(RouteDAO.class.toString(), "Error search(): " + e.toString());
		}
		return routes;
	}

	/**
	 * Inserts a specific route
	 * 
	 * @param entity
	 *            the route to insert
	 * @return the generated autoincrement id or -1 if failed
	 */
	@Override
	public long insert(Entity entity) {
		long result;
		try {
			Route route = (Route) entity;
			ContentValues values = new ContentValues();
			values.put(Constants.DB_ROUTES_COLUMN_DISTANCE, route.getDistanceInKm());
			if (route.getName() != null)
				values.put(Constants.DB_ROUTES_COLUMN_NAME, route.getName());
			values.put(Constants.DB_ROUTES_COLUMN_RUNCOUNT, route.getRunCount());
			if(route.getWaypoints() != null && !route.getWaypoints().isEmpty()) // do not insert empty waypoints list
				waypointDAO.insertWaypointsOfRoute(route.getID(), route.getWaypoints());
			result = ghostDB.insert(Constants.DB_TABLE_ROUTES, null, values);
		} catch (Exception e) {
			Log.e(RouteDAO.class.toString(), "Error insert(): " + e.toString());
			result = -1;
		}
		return result;
	}

	/**
	 * Deletes a spefific route identified by id. Not found or error: false
	 * 
	 * @param id
	 *            the id of the route
	 * @return true, false if failed
	 */
	@Override
	public boolean delete(long id) {
		boolean result = false;
		try {
			waypointDAO.deleteWaypointsOfRoute(id);
			result = ghostDB.delete(Constants.DB_TABLE_ROUTES, Constants.DB_ROUTES_COLUMN_ID + "=" + id, null) > 0;
		} catch (Exception e) {
			Log.e(RouteDAO.class.toString(), "Error delete(): " + e.toString());
			result = false;
		}
		return result;
	}

	/**
	 * Updates a spefific route identified by id of entity with the data stored
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
			Route route = (Route) entity;
			ContentValues values = new ContentValues();
			values.put(Constants.DB_ROUTES_COLUMN_DISTANCE, route.getDistanceInKm());
			if (route.getName() != null)
				values.put(Constants.DB_ROUTES_COLUMN_NAME, route.getName());
			values.put(Constants.DB_ROUTES_COLUMN_RUNCOUNT, route.getRunCount());
			if(route.getWaypoints() != null && !route.getWaypoints().isEmpty()) // do not update empty waypoints list
				waypointDAO.updateWaypointsOfRoute(route.getID(), route.getWaypoints());
			result = ghostDB.update(Constants.DB_TABLE_ROUTES, values, Constants.DB_ROUTES_COLUMN_ID + "=" + route.getID(), null) > 0;
		} catch (Exception e) {
			Log.e(RouteDAO.class.toString(), "Error update(): " + e.toString());
			result = false;
		}
		return result;
	}

}
