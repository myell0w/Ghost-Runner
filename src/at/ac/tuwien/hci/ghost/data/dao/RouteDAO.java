package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Period;
import at.ac.tuwien.hci.ghost.data.entities.Goal.Type;
import at.ac.tuwien.hci.ghost.util.Constants;

public class RouteDAO extends DataAccessObject {

	public RouteDAO(Context context) {
		super(context);
	}

	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove Stub-Implementation
		List<Entity> routes = new ArrayList<Entity>();
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
						Route route = new Route(cursor.getInt(0));
						route.setDistance(cursor.getFloat(1));
						route.setName(cursor.getString(2));
						route.setRunCount(cursor.getInt(3));

						routes.add(route);
					}
					while(cursor.moveToNext());
				}
				cursor.close();
			}
			routes.add(new Route(1,"Route 66", 3.4f, 12));
			routes.add(new Route(2,"Fun", 3.4f, 2));
			routes.add(new Route(3,"Home Run", 1.4f));
			routes.add(new Route(4,"Warm up", 2.4f, 32));
		}
		catch(Exception e)
		{
			Log.e(GoalDAO.class.toString(),"Fuck3: " + e);
		}
		return routes;
	}

	@Override
	public boolean delete(long id) {
		return ghostDB.delete(Constants.DB_TABLE_ROUTES, Constants.DB_ROUTES_COLUMN_ID + "=" + id , null) > 0;
	}

	@Override
	public long insert(Entity entity) {
		Route route = (Route)entity;
		long result;
		ContentValues values = new ContentValues();
		values.put(Constants.DB_ROUTES_COLUMN_DISTANCE, route.getDistance());
		if(route.getName() != null)
			values.put(Constants.DB_ROUTES_COLUMN_NAME, route.getName());
		values.put(Constants.DB_ROUTES_COLUMN_RUNCOUNT, route.getRunCount());
		result = ghostDB.insert(Constants.DB_TABLE_ROUTES, null, values);
		Log.e(GoalDAO.class.toString(),"Inserted feckin' value in table Routes");
		return result;
	}

	@Override
	public Entity search(long id) {
		Route route = null;
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
				route = new Route(cursor.getInt(0));
				route.setDistance(cursor.getFloat(1));
				route.setName(cursor.getString(2));
				route.setRunCount(cursor.getInt(3));
			}
		}
		return route;
	}

	@Override
	public boolean update(Entity entity) {
		Route route = (Route)entity;
		ContentValues values = new ContentValues();
		values.put(Constants.DB_ROUTES_COLUMN_DISTANCE, route.getDistance());
		if(route.getName() != null)
			values.put(Constants.DB_ROUTES_COLUMN_NAME, route.getName());
		values.put(Constants.DB_ROUTES_COLUMN_RUNCOUNT, route.getRunCount());
		return ghostDB.update(Constants.DB_TABLE_ROUTES, values, Constants.DB_ROUTES_COLUMN_ID + "=" + route.getID(), null) > 0;
	}

}
