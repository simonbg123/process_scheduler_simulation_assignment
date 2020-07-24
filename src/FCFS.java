import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class FCFS implements SchedulingAlgorithm {

    LinkedList<SimProcess> ready_queue;
    IOqueue io_queue;
    SimulationResult sim_result;
    StringArrayList[] cpu_list;

    public FCFS() {
        ready_queue = new LinkedList<>();
        io_queue = new IOqueue();
        sim_result = new SimulationResult();
        cpu_list = new StringArrayList[Simulation.n_cpus];
    }

    @Override
    public SimulationResult run_processes(ArrayList<SimProcess> process_list) {

        Iterator<SimProcess> iterator = process_list.iterator();
        int time = 0;
        while (!process_list.isEmpty()) {

            // Poll all processes and queue those that arrive
            while (iterator.hasNext()) {
                SimProcess p = iterator.next();
                if (p.arrival_time == time) {
                    ready_queue.add(p);
                }

            }
            //else if (!p.IOrequests.isEmpty() && p.exec_time - p.time_rem == p.IOrequests.peek()) {
            //                    p.IOrequests.removeFirst();
            //                    io_queue.wait_queue.add(p); // may still be moved to io processing during this cycle
            //
            //
            //                }


            ++time;
        }





        return null;
    }
}
