import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class FCFS implements SchedulingAlgorithm {

    LinkedList<SimProcess> ready_queue;
    ArrayList<ArrayList<String>> ready_queue_timeline;
    IOqueue io_queue;
    SimulationResult sim_result;
    CPU[] cpu_list;
    int time;

    public FCFS() {
        ready_queue = new LinkedList<>();
        ready_queue_timeline = new ArrayList<>();
        io_queue = new IOqueue();
        sim_result = new SimulationResult();
        cpu_list = new CPU[Simulation.n_cpus];
        time = 0;

        for (int i = 0; i < cpu_list.length; ++i) {
            cpu_list[i] = new CPU();
        }
    }

    @Override
    public SimulationResult run_processes(ArrayList<SimProcess> process_list) {

        outerloop:
        while (!process_list.isEmpty()) {

            Iterator<SimProcess> iterator = process_list.iterator(); // get a fresh iterator
            // Poll all processes and queue those that arrive
            while (iterator.hasNext()) {
                SimProcess p = iterator.next();
                if (p.arrival_time == time) {
                    ready_queue.add(p);
                }
                else if (p.turnaround > 0) { // process has finished
                    sim_result.processes.add(p);
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
                    // check if the ending process was the last one remaining
                    if (process_list.size() == 1) {
                        sim_result.processes.add(p);
                        process_list.remove(p);
                        break outerloop;
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

            // check IO wait queue, move process for IO processing
            // ++timewaiting for all remaining in queue
            if (io_queue.currentlyProcessing == null) {
                SimProcess p = io_queue.wait_queue.poll();
                if (p != null) {
                    io_queue.currentlyProcessing = p;
                    io_queue.time_remaining = 1;
                }
            } // note: no else: we already checked earlier if a process had finished its IO

            // increment waiting time for all processes in the IO waiting queue
            // if they're in the wait queu at this point, we know they will spend the next
            // cycle there
            for (SimProcess p : io_queue.wait_queue) {
                ++p.time_waiting;
            }

            // update IO timeline and IO wait-queue timeline
            io_queue.update_io_timelines();


            // increment waiting time for all processes int the ready-queue
            // same reasoning as for the IO wait queue
            for (SimProcess p : ready_queue) {
                ++p.time_waiting;
            }

            // update ready-queue timeline
            ArrayList<String> current_state = new ArrayList<>();
            for (SimProcess p : ready_queue) {
                current_state.add(p.pid);
            }
            if (current_state.isEmpty()) current_state.add(Simulation.IDLE);
            ready_queue_timeline.add(current_state);

            // update time remaining for processes on CPUs
            // as well as timeline
            for (CPU cpu : cpu_list) {
                SimProcess p = cpu.currentProcess;
                if (p != null) {
                    --cpu.currentProcess.time_rem;
                    cpu.timeline.add(p.pid);
                }
                else {
                    cpu.timeline.add(Simulation.IDLE);
                }

            }


            ++time;
        }


        // get the cpu timelines
        for (CPU cpu : cpu_list) {
            sim_result.cpu_timelines.add(cpu.timeline);
        }
        // add the ready-queue timeline
        sim_result.readyqueue_timeline = ready_queue_timeline;
        // add IO timeline
        sim_result.IO_timeline = io_queue.timeline;
        // add IO wait queue timeline
        sim_result.IO_waitqueue_timeline = io_queue.waitqueue_timeline;


        return sim_result;
    }

    /**
     * Tries to give a process to a CPU, and updates the SimProcess stats
     * as well as the CPU timeline
     */
    void give_process_to_cpu(CPU cpu) {
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
