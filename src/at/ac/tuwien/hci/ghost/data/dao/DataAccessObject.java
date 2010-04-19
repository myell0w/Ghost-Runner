package at.ac.tuwien.hci.ghost.data.dao;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import at.ac.tuwien.hci.ghost.data.entities.Entity;

public abstract class DataAccessObject {
	
	protected SQLiteDatabase ghostDB = null;
	protected DBOpenHelper dbOpenHelper = null;
	protected Context context = null;
	
	public DataAccessObject(Context context)
	{
		this.context = context;
		dbOpenHelper = new DBOpenHelper(context);
		open();
	}
	
	protected void open()
	{
		ghostDB = dbOpenHelper.getWritableDatabase();
	}
	
	protected void close()
	{
		ghostDB.close();
	}
	
	abstract public List<Entity> search(List<Entity> searchTerms);
	abstract public Entity search(long id);
	abstract public long insert(Entity entity);
	abstract public boolean delete(long id);
	abstract public boolean update(Entity entity);
}
