/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.*;

public class SRTF extends SchedulingAlgorithm {


    public SRTF(int n_cpus, ArrayList<SimProcess> process_list) {

        super(n_cpus, process_list);
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.timeRem));
        // if we had a getTimeRem() in SimProcess, then we could use: Comparator.comparingInt(SimProcess::getTimeRem)

    }

    // Poll all CPUs, check availability, then status of current process
    // check if process finished or needs IO
    // check if processes can be bumped
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
                int time_rem = p.timeRem;

                var ite = readyQueue.iterator();

                while (ite != null && ite.hasNext()) {
                    var pr = ite.next();
                    if (pr.timeRem >= time_rem) break; // the queue doesn't have shorter remaining times
                    else { // there is a shorter remaining time, need to bump
                        cpu.currentProcess = pr;
                        ite.remove();
                        ite = null;
                        readyQueue.add(p); // p goes back to the queue
                    }
                }

            }
            // else, it is an active process. Will update at end of this iteration
        }
    }


    private void give_process_to_cpu(CPU cpu) {
        SimProcess p = readyQueue.poll();

        if (p != null) {
            cpu.currentProcess = p;
            if (p.responseTime < 0) { // first time in a CPU
                p.responseTime = time - p.arrivalTime;
            }
        }
        else cpu.currentProcess = null;

    }
}
