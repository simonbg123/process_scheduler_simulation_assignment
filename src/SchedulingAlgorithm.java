import java.util.ArrayList;

public interface SchedulingAlgorithm {

    SimulationResult run_processes(ArrayList<SimProcess> process_list);

}
