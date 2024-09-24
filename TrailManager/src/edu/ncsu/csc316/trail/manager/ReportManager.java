package edu.ncsu.csc316.trail.manager;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;
import edu.ncsu.csc316.dsa.sorter.MergeSorter;
import edu.ncsu.csc316.trail.data.Landmark;
import edu.ncsu.csc316.trail.data.Trail;
import edu.ncsu.csc316.trail.dsa.Algorithm;
import edu.ncsu.csc316.trail.dsa.DSAFactory;
import edu.ncsu.csc316.trail.dsa.DataStructure;

import java.io.FileNotFoundException;
import java.util.Comparator;

/**
 * This class utilizes the TrailManager to report the distances from an origin
 * and the proposed first aid locations to output in the GUI.
 *
 * @author Mukul Sauhta
 */
public class ReportManager {
	/** the trail manager field*/
    private TrailManager trailManager;

    /**
     * This is the constructor for ReportManager which takes an input path to a landmark file and
     * a trail file.
     *
     * @param pathToLandmarkFile the file path to the landmark file
     * @param pathToTrailFile    the file path to the trail file.
     * @throws FileNotFoundException if neither file exists.
     */
    public ReportManager(String pathToLandmarkFile, String pathToTrailFile) throws FileNotFoundException {
        DSAFactory.setMapType(DataStructure.SKIPLIST);
        DSAFactory.setListType(DataStructure.SINGLYLINKEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);

        this.trailManager = new TrailManager(pathToLandmarkFile, pathToTrailFile);
    }

    /**
     * This gets a report of the distances from an origin and returns it as a string.
     *
     * @param originLandmark the origin landmark.
     * @return the string containing all of the accessible landmarks from the origin
     * and their respective distances from the origin landmark.
     */
    public String getDistancesReport(String originLandmark) {
        Map<Landmark, Integer> distances = this.trailManager.getDistancesToDestinations(originLandmark);
        @SuppressWarnings("unchecked")
		Entry<Landmark, Integer>[] entries = new Entry[distances.size()];
        int index  = 0;
        for(Entry<Landmark, Integer> e : distances.entrySet()) {
        	entries[index] = e;
        	index++;
        }
        //create custom comparator so that we can assign by first amount of intersecting trails, and then 
        //alphabetically.
        Comparator<Entry<Landmark, Integer>> comparator = new Comparator<Entry<Landmark, Integer>>() {
            @Override
            public int compare(Entry<Landmark, Integer> e1, Entry<Landmark, Integer> e2) {
                int valueCompare = Integer.compare(e1.getValue(), e2.getValue());
                if (valueCompare == 0) {
                    return e1.getKey().getDescription().compareTo(e2.getKey().getDescription()); 
                }
                return valueCompare;
            }
        };
        String description;
        if(trailManager.getLandmarkByID(originLandmark) == null) {
        	return "The provided landmark ID (" + originLandmark + ") is invalid for the park.";
        } else {
        	description = trailManager.getLandmarkByID(originLandmark).getDescription();
        }
        MergeSorter<Entry<Landmark, Integer>> mergeSort = new MergeSorter<Entry<Landmark, Integer>>(comparator);
        mergeSort.sort(entries);
        if(entries.length == 0) {
        	return "No landmarks are reachable from " + trailManager.getLandmarkByID(originLandmark).getDescription() +
        	 	   " (" + originLandmark + ").";
        }
        String f = "Landmarks Reachable from " + description + " (" + originLandmark + ") {\n";
        for(int i = 0; i < entries.length; i++) {
        	if(entries[i].getValue() > 5280) {
        	    double miles = entries[i].getValue() / 5280.0;
        	    f += String.format("   %d feet (%.2f miles) to %s (%s)\n",
        	            entries[i].getValue(), miles, entries[i].getKey().getDescription(), entries[i].getKey().getId());
        	} else {
        	    f += String.format("   %d feet to %s (%s)\n",
        	            entries[i].getValue(), entries[i].getKey().getDescription(), entries[i].getKey().getId());
        	}
        	  
        }
        f += "}";
        return f;
        
    }

    /**
     * This gets the proposed first aid locations based on the minimum number of intersecting
     * trails on a landmark, and then returns it as a string.
     *
     * @param numberOfIntersectingTrails the min number of intersecting trails.
     * @return the string containing all first aid locations which meet the min number of
     * intersecting trails.
     */
    public String getProposedFirstAidLocations(int numberOfIntersectingTrails) {
    	if(numberOfIntersectingTrails <= 0) return "Number of intersecting trails must be greater than 0.";
    	if(numberOfIntersectingTrails >= 1000000000) return "No landmarks have at least 1000000000 intersecting trails.";
        Map<Landmark, List<Trail>> trailsForProposal = this.trailManager.getProposedFirstAidLocations(numberOfIntersectingTrails);
        @SuppressWarnings("unchecked")
		Entry<Landmark, List<Trail>>[] entryArray = new Entry[trailsForProposal.size()];
        int index = 0;
        //now we have to iterate through the entry set and add each entry to the array so we can sort it.
        //for each entry in trailsforproposal, we assign to the array
        for(Entry<Landmark, List<Trail>> e : trailsForProposal.entrySet()) {
        	entryArray[index++] = e;
        }
        //create custom comparator so that we can assign by first amount of intersecting trails, and then 
        //alphabetically.
        Comparator<Entry<Landmark, List<Trail>>> comparator = new Comparator<Entry<Landmark, List<Trail>>>() {
            @Override
            public int compare(Entry<Landmark, List<Trail>> e1, Entry<Landmark, List<Trail>> e2) {
                int valueCompare = Integer.compare(e2.getValue().size(), e1.getValue().size());
                if (valueCompare == 0) {
                    return e1.getKey().getDescription().compareTo(e2.getKey().getDescription()); 
                }
                return valueCompare;
            }
        };
        MergeSorter<Entry<Landmark, List<Trail>>> mergeSort = new MergeSorter<Entry<Landmark, List<Trail>>>(comparator);
        mergeSort.sort(entryArray);
        String f = "Proposed Locations for First Aid Stations {\n";
        for(int i = 0; i < entryArray.length; i++) {
        	f += "   " + entryArray[i].getKey().getDescription() + " (" + entryArray[i].getKey().getId() + ") - " 
        	+ entryArray[i].getValue().size() + " intersecting trails\n"; 
        }
        f += "}";
        return f;
    }

}
