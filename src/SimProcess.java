/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.LinkedList;

/**
 * Simulates a running process
 */
public class SimProcess {

    String pid;
    int arrivalTime;
    int execTime;  // total cpu time required by the process
    LinkedList<Integer> IOrequests;  // times of IO requests to be made by the process, in relation to the execTime
    int timeRem;  // current remaining CPU time required by the process
    int timeWaiting;
    int turnaround;
    int responseTime;


    public SimProcess(String pid, int arrival_time, int exec_time, LinkedList<Integer> IOrequests) {
        this.pid = pid;
        this.arrivalTime = arrival_time;
        this.execTime = exec_time;
        this.IOrequests = IOrequests;
        this.timeRem = exec_time;
        timeWaiting = 0;
        turnaround = 0;
        responseTime = -1;
    }

    public int getTimeRem() {
        return timeRem;
    }
}
