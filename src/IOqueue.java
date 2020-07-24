import java.util.LinkedList;

public class IOqueue {

    SimProcess currentlyProcessing;
    int time_remaining;
    LinkedList<SimProcess> wait_queue;

    public IOqueue() {
        currentlyProcessing = null;
        time_remaining = 0;
        wait_queue = new LinkedList<>();
    }
}
