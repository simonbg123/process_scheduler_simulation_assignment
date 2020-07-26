import java.util.*;

public class SRTF extends SchedulingAlgorithm {


    public SRTF(ArrayList<SimProcess> process_list) {

        super(process_list);
        ready_queue = new PriorityQueue<>(Comparator.comparingInt(SimProcess::getTime_rem));

    }

    // Poll all CPUs, check availability, then status of current process
    // check if process finished or needs IO
    // check if processes can be bumped
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
            else { // active process, check if can be bumped
                int time_rem = p.time_rem;

                var ite = ready_queue.iterator();

                while (ite != null && ite.hasNext()) {
                    var pr = ite.next();
                    if (pr.time_rem >= time_rem) break; // the queue doesn't have shorter remaining times
                    else { // there is a shorter remaining time, need to bump
                        cpu.currentProcess = pr;
                        ite.remove();
                        ite = null;
                        ready_queue.add(p); // p goes back to the queue
                    }
                }

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
