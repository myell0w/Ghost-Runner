package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.util.Date;

public class RunDAO implements DataAccessObject {

	@Override
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
	}

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
}
