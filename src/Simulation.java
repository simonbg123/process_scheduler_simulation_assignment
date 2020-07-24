import java.util.ArrayList;
import java.util.Iterator;

public class Simulation {

    public static final String IDLE = "___";
    static int n_cpus;
    static void print_results(ArrayList<SimProcess> process_list, SimulationResult results) {

    }
    static void read_input(String file_path) {

    }

    public static void main(String[] args) {
        //main loop create a brand new list of processes for each algo

        // iterator test
        ArrayList<String> l = new ArrayList<>();
        l.add("hello1");
        l.add("hello2");

        Iterator<String> it = l.iterator();

        while (it.hasNext()) {
            System.out.println("hey");
            System.out.println(it.next());
            //it.remove();
        }
        System.out.println("done");
        System.out.println(l);
    }




}
