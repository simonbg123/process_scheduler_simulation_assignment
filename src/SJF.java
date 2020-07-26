import java.util.*;

public class SJF extends FCFS {


    public SJF(ArrayList<SimProcess> process_list) {
        super(process_list);
        ready_queue = new PriorityQueue<>(Comparator.comparingInt(SimProcess::getTime_rem));
    }
}
