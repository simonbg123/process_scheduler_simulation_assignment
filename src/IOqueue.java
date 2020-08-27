/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class IOqueue {

    SimProcess currentlyProcessing;
    int timeRemaining;
    LinkedList<SimProcess> waitQueue;
    ArrayList<String> timeline;
    ArrayList<ArrayList<String>> waitqueueTimeline;


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
        if (p == null) timeline.add(Main.IDLE);
        else timeline.add(p.pid);

        // update IO wait-queue timeline
        ArrayList<String> current_pid_list = new ArrayList<>();
        for (SimProcess pr : waitQueue) {
            current_pid_list.add(pr.pid);
        }
        if (current_pid_list.isEmpty()) current_pid_list.add(Main.IDLE);
        waitqueueTimeline.add(current_pid_list);
    }

}
