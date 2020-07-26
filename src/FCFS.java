/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.*;

public class FCFS extends SchedulingAlgorithm {

    public FCFS(ArrayList<SimProcess> process_list) {

        super(process_list);
        ready_queue = new LinkedList<>();

    }

    protected void poll_cpus() {
        for (CPU cpu : cpu_list) {
            // check if CPU is currently free at the beginning of this cycle
            SimProcess p = cpu.currentProcess;
            if (p == null) {
                give_process_to_cpu(cpu);
            }
            else if (p.time_rem == 0){
                p.turnaround = time - p.arrival_time; // will be removed from active list when next polled
                sim_result.processes.add(p);
                process_list.remove(p);
                // check if the ending process was the last one remaining
                if (process_list.isEmpty()) {
                    break;
                }
                // try to replace the process with one from the ready-queue
                cpu.currentProcess = null;
                give_process_to_cpu(cpu);
            }
            else if (!p.IOrequests.isEmpty() && p.exec_time - p.time_rem == p.IOrequests.peek()) { // requests OI
                p.IOrequests.removeFirst();
                io_queue.wait_queue.add(p); // may still be moved to io processing during this cycle
                cpu.currentProcess = null;
                give_process_to_cpu(cpu);
            }
            // else, it is an active process. Will update at end of this iteration
        }
    }

    private void give_process_to_cpu(CPU cpu) {
        SimProcess p = ready_queue.poll();

        if (p != null) {
            cpu.currentProcess = p;
            if (p.response_time < 0) { // first time in a CPU
                p.response_time = time - p.arrival_time;
            }
        }
        else cpu.currentProcess = null;

    }
}
