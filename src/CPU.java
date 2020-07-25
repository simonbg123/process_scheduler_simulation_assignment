import java.util.ArrayList;


public class CPU {
    SimProcess currentProcess;
    ArrayList<String> timeline;
    int current_timeslice; // for purposes of round-robbin

    public CPU() {
        currentProcess = null;
        timeline = new ArrayList<>();
        current_timeslice = 0;
    }
}
