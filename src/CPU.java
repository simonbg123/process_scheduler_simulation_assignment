/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

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
