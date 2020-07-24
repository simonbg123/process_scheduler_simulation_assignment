import java.util.ArrayList;

public class SimulationResult {

    ArrayList<ArrayList<String>> cpu_timelines;
    ArrayList<String> IO_timeline;
    ArrayList<ArrayList<String>> IO_waitqueue_timeline;

    public SimulationResult() {
        cpu_timelines = new ArrayList<>();
        IO_timeline = new ArrayList<>();
        IO_waitqueue_timeline = new ArrayList<>();
    }
}
