/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IOqueue {

    private SimProcess currentlyProcessing;
    private int timeRemaining;
    private LinkedList<SimProcess> waitQueue;
    private List<String> timeline;
    private List<List<String>> waitqueueTimeline;


    public IOqueue() {
        currentlyProcessing = null;
        timeRemaining = 0;
        waitQueue = new LinkedList<>();
        timeline = new ArrayList<>();
        waitqueueTimeline = new ArrayList<>();
    }

    public void update_io_timelines() {

        // update the IO timeline
        SimProcess p = currentlyProcessing;
        if (p == null) timeline.add(SchedulingAlgorithm.IDLE);
        else timeline.add(p.getPid());

        // update IO wait-queue timeline
        ArrayList<String> current_pid_list = new ArrayList<>();
        for (SimProcess pr : waitQueue) {
            current_pid_list.add(pr.getPid());
        }
        if (current_pid_list.isEmpty()) current_pid_list.add(SchedulingAlgorithm.IDLE);
        waitqueueTimeline.add(current_pid_list);
    }

    public SimProcess getCurrentlyProcessing() {
        return currentlyProcessing;
    }

    public void setCurrentlyProcessing(SimProcess currentlyProcessing) {
        this.currentlyProcessing = currentlyProcessing;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public void decrementTimeRemaining() {
        --timeRemaining;
    }

    public SimProcess pollWaitQueue() {
        return waitQueue.poll();
    }

    public void addToWaitQueue(SimProcess p) {
        waitQueue.add(p);
    }

    public List<SimProcess> getWaitQueue() {
        return waitQueue;
    }

    public List<String> getTimeline() {
        return timeline;
    }

    public List<List<String>> getWaitqueueTimeline() {
        return waitqueueTimeline;
    }

}
