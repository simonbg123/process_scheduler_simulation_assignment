/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the data structures needed to output the result of a simulation
 */
public class SimulationResult {

    List<ArrayList<String>> cpuTimelines;
    List<ArrayList<String>> readyqueueTimeline;
    List<String> ioTimeline;
    List<ArrayList<String>> ioWaitqueueTimeline;
    List<SimProcess> processes;

    public SimulationResult() {
        cpuTimelines = new ArrayList<>();
        readyqueueTimeline = new ArrayList<>();
        ioTimeline = new ArrayList<>();
        ioWaitqueueTimeline = new ArrayList<>();
        processes = new ArrayList<>();
    }
}
