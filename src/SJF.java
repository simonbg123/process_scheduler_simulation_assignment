/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.*;

public class SJF extends FCFS {


    public SJF(int n_cpus, ArrayList<SimProcess> process_list) {
        super(n_cpus, process_list);
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(p->p.timeRem));
        // if we had a getTimeRem() in SimProcess, then we could use: Comparator.comparingInt(SimProcess::getTimeRem)
    }
}
