import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class FCFS implements SchedulingAlgorithm {

    LinkedList<SimProcess> ready_queue;
    IOqueue io_queue;
    SimulationResult sim_result;
    CPU[] cpu_list;
    int time;

    public FCFS() {
        ready_queue = new LinkedList<>();
        io_queue = new IOqueue();
        sim_result = new SimulationResult();
        cpu_list = new CPU[Simulation.n_cpus];
        time = 0;
    }

    @Override
    public SimulationResult run_processes(ArrayList<SimProcess> process_list) {

        Iterator<SimProcess> iterator = process_list.iterator();
        while (!process_list.isEmpty()) {

            // Poll all processes and queue those that arrive
            while (iterator.hasNext()) {
                SimProcess p = iterator.next();
                if (p.arrival_time == time) {
                    ready_queue.add(p);
                }
                else if (p.turnaround > 0) { // process has finished
                    iterator.remove();
                }
            }
            // Check IO queue for finished IO and place process in ready-queue
            if (io_queue.currentlyProcessing != null) {
                io_queue.time_remaining -= 1;
                if (io_queue.time_remaining < 0) {
                    ready_queue.add(io_queue.currentlyProcessing);
                    io_queue.currentlyProcessing = null;
                }
            }

            // Poll all CPUs, check availability, then status of current process
            // check if process finished or needs IO
            for (CPU cpu : cpu_list) {
                // check if CPU is currently free at the beginning of this cycle
                SimProcess p = cpu.currentProcess;
                if (p == null) {
                    give_process_to_cpu(cpu);
                }
                else if (p.time_rem == 0){
                    p.turnaround = time - p.arrival_time; // will be removed from active list when next polled
                    // try to replace the process with one from the ready-queue
                    give_process_to_cpu(cpu);
                }
                else if (p.exec_time - p.time_rem == p.IOrequests.peek()) { // requests OI
                    p.IOrequests.removeFirst();
                    io_queue.wait_queue.add(p); // may still be moved to io processing during this cycle
                    give_process_to_cpu(cpu);
                }
                else { // active process
                    --p.time_rem;
                    cpu.timeline.add(p.pid);
                }
            }

            // check IO wait queue, move process for IO processing
            // ++timewaiting for all remaining in queue
            // print current pid to IO timeline - add to IOqueue class
            // print current wait queue to IO wait queue timeline - add to class

            // loop through ready-queue: ++timewaiting for all




            ++time;
        }





        return null;
    }

    /**
     * Tries to give a process to a CPU, and updates the SimProcess stats
     * as well as the CPU timeline
     */
    void give_process_to_cpu(CPU cpu) {
        SimProcess p = ready_queue.poll();

        if (p != null) {
            cpu.currentProcess = p;
            cpu.timeline.add(p.pid);
            p.time_rem -= 1;
            if (p.response_time < 0) { // first time in a CPU
                p.response_time = time - p.arrival_time;
            }
        }
        else cpu.timeline.add(Simulation.IDLE);
    }
}
