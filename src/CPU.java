import java.util.ArrayList;


public class CPU {
    SimProcess currentProcess;
    ArrayList<String> timeline;

    public CPU() {
        currentProcess = null;
        timeline = new ArrayList<>();
    }
}
