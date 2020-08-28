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

    private List<List<String>> cpuTimelines;
    private List<List<String>> readyqueueTimeline;
    private List<String> ioTimeline;
    private List<List<String>> ioWaitqueueTimeline;
    private List<SimProcess> processes;

    public SimulationResult() {
        cpuTimelines = new ArrayList<>();
        readyqueueTimeline = new ArrayList<>();
        ioTimeline = new ArrayList<>();
        ioWaitqueueTimeline = new ArrayList<>();
        processes = new ArrayList<>();
    }

    public int getNumberOfCPUs() {
        return cpuTimelines.size();
    }

    public List<String> getCPUtimeline(int i) {
        return cpuTimelines.get(i);
    }

    public void addCPUtimeline(List<String> timeline) {
        cpuTimelines.add(timeline);
    }

    public List<List<String>> getReadyqueueTimeline() {
        return readyqueueTimeline;
    }

    public List<String> getIOtimeline() {
        return ioTimeline;
    }

    public List<List<String>> getIOwaitqueueTimeline() {
        return ioWaitqueueTimeline;
    }

    public List<SimProcess> getProcesses() {
        return processes;
    }

    public void addProcess(SimProcess p) {
        processes.add(p);
    }

    public void setReadyqueueTimeline(List<List<String>> readyqueueTimeline) {
        this.readyqueueTimeline = readyqueueTimeline;
    }

    public void setIOtimeline(List<String> ioTimeline) {
        this.ioTimeline = ioTimeline;
    }

    public void setIOwaitqueueTimeline(List<List<String>> ioWaitqueueTimeline) {
        this.ioWaitqueueTimeline = ioWaitqueueTimeline;
    }
}
