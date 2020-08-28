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

    private String pid;
    private int arrivalTime;
    private int execTime;  // total cpu time required by the process
    private LinkedList<Integer> IOrequests;  // times of IO requests to be made by the process, in relation to the execTime
    private int timeRem;  // current remaining CPU time required by the process
    private int timeWaiting;
    private int turnaround;
    private int responseTime;


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

    public String getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getExecTime() {
        return execTime;
    }

    public boolean hasIOrequests() {
        return !IOrequests.isEmpty();
    }

    public int peekNextIOrequest() {
        return IOrequests.peek();
    }

    public void removeFirstIOrequest() {
        IOrequests.removeFirst();
    }

    public int getTimeRem() {
        return timeRem;
    }

    public void decrementTimeRem() {
        --timeRem;
    }

    public int getTimeWaiting() {
        return timeWaiting;
    }

    public void incrementTimeWaiting() {
        ++timeWaiting;
    }

    public int getTurnaround() {
        return turnaround;
    }

    public void setTurnaround(int turnaround) {
        this.turnaround = turnaround;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }


}
