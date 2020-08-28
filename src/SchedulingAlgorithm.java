/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public abstract class SchedulingAlgorithm {

    public static final String IDLE = "___";

    protected List<SimProcess> processList;
    protected Queue<SimProcess> readyQueue;
    protected List<List<String>> readyQueueTimeline;
    protected IOqueue ioQueue;
    protected SimulationResult simResult;
    protected CPU[] cpuList;
    protected int time;

    /**
     * Note: the ready-queue construction is left to the extending class
     * @param process_list
     */
    public SchedulingAlgorithm(int n_cpus, ArrayList<SimProcess> process_list) {

        this.processList = process_list;
        readyQueueTimeline = new ArrayList<>();
        ioQueue = new IOqueue();
        simResult = new SimulationResult();
        cpuList = new CPU[n_cpus];
        time = 0;

        for (int i = 0; i < cpuList.length; ++i) {
            cpuList[i] = new CPU();
        }
    }

    /**
     * Poll all CPUs, check availability, then status of current processes
     * check if process finished or needs IO
     * and implements algorithm-specific behaviour
     */
    abstract protected void pollCPUs();

    public SimulationResult runProcesses() {

        while (!processList.isEmpty()) {

            // Poll all processes and queue those that arrive
            for (SimProcess p : processList) {
                if (p.getArrivalTime() == time) {
                    readyQueue.add(p);
                }

            }
            // Check IO queue for finished IO and place process in ready-queue
            if (ioQueue.getCurrentlyProcessing() != null) {
                ioQueue.decrementTimeRemaining();
                if (ioQueue.getTimeRemaining() < 0) {
                    readyQueue.add(ioQueue.getCurrentlyProcessing());
                    ioQueue.setCurrentlyProcessing(null);
                }
            }

            // Poll all CPUs, check availability, then status of current process
            // check if process finished or needs IO
            // and implements algorithm-specific behaviour
            pollCPUs();

            // if the last processes has been taken out, we want to exit the loop and not update values for the next time unit
            if (processList.isEmpty()) break;

            // check IO wait queue, move process for IO processing
            // ++timewaiting for all remaining in queue
            if (ioQueue.getCurrentlyProcessing() == null) {
                SimProcess p = ioQueue.pollWaitQueue();
                if (p != null) {
                    ioQueue.setCurrentlyProcessing(p);
                    ioQueue.setTimeRemaining(1);
                }
            } // note: no else: we already checked earlier if a process had finished its IO

            // increment waiting time for all processes in the IO waiting queue
            // if they're in the wait queu at this point, we know they will spend the next
            // cycle there
            for (SimProcess p : ioQueue.getWaitQueue()) {
                p.incrementTimeWaiting();
            }

            // update IO timeline and IO wait-queue timeline
            ioQueue.updateIoTimelines();


            // increment waiting time for all processes int the ready-queue
            // same reasoning as for the IO wait queue
            for (SimProcess p : readyQueue) {
                p.incrementTimeWaiting();
            }

            // update ready-queue timeline
            ArrayList<String> current_state = new ArrayList<>();
            for (SimProcess p : readyQueue) {
                current_state.add(p.getPid());
            }
            if (current_state.isEmpty()) current_state.add(IDLE);
            readyQueueTimeline.add(current_state);

            // update time remaining for processes on CPUs
            // as well as timeline
            for (CPU cpu : cpuList) {
                SimProcess p = cpu.getCurrentProcess();
                if (p != null) {
                    p.decrementTimeRem();
                    cpu.addToTimeline(p.getPid());
                }
                else {
                    cpu.addToTimeline(IDLE);
                }

            }


            ++time;
        }


        // get the cpu timelines
        for (CPU cpu : cpuList) {
            simResult.addCPUtimeline(cpu.getTimeline());
        }
        // add the ready-queue timeline
        simResult.setReadyqueueTimeline(readyQueueTimeline);
        // add IO timeline
        simResult.setIOtimeline(ioQueue.getTimeline());
        // add IO wait queue timeline
        simResult.setIOwaitqueueTimeline(ioQueue.getWaitqueueTimeline());

        // sort processes
        simResult.getProcesses().sort(Comparator.comparingInt(p -> Integer.parseInt(p.getPid().substring(1))));

        return simResult;
    }






}
