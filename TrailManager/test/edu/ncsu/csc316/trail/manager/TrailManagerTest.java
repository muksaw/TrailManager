/**
 * 
 */
package edu.ncsu.csc316.trail.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;

/**
 * This is the test class for TrailManager
 * @author Mukul Sauhta
 *
 */
public class TrailManagerTest {

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.TrailManager#TrailManager(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testTrailManager() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		TrailManager manager;
		try {
			manager = new TrailManager(landmarkFile, trailFile);
			assertNotNull(manager);
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.TrailManager#getDistancesToDestinations(java.lang.String)}.
	 */
	@Test
	public void testGetDistancesToDestinations() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		TrailManager manager;
		try {
			manager = new TrailManager(landmarkFile, trailFile);
			assertNotNull(manager);
			Map<Landmark, Integer> h = manager.getDistancesToDestinations("L02");
			assertNotNull(h);
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
		
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.TrailManager#getLandmarkByID(java.lang.String)}.
	 */
	@Test
	public void testGetLandmarkByID() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		TrailManager manager;
		try {
			manager = new TrailManager(landmarkFile, trailFile);
			assertNotNull(manager);
			assertEquals("Park Entrance", manager.getLandmarkByID("L01").getDescription());
			assertNull(manager.getLandmarkByID("hh"));
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.TrailManager#getProposedFirstAidLocations(int)}.
	 */
	@Test
	public void testGetProposedFirstAidLocations() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		TrailManager manager;
		try {
			manager = new TrailManager(landmarkFile, trailFile);
			assertNotNull(manager);
			Map<Landmark, List<Trail>> h = manager.getProposedFirstAidLocations(3);
			assertEquals(1, h.size());
			String str = h.toString();
			assertEquals("SkipListMap[Landmark [id=L01, description=Park Entrance, type=Location]]", str);
			
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
	}

}
