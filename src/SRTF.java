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
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(SimProcess::getTimeRem)); // :: = key extractor
        // if we didn't have a getTimeRem() in SimProcess and fields were accessible,
        // then we could use: Comparator.comparingInt(p -> p.timeRem)

    }

    // Poll all CPUs, check availability, then status of current process
    // check if process finished or needs IO
    // check if processes can be bumped
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
            else { // active process, check if can be bumped
                int time_rem = p.getTimeRem();

                var ite = readyQueue.iterator();

                while (ite != null && ite.hasNext()) {
                    var pr = ite.next();
                    if (pr.getTimeRem() >= time_rem) break; // the queue doesn't have shorter remaining times
                    else { // there is a shorter remaining time, need to bump
                        cpu.setCurrentProcess(pr);
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
            cpu.setCurrentProcess(p);
            if (p.getResponseTime() < 0) { // first time in a CPU
                p.setResponseTime(time - p.getArrivalTime());
            }
        }
        else cpu.setCurrentProcess(null);

    }
}
