/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;

/**
 * Contains the data structures needed to output the result of a simulation
 */
public class SimulationResult {

    ArrayList<ArrayList<String>> cpuTimelines;
    ArrayList<ArrayList<String>> readyqueueTimeline;
    ArrayList<String> ioTimeline;
    ArrayList<ArrayList<String>> ioWaitqueueTimeline;
    ArrayList<SimProcess> processes;

    public SimulationResult() {
        cpuTimelines = new ArrayList<>();
        readyqueueTimeline = new ArrayList<>();
        ioTimeline = new ArrayList<>();
        ioWaitqueueTimeline = new ArrayList<>();
        processes = new ArrayList<>();
    }
}
