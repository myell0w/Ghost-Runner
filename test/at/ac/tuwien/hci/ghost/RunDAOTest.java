package at.ac.tuwien.hci.ghost;


import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;
import android.test.AndroidTestCase;
import at.ac.tuwien.hci.ghost.data.dao.RunDAO;
import at.ac.tuwien.hci.ghost.data.entities.Route;
import at.ac.tuwien.hci.ghost.data.entities.Run;
import at.ac.tuwien.hci.ghost.util.Date;


public class RunDAOTest extends AndroidTestCase {
	
	private RunDAO runDAO = null;
	
	protected void setUp() throws Exception {
		System.out.println("SETTING UP");
		if(runDAO == null)
			runDAO = new RunDAO(this.getContext());
		Run run1 = new Run(1, new Date(1,1,2010), 30, 0.5f, 5, Route.getEmptyRoute()); // oldest
		Run run2 = new Run(2, new Date(1,2,2010), 60, 1.0f, 10, Route.getEmptyRoute());
		Run run3 = new Run(3, new Date(1,3,2010), 120, 2.0f, 20, Route.getEmptyRoute()); // newest
		System.out.println("INSERTING RUNS");
		runDAO.insert(run1);
		runDAO.insert(run2);
		runDAO.insert(run3);
	}
	
	protected void tearDown() throws Exception {
		System.out.println("TEARING DOWN");
		if(runDAO.delete(1) == false)
			System.out.println("ERROR DELETING ALL");
		else
			System.out.println("SUCCESS DELETING ALL");
		runDAO.delete(2);
		runDAO.delete(3);
	}

    public void testGetAll() throws Throwable {
    	System.out.println("TESTING testGetAll");
    	List<Run> allRuns = runDAO.entitiesToRuns(runDAO.getAll());
    	assertEquals("Size of allRuns must be 3", 3, allRuns.size());
    }
    
    public void testFuck() throws Throwable {
    	System.out.println("TESTING testFuck");
    }
}