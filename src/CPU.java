/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.util.ArrayList;
import java.util.List;


public class CPU {
    private SimProcess currentProcess;
    private List<String> timeline;
    private int currentTimeslice; // for purposes of round-robbin

    public CPU() {
        currentProcess = null;
        timeline = new ArrayList<>();
        currentTimeslice = 0;
    }

    public SimProcess getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(SimProcess currentProcess) {
        this.currentProcess = currentProcess;
    }

    public List<String> getTimeline() {
        return timeline;
    }

    public int getCurrentTimeslice() {
        return currentTimeslice;
    }

    public void incrementCurrentTimeslice() {
        ++currentTimeslice;
    }

    public void setCurrentTimeslice(int currentTimeslice) {
        this.currentTimeslice = currentTimeslice;
    }


    public void addToTimeline(String s) {
        timeline.add(s);
    }
}
