package at.ac.tuwien.hci.ghost.data.dao;

import java.util.List;

import at.ac.tuwien.hci.ghost.data.entities.Entity;

public interface DataAccessObject {
	public List<Entity> search(List<Entity> searchTerms);
}
