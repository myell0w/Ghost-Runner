package at.ac.tuwien.hci.ghost.data.dao;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
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
     * @param id the id of the run
     * @return the run or null if failed
     */
	@Override
	public Entity search(long id)
	{
		Run run = null;
		try
		{
			Cursor cursor = null;
			cursor = ghostDB.query(true,
								Constants.DB_TABLE_RUNS,
								new String[] {Constants.DB_RUNS_COLUMN_ID,
											  Constants.DB_RUNS_COLUMN_DATE,
											  Constants.DB_RUNS_COLUMN_TIMEINSECONDS,
											  Constants.DB_RUNS_COLUMN_DISTANCE,
											  Constants.DB_RUNS_COLUMN_PACE,
											  Constants.DB_RUNS_COLUMN_SPEED,
											  Constants.DB_RUNS_COLUMN_CALORIES,
											  Constants.DB_RUNS_COLUMN_ROUTEID},
								Constants.DB_RUNS_COLUMN_ID + "=" + id,
								null, null, null, null, null);
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					run = new Run(cursor.getLong(0));
					run.setDate(new Date(cursor.getLong(1)));
					run.setTime(cursor.getLong(2));
					run.setDistance(cursor.getFloat(3));
					run.setPace(cursor.getFloat(4));
					run.setSpeed(cursor.getFloat(5));
					run.setCalories(cursor.getInt(6));
					if(!cursor.isNull(7))
					{
						run.setRoute((Route) routeDAO.search(cursor.getLong(7)));
					}
				}
			}
		}
		catch(Exception e)
		{
			run = null;
			Log.e(RunDAO.class.toString(),"Error search(): " + e.toString());
		}
		return run;
	}

	
	/**
     * Returns a list of runs that meet the criteria given in searchTerms.
     * For detailed information ask Mr Tretter.
     * Not found: empty list
     * Error: null
     * 
     * @param searchTerms the search criteria
     * @return list of runs
     */
	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		List<Entity> runs = new ArrayList<Entity>();
		
		/* TODO Remove stub shit */
		if (searchTerms != null && !searchTerms.isEmpty() && searchTerms.get(0).getID() == 1) {
			runs.add(new Run(1,new Date(), 3600, 9.56f, 323, null));
			runs.add(new Run(2,new Date(12,3,2010), 3300, 5.56f, 323, null));
			} else {
				runs.add(new Run(3,new Date(2,3,2010,14,20), 5600, 12.56f, 423, null));
				runs.add(new Run(4,new Date(3,3,2010,17,22), 7600, 23.56f, 523, null));
				runs.add(new Run(5,new Date(4,3,2010,12,00), 1600, 2.56f, 623, null));
			}
		
		try
		{
			Cursor cursor = null;
			cursor = ghostDB.query(Constants.DB_TABLE_RUNS,
									new String[] {Constants.DB_RUNS_COLUMN_ID,
									  			  Constants.DB_RUNS_COLUMN_DATE,
									  			  Constants.DB_RUNS_COLUMN_TIMEINSECONDS,
									  			  Constants.DB_RUNS_COLUMN_DISTANCE,
									  			  Constants.DB_RUNS_COLUMN_PACE,
									  			  Constants.DB_RUNS_COLUMN_SPEED,
									  			  Constants.DB_RUNS_COLUMN_CALORIES,
									  			  Constants.DB_RUNS_COLUMN_ROUTEID},
									null, null, null, null, null);
			if(cursor != null)
			{
				if(cursor.moveToFirst())
				{
					do
					{
						Run run = new Run(cursor.getLong(0));
						run.setDate(new Date(cursor.getLong(1)));
						run.setTime(cursor.getLong(2));
						run.setDistance(cursor.getFloat(3));
						run.setPace(cursor.getFloat(4));
						run.setSpeed(cursor.getFloat(5));
						run.setCalories(cursor.getInt(6));
						if(!cursor.isNull(7))
						{
							run.setRoute((Route) routeDAO.search(cursor.getLong(7)));
						}

						runs.add(run);
					}
					while(cursor.moveToNext());
				}
				cursor.close();
			}
		}
		catch(Exception e)
		{
			runs = null;
			Log.e(RunDAO.class.toString(),"Error search(): " + e.toString());
		}
		return runs;
	}
	
	/*@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove method stub
		List<Entity> searchResult = new ArrayList<Entity>();
		
		if (searchTerms != null && !searchTerms.isEmpty() && searchTerms.get(0).getID() == 1) {
		searchResult.add(new Run(1,new Date(), 3600, 9.56f, 323, null));
		searchResult.add(new Run(2,new Date(12,3,2010), 3300, 5.56f, 323, null));
		} else {
			searchResult.add(new Run(3,new Date(2,3,2010,14,20), 5600, 12.56f, 423, null));
			searchResult.add(new Run(4,new Date(3,3,2010,17,22), 7600, 23.56f, 523, null));
			searchResult.add(new Run(5,new Date(4,3,2010,12,00), 1600, 2.56f, 623, null));
		}
		
		return searchResult;
	}*/

	public List<Run> getAllRunsOfRoute(Route route) {
		List<Entity> searchTerm = new ArrayList<Entity>(1);
		List<Entity> searchResult = null;
		List<Run> ret = null;
		
		searchTerm.add(route);
		searchResult = search(searchTerm);
		
		ret = new ArrayList<Run>(searchResult.size());
		
		for (Entity e : searchResult) {
			if (e instanceof Run) {
				ret.add((Run)e);
			}
		}
		
		return ret;
	}
	
	public List<Run> getAllRunsInMonth(int month) {
		// TODO: remove stub-implementation and implement
		List<Entity> searchResult = null;
		List<Run> ret = null;
		
		searchResult = search(null);
		
		ret = new ArrayList<Run>(searchResult.size());
		
		for (Entity e : searchResult) {
			if (e instanceof Run) {
				ret.add((Run)e);
			}
		}
		
		return ret;
		
	}

	/**
     * Inserts a specific run
     * 
     * @param entity the run to insert
     * @return the generated autoincrement id or -1 if failed
     */
	@Override
	public long insert(Entity entity)
	{
		long result;
		try
		{
			Run run = (Run)entity;
			ContentValues values = new ContentValues();
			if(run.getDate() != null)
				values.put(Constants.DB_RUNS_COLUMN_DATE, run.getDate().getAsJavaDefaultDate().getTime());
			values.put(Constants.DB_RUNS_COLUMN_TIMEINSECONDS, run.getTime());
			values.put(Constants.DB_RUNS_COLUMN_DISTANCE, run.getDistance());
			values.put(Constants.DB_RUNS_COLUMN_PACE, run.getPace());
			values.put(Constants.DB_RUNS_COLUMN_SPEED, run.getSpeed());
			values.put(Constants.DB_RUNS_COLUMN_CALORIES, run.getCalories());
			if(run.getRoute() != null)
				values.put(Constants.DB_RUNS_COLUMN_ROUTEID, run.getRoute().getID());
			result = ghostDB.insert(Constants.DB_TABLE_RUNS, null, values);
		}
		catch(Exception e)
		{
			Log.e(RunDAO.class.toString(),"Error insert(): " + e.toString());
			result = -1;
		}
		return result;
	}
	
	/**
     * Deletes a spefific run identified by id. Not found or error: false
     * 
     * @param id the id of the run
     * @return true, false if failed
     */
	public boolean delete(long id)
	{
		boolean result = false;
		try
		{
			result = ghostDB.delete(Constants.DB_TABLE_RUNS, Constants.DB_RUNS_COLUMN_ID + "=" + id , null) > 0;
		}
		catch(Exception e)
		{
			Log.e(RunDAO.class.toString(),"Error delete(): " + e.toString());
			result = false;
		}
		return result;
	}
	
	/**
     * Updates a spefific run identified by id of entity with the data stored in entity.
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
			Run run = (Run)entity;
			ContentValues values = new ContentValues();
			if(run.getDate() != null)
				values.put(Constants.DB_RUNS_COLUMN_DATE, run.getDate().getAsJavaDefaultDate().getTime());
			values.put(Constants.DB_RUNS_COLUMN_TIMEINSECONDS, run.getTime());
			values.put(Constants.DB_RUNS_COLUMN_DISTANCE, run.getDistance());
			values.put(Constants.DB_RUNS_COLUMN_PACE, run.getPace());
			values.put(Constants.DB_RUNS_COLUMN_SPEED, run.getSpeed());
			values.put(Constants.DB_RUNS_COLUMN_CALORIES, run.getCalories());
			if(run.getRoute() != null)
				values.put(Constants.DB_RUNS_COLUMN_ROUTEID, run.getRoute().getID());
			result = ghostDB.update(Constants.DB_TABLE_RUNS, values, Constants.DB_RUNS_COLUMN_ID + "=" + run.getID(), null) > 0;
		}
		catch(Exception e)
		{
			Log.e(RunDAO.class.toString(),"Error update(): " + e.toString());
			result = false;
		}
		return result;
	}
}
