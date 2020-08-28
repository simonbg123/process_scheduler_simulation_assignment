/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.*;

public class RR extends SchedulingAlgorithm {

    public RR(int n_cpus, ArrayList<SimProcess> process_list) {

        super(n_cpus, process_list);
        readyQueue = new LinkedList<>();

    }

    // Poll all CPUs, check availability, then status of current process
    // check if process finished or needs IO
    // check if time slices have expired
    protected void pollCPUs() {

        for (CPU cpu : cpuList) {
            // check if CPU is currently free at the beginning of this cycle
            SimProcess p = cpu.getCurrentProcess();
            if (p == null) {
                giveProcessToCPU(cpu);
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
                giveProcessToCPU(cpu);
            }
            else if (p.hasIOrequests() && p.getExecTime() - p.getTimeRem() == p.peekNextIOrequest()) { // requests OI
                p.removeFirstIOrequest();
                ioQueue.addToWaitQueue(p); // may still be moved to io processing during this cycle
                cpu.setCurrentProcess(null);
                giveProcessToCPU(cpu);
            }
            else { // active process, check if can be bumped
                if (cpu.getCurrentTimeslice() == Main.quantum) {
                    cpu.setCurrentProcess(null);
                    readyQueue.add(p); // if p is alone in the queue, it may come back for another slice
                    giveProcessToCPU(cpu);
                }
                else cpu.incrementCurrentTimeslice();

            }

        }

    }

    private void giveProcessToCPU(CPU cpu) {
        SimProcess p = readyQueue.poll();

        if (p != null) {
            cpu.setCurrentProcess(p);
            if (p.getResponseTime() < 0) { // first time in a CPU
                p.setResponseTime(time - p.getArrivalTime());
            }
            cpu.setCurrentTimeslice(1);
        }
        else {
            cpu.setCurrentProcess(null);
            cpu.setCurrentTimeslice(0);
        }

    }
}
