import java.util.*;

public class SJF extends FCFS {


    public SJF() {
        ready_queue = new PriorityQueue<>(Comparator.comparingInt(SimProcess::getTime_rem));
    }
}
