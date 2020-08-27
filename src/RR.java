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
            SimProcess p = cpu.currentProcess;
            if (p == null) {
                give_process_to_cpu(cpu);
            }
            else if (p.timeRem == 0){
                p.turnaround = time - p.arrivalTime; // will be removed from active list when next polled
                simResult.processes.add(p);
                processList.remove(p);
                // check if the ending process was the last one remaining
                if (processList.isEmpty()) {
                    break;
                }
                // try to replace the process with one from the ready-queue
                cpu.currentProcess = null;
                give_process_to_cpu(cpu);
            }
            else if (!p.IOrequests.isEmpty() && p.execTime - p.timeRem == p.IOrequests.peek()) { // requests OI
                p.IOrequests.removeFirst();
                ioQueue.waitQueue.add(p); // may still be moved to io processing during this cycle
                cpu.currentProcess = null;
                give_process_to_cpu(cpu);
            }
            else { // active process, check if can be bumped
                if (cpu.currentTimeslice == Main.quantum) {
                    cpu.currentProcess = null;
                    readyQueue.add(p); // if p is alone in the queue, it may come back for another slice
                    give_process_to_cpu(cpu);
                }
                else ++cpu.currentTimeslice;

            }

        }

    }

    private void give_process_to_cpu(CPU cpu) {
        SimProcess p = readyQueue.poll();

        if (p != null) {
            cpu.currentProcess = p;
            if (p.responseTime < 0) { // first time in a CPU
                p.responseTime = time - p.arrivalTime;
            }
            cpu.currentTimeslice = 1;
        }
        else {
            cpu.currentProcess = null;
            cpu.currentTimeslice = 0;
        }

    }
}
