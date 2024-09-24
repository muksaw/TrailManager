package edu.ncsu.csc316.trail.manager;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;
import edu.ncsu.csc316.trail.io.TrailInputReader;

import java.io.FileNotFoundException;

/**
 * This is the TrailManager which allows for the user to determine how far they are from destinations
 * based on an input origin, get landmarks by their name, and determine the best first aid locations
 * based on the number of intersecting trails a landmark has.
 *
 * @author Mukul Sauhta
 */
public class TrailManager {
	/** this is the list of landmarks */
    private List<Landmark> landmarks;
    /** this is the list of trails */
    private List<Trail> trails;

    /**
     * This is the constructor for the TrailManager, which requires an input path to the landmark file,
     * and  input path to the trail file.
     *
     * @param pathToLandmarkFile the file containing landmarks.
     * @param pathToTrailFile    the file containing trails.
     * @throws FileNotFoundException if there is no file found on either parameter.
     */
    public TrailManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
    	DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
    	DSAFactory.setMapType(DataStructure.SKIPLIST);
        this.landmarks = TrailInputReader.readLandmarks(pathToLandmarkFile);
        this.trails = TrailInputReader.readTrails(pathToTrailFile);
    }

    /**
     * This method gets the distances to the destinations from a specific landmark by fetching it using
     * its ID, and then determining the distance between all possible trails that it can access from that point.
     *
     * @param originLandmark the origin we start from.
     * @return a map of all the landmarks which are accessible from the origin, followed by their distance from the
     * origin.
     */
    public Map<Landmark, Integer> getDistancesToDestinations(String originLandmark) {
        Landmark originLandmarkObject = getLandmarkByID(originLandmark);
        Map<Landmark, Integer> distances = DSAFactory.getMap(null);

        //set all distances to max value
        for (Landmark l : landmarks) {
            if (l.equals(originLandmarkObject)) {
                distances.put(l, 0);
            } else {
                distances.put(l, Integer.MAX_VALUE);
            }
        }

        //recursively explore all adjacent landmarks
        exploreAdjacentLandmarks(originLandmarkObject, distances);

        //remove landmarks that are not accessible
        for (Landmark l : landmarks) {
        	if (l.equals(originLandmarkObject)) {
                distances.remove(l);
            } else if (distances.get(l) == Integer.MAX_VALUE) {
                distances.remove(l);
            }
        }

        return distances;
    }
    
    /**
     * This is a helper method which utilizes recursion to exlore the adjacentt landmarks.
     * @param currentLandmark the landmark we are currently on 
     * @param distances the distances in a map.
     */
    private void exploreAdjacentLandmarks(Landmark currentLandmark, Map<Landmark, Integer> distances) {
        for (Trail t : trails) {
            Landmark otherLandmark = null;
            if (t.getLandmarkOne() != null && currentLandmark != null && t.getLandmarkOne().equals(currentLandmark.getId())) {
                otherLandmark = getLandmarkByID(t.getLandmarkTwo());
            } else if (t.getLandmarkTwo() != null && currentLandmark != null && t.getLandmarkTwo().equals(currentLandmark.getId())) {
                otherLandmark = getLandmarkByID(t.getLandmarkOne());
            }

            if (otherLandmark != null) {
                int newDistance = distances.get(currentLandmark) + t.getLength();
                if (newDistance < distances.get(otherLandmark)) {
                    distances.put(otherLandmark, newDistance);
                    //explore all adjacent landmarks recursively
                    exploreAdjacentLandmarks(otherLandmark, distances);
                }
            }
        }
    }

    /**
     * This method allows for the landmark to be fetched given its ID.
     *
     * @param landmarkID the ID of the landmark that the method fetches.
     * @return The landmark based on the ID parameter.
     */
    public Landmark getLandmarkByID(String landmarkID) {
        for (Landmark l : landmarks) {
            if (l.getId().equals(landmarkID)) {
                return l;
            }
        }
        return null;
    }

    /**
     * This method gets the proposed first aid locations given a minimum number of intersecting trails on the
     * landmark necessary to be a first aid location, and then returns a map of all the landmarks (key) which has
     * the minimum # of intersecting trails (value).
     *
     * @param numberOfIntersectingTrails the minimum number of intersecting trails the user inputs or a proposed
     *                                   first aid location.
     * @return the map of all landmarks that can be a proposed first aid location.
     */
    public Map<Landmark, List<Trail>> getProposedFirstAidLocations(int numberOfIntersectingTrails) {
    	
        Map<Landmark, List<Trail>> proposedFirstAidLocations = DSAFactory.getMap(null);
        for (Trail trail : trails) {
            Landmark one = getLandmarkByID(trail.getLandmarkOne());
            if (proposedFirstAidLocations.get(one) == null) {
                List<Trail> trailList = DSAFactory.getIndexedList();
                proposedFirstAidLocations.put(one, trailList);
            }
            proposedFirstAidLocations.get(one).addLast(trail);

            Landmark two = getLandmarkByID(trail.getLandmarkTwo());
            if (proposedFirstAidLocations.get(two) == null) {
                List<Trail> trailList = DSAFactory.getIndexedList();
                proposedFirstAidLocations.put(two, trailList);
            }
            proposedFirstAidLocations.get(two).addLast(trail);
        }
        //remove all landmarks that do not have the minimum number of intersecting trails
        for (Landmark l : landmarks) {
            if (proposedFirstAidLocations.get(l) != null && proposedFirstAidLocations.get(l).size() < numberOfIntersectingTrails) {
                proposedFirstAidLocations.remove(l);
            }
        }
        return proposedFirstAidLocations;
    }
}
