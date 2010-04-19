package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Route;

public class RouteDAO extends DataAccessObject {

	public RouteDAO(Context context) {
		super(context);
	}

	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove Stub-Implementation
		List<Entity> routes = new ArrayList<Entity>();
		
		routes.add(new Route(1,"Route 66", 3.4f, 12));
		routes.add(new Route(2,"Fun", 3.4f, 2));
		routes.add(new Route(3,"Home Run", 1.4f));
		routes.add(new Route(4,"Warm up", 2.4f, 32));
		
		return routes;
	}

}
