package at.ac.tuwien.hci.ghost.data.dao;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.hci.ghost.data.entities.Entity;
import at.ac.tuwien.hci.ghost.data.entities.Goal;

public class GoalDAO implements DataAccessObject {

	@Override
	public List<Entity> search(List<Entity> searchTerms) {
		// TODO remove Stub-Implementation
		List<Entity> goals = new ArrayList<Entity>();
		
		goals.add(new Goal(1,Goal.Type.DISTANCE, 50.f, 0.4f));
		goals.add(new Goal(2,Goal.Type.CALORIES, 2000.f));
		goals.add(new Goal(3,Goal.Type.RUNS, 20));
		
		return goals;
	}

}
