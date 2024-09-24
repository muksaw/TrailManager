/**
 * 
 */
package edu.ncsu.csc316.trail.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

/**
 * This is the test class for ReportManager
 * @author Mukul Sauhta
 *
 */
public class ReportManagerTest {

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.ReportManager#ReportManager(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testReportManager() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		ReportManager manager;
		try {
			manager = new ReportManager(landmarkFile, trailFile);
			assertNotNull(manager);
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.ReportManager#getDistancesReport(java.lang.String)}.
	 */
	@Test
	public void testGetDistancesReport() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		ReportManager manager;
		try {
			manager = new ReportManager(landmarkFile, trailFile);
			assertNotNull(manager);
			String h = manager.getDistancesReport("L02");
			assertNotNull(h);
			
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
	}

	/**
	 * Test method for {@link edu.ncsu.csc316.trail.manager.ReportManager#getProposedFirstAidLocations(int)}.
	 */
	@Test
	public void testGetProposedFirstAidLocations() {
		String landmarkFile = "input/landmarks_sample.csv";
		String trailFile = "input/trails_sample.csv";
		
		ReportManager manager;
		try {
			manager = new ReportManager(landmarkFile, trailFile);
			assertNotNull(manager);
			String firstAid = manager.getProposedFirstAidLocations(3);
			assertNotNull(firstAid);
			String firstAid2 = manager.getProposedFirstAidLocations(2);
			assertNotNull(firstAid2);
		} catch (FileNotFoundException e) {
			fail("Failed to read input files.");
		}
	}

}
