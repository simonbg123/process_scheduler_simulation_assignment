/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public abstract class SchedulingAlgorithm {

    ArrayList<SimProcess> processList;
    Queue<SimProcess> readyQueue;
    ArrayList<ArrayList<String>> readyQueueTimeline;
    IOqueue ioQueue;
    SimulationResult simResult;
    CPU[] cpuList;
    int time;

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
                if (p.arrivalTime == time) {
                    readyQueue.add(p);
                }

            }
            // Check IO queue for finished IO and place process in ready-queue
            if (ioQueue.currentlyProcessing != null) {
                ioQueue.timeRemaining -= 1;
                if (ioQueue.timeRemaining < 0) {
                    readyQueue.add(ioQueue.currentlyProcessing);
                    ioQueue.currentlyProcessing = null;
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
            if (ioQueue.currentlyProcessing == null) {
                SimProcess p = ioQueue.waitQueue.poll();
                if (p != null) {
                    ioQueue.currentlyProcessing = p;
                    ioQueue.timeRemaining = 1;
                }
            } // note: no else: we already checked earlier if a process had finished its IO

            // increment waiting time for all processes in the IO waiting queue
            // if they're in the wait queu at this point, we know they will spend the next
            // cycle there
            for (SimProcess p : ioQueue.waitQueue) {
                ++p.timeWaiting;
            }

            // update IO timeline and IO wait-queue timeline
            ioQueue.update_io_timelines();


            // increment waiting time for all processes int the ready-queue
            // same reasoning as for the IO wait queue
            for (SimProcess p : readyQueue) {
                ++p.timeWaiting;
            }

            // update ready-queue timeline
            ArrayList<String> current_state = new ArrayList<>();
            for (SimProcess p : readyQueue) {
                current_state.add(p.pid);
            }
            if (current_state.isEmpty()) current_state.add(Main.IDLE);
            readyQueueTimeline.add(current_state);

            // update time remaining for processes on CPUs
            // as well as timeline
            for (CPU cpu : cpuList) {
                SimProcess p = cpu.currentProcess;
                if (p != null) {
                    --cpu.currentProcess.timeRem;
                    cpu.timeline.add(p.pid);
                }
                else {
                    cpu.timeline.add(Main.IDLE);
                }

            }


            ++time;
        }


        // get the cpu timelines
        for (CPU cpu : cpuList) {
            simResult.cpuTimelines.add(cpu.timeline);
        }
        // add the ready-queue timeline
        simResult.readyqueueTimeline = readyQueueTimeline;
        // add IO timeline
        simResult.ioTimeline = ioQueue.timeline;
        // add IO wait queue timeline
        simResult.ioWaitqueueTimeline = ioQueue.waitqueueTimeline;

        // sort processes
        Collections.sort(simResult.processes, (SimProcess s1, SimProcess s2)->Integer.parseInt(s1.pid.substring(1)) - (Integer.parseInt(s2.pid.substring(1))));
        return simResult;
    }






}
