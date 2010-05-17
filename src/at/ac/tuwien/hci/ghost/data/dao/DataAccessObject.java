package at.ac.tuwien.hci.ghost.data.dao;

import java.util.List;

import at.ac.tuwien.hci.ghost.data.entities.Entity;

public abstract class DataAccessObject {

	/*protected DBOpenHelper dbOpenHelper = null;
	protected Context context = null;

	public DataAccessObject(Context context) {
		this.context = context;
		dbOpenHelper = new DBOpenHelper(context);
		open();
	}

	protected void open() {
		ghostDB = dbOpenHelper.getWritableDatabase();
	}

	protected void close() {
		ghostDB.close();
	}*/

	// abstract public List<Entity> search(List<Entity> searchTerms);
	abstract protected List<Entity> search(String selection, String orderBy);

	abstract public List<Entity> getAll();

	abstract public Entity search(long id);

	abstract public long insert(Entity entity);

	abstract public boolean delete(long id);

	abstract public boolean update(Entity entity);
}
