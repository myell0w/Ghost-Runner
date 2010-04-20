package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.util.Constants;

public class RouteDAO extends DataAccessObject {

	public RouteDAO(Context context) {
		super(context);
	}
	
	/**
     * Returns a spefific route identified by id. Not found or error: null
     * 
     * @param id the id of the route
     * @return the route or null if failed
     */
	public Entity search(long id)
	{
		Route route = null;
		try
		{
			Cursor cursor = null;
			cursor = ghostDB.query(true,
								Constants.DB_TABLE_ROUTES,
								new String[] {Constants.DB_ROUTES_COLUMN_ID,
								Constants.DB_ROUTES_COLUMN_DISTANCE,
								Constants.DB_ROUTES_COLUMN_NAME,
								Constants.DB_ROUTES_COLUMN_RUNCOUNT},
								Constants.DB_ROUTES_COLUMN_ID + "=" + id,
								null, null, null, null, null);
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					route = new Route(cursor.getLong(0));
					route.setDistance(cursor.getFloat(1));
					route.setName(cursor.getString(2));
					route.setRunCount(cursor.getInt(3));
				}
			}
		}
		catch(Exception e)
		{
			route = null;
			Log.e(RouteDAO.class.toString(),"Error search(): " + e.toString());
		}
		return route;
	}

	/**
     * Returns a list of routes that meet the criteria given in searchTerms.
     * For detailed information ask Mr Tretter.
     * Not found: empty list
     * Error: null
     * 
     * @param searchTerms the search criteria
     * @return list of routes
     */
	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		List<Entity> routes = new ArrayList<Entity>();
		
		/* TODO remove stub shit */
		routes.add(new Route(1,"Route 66", 3.4f, 12));
		routes.add(new Route(2,"Fun", 3.4f, 2));
		routes.add(new Route(3,"Home Run", 1.4f));
		routes.add(new Route(4,"Warm up", 2.4f, 32));
		
		try
		{
			Cursor cursor = null;
			cursor = ghostDB.query(Constants.DB_TABLE_ROUTES,
									new String[] {Constants.DB_ROUTES_COLUMN_ID,
									Constants.DB_ROUTES_COLUMN_DISTANCE,
									Constants.DB_ROUTES_COLUMN_NAME,
									Constants.DB_ROUTES_COLUMN_RUNCOUNT},
									null, null, null, null, null);
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					do
					{
						Route route = new Route(cursor.getLong(0));
						route.setDistance(cursor.getFloat(1));
						route.setName(cursor.getString(2));
						route.setRunCount(cursor.getInt(3));

						routes.add(route);
					}
					while(cursor.moveToNext());
				}
				cursor.close();
			}
		}
		catch(Exception e)
		{
			routes = null;
			Log.e(RouteDAO.class.toString(),"Error search(): " + e.toString());
		}
		return routes;
	}
	
	/**
     * Inserts a specific route
     * 
     * @param entity the route to insert
     * @return the generated autoincrement id or -1 if failed
     */
	public long insert(Entity entity)
	{
		long result;
		try
		{
			Route route = (Route)entity;
			ContentValues values = new ContentValues();
			values.put(Constants.DB_ROUTES_COLUMN_DISTANCE, route.getDistance());
			if(route.getName() != null)
				values.put(Constants.DB_ROUTES_COLUMN_NAME, route.getName());
			values.put(Constants.DB_ROUTES_COLUMN_RUNCOUNT, route.getRunCount());
			result = ghostDB.insert(Constants.DB_TABLE_ROUTES, null, values);
		}
		catch(Exception e)
		{
			Log.e(RouteDAO.class.toString(),"Error insert(): " + e.toString());
			result = -1;
		}
		return result;
	}
	
	/**
     * Deletes a spefific route identified by id. Not found or error: false
     * 
     * @param id the id of the route
     * @return true, false if failed
     */
	public boolean delete(long id)
	{
		boolean result = false;
		try
		{
			result = ghostDB.delete(Constants.DB_TABLE_ROUTES, Constants.DB_ROUTES_COLUMN_ID + "=" + id , null) > 0;
		}
		catch(Exception e)
		{
			Log.e(RouteDAO.class.toString(),"Error delete(): " + e.toString());
			result = false;
		}
		return result;
	}

	/**
     * Updates a spefific route identified by id of entity with the data stored in entity.
     * Not found or error: false
     * 
     * @param entity the entity to update
     * @return true, false if failed
     */
	public boolean update(Entity entity)
	{
		boolean result = false;
		try
		{
			Route route = (Route)entity;
			ContentValues values = new ContentValues();
			values.put(Constants.DB_ROUTES_COLUMN_DISTANCE, route.getDistance());
			if(route.getName() != null)
				values.put(Constants.DB_ROUTES_COLUMN_NAME, route.getName());
			values.put(Constants.DB_ROUTES_COLUMN_RUNCOUNT, route.getRunCount());
			result =  ghostDB.update(Constants.DB_TABLE_ROUTES, values, Constants.DB_ROUTES_COLUMN_ID + "=" + route.getID(), null) > 0;
		}
		catch(Exception e)
		{
			Log.e(RouteDAO.class.toString(),"Error update(): " + e.toString());
			result = false;
		}
		return result;
	}

}
