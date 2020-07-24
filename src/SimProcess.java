import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Simulates a running process
 */
public class SimProcess {

    String pid;
    int arrival_time;
    int exec_time;  // total cpu time required by the process
    LinkedList<Integer> IOrequests;  // times of IO requests to be made by the process, in relation to the exec_time
    int time_rem;  // current remaining CPU time required by the process
    int time_waiting;
    int time_of_completion;
    int response_time;

    public SimProcess(String pid, int arrival_time, int exec_time, LinkedList<Integer> IOrequests) {
        this.pid = pid;
        this.arrival_time = arrival_time;
        this.exec_time = exec_time;
        this.IOrequests = IOrequests;
        this.time_rem = exec_time;
        time_waiting = 0;
        time_of_completion = 0;
        response_time = -1;
    }
}
