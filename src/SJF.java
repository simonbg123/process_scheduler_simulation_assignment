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
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(SimProcess::getTimeRem)); // :: = key extractor
        // if we had no getTimeRem() in SimProcess and timeRem was accessible,
        // then we could use: Comparator.comparingInt(p->p.timeRem)
    }
}
