import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

abstract class SchedulingAlgorithm {

    ArrayList<SimProcess> process_list;
    Queue<SimProcess> ready_queue;
    ArrayList<ArrayList<String>> ready_queue_timeline;
    IOqueue io_queue;
    SimulationResult sim_result;
    CPU[] cpu_list;
    int time;

    /**
     * Note: the ready-queue construction is left to the extending class
     * @param process_list
     */
    public SchedulingAlgorithm(ArrayList<SimProcess> process_list) {

        this.process_list = process_list;
        ready_queue_timeline = new ArrayList<>();
        io_queue = new IOqueue();
        sim_result = new SimulationResult();
        cpu_list = new CPU[Simulation.n_cpus];
        time = 0;

        for (int i = 0; i < cpu_list.length; ++i) {
            cpu_list[i] = new CPU();
        }
    }

    /**
     * Poll all CPUs, check availability, then status of current processes
     * check if process finished or needs IO
     * and implements algorithm-specific behaviour
     */
    abstract protected void poll_cpus();

    public SimulationResult run_processes() {

        while (!process_list.isEmpty()) {

            // Poll all processes and queue those that arrive
            for (SimProcess p : process_list) {
                if (p.arrival_time == time) {
                    ready_queue.add(p);
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
            // and implements algorithm-specific behaviour
            poll_cpus();

            // if the last processes has been taken out, we want to exit the loop and not update values for the next time unit
            if (process_list.isEmpty()) break;

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

        // sort processes
        Collections.sort(sim_result.processes, (SimProcess s1, SimProcess s2)->s1.pid.compareTo(s2.pid));
        return sim_result;
    }






}
