import java.util.*;

public class SJF extends FCFS {


    public SJF() {
        ready_queue = new PriorityQueue<>((SimProcess s1, SimProcess s2)->(s1.time_rem - s2.time_rem));
    }
}
