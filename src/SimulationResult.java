/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;

public class SimulationResult {

    ArrayList<ArrayList<String>> cpu_timelines;
    ArrayList<ArrayList<String>> readyqueue_timeline;
    ArrayList<String> IO_timeline;
    ArrayList<ArrayList<String>> IO_waitqueue_timeline;
    ArrayList<SimProcess> processes;

    public SimulationResult() {
        cpu_timelines = new ArrayList<>();
        readyqueue_timeline = new ArrayList<>();
        IO_timeline = new ArrayList<>();
        IO_waitqueue_timeline = new ArrayList<>();
        processes = new ArrayList<>();
    }
}
