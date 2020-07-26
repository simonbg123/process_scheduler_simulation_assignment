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
    int time_remaining;
    LinkedList<SimProcess> wait_queue;
    ArrayList<String> timeline;
    ArrayList<ArrayList<String>> waitqueue_timeline;


    public IOqueue() {
        currentlyProcessing = null;
        time_remaining = 0;
        wait_queue = new LinkedList<>();
        timeline = new ArrayList<>();
        waitqueue_timeline = new ArrayList<>();
    }

    public void update_io_timelines() {

        // update the IO timeline
        SimProcess p = currentlyProcessing;
        if (p == null) timeline.add(Simulation.IDLE);
        else timeline.add(p.pid);

        // update IO wait-queue timeline
        ArrayList<String> current_pid_list = new ArrayList<>();
        for (SimProcess pr : wait_queue) {
            current_pid_list.add(pr.pid);
        }
        if (current_pid_list.isEmpty()) current_pid_list.add(Simulation.IDLE);
        waitqueue_timeline.add(current_pid_list);
    }

}
