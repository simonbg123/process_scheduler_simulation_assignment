/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.*;

public class FCFS extends SchedulingAlgorithm {

    public FCFS(int nCpus, ArrayList<SimProcess> processList) {

        super(nCpus, processList);
        readyQueue = new LinkedList<>();

    }

    protected void pollCPUs() {
        for (CPU cpu : cpuList) {
            // check if CPU is currently free at the beginning of this cycle
            SimProcess p = cpu.getCurrentProcess();
            if (p == null) {
                give_process_to_cpu(cpu);
            }
            else if (p.getTimeRem() == 0){
                p.setTurnaround(time - p.getArrivalTime()); // will be removed from active list when next polled
                simResult.addProcess(p);
                processList.remove(p);
                // check if the ending process was the last one remaining
                if (processList.isEmpty()) {
                    break;
                }
                // try to replace the process with one from the ready-queue
                cpu.setCurrentProcess(null);
                give_process_to_cpu(cpu);
            }
            else if (p.hasIOrequests() && p.getExecTime() - p.getTimeRem() == p.peekNextIOrequest()) { // requests OI
                p.removeFirstIOrequest();
                ioQueue.addToWaitQueue(p); // may still be moved to io processing during this cycle
                cpu.setCurrentProcess(null);
                give_process_to_cpu(cpu);
            }
            // else, it is an active process. Will update at end of this iteration
        }
    }

    private void give_process_to_cpu(CPU cpu) {
        SimProcess p = readyQueue.poll();

        if (p != null) {
            cpu.setCurrentProcess(p);
            if (p.getResponseTime() < 0) { // first time in a CPU
                p.setResponseTime(time - p.getArrivalTime());
            }
        }
        else cpu.setCurrentProcess(null);

    }
}
