/**
 * Author: Simon Brillant-Giroux
 * COMP-346
 * Assignment 2
 * July 26, 2020
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    static final String IDLE = "___";
    static int nCPUs;
    static int quantum;
    static ArrayList<String> inputLines = new ArrayList<>();

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String inputFilePath;
        String outputFilePath;

        if (args.length == 3) {
            inputFilePath = args[0];
            outputFilePath = args[1];
            quantum = Integer.parseInt(args[2]);
        }
        else {
            while (true) {
                System.out.println("Please provide a valid input file path: ");
                inputFilePath = scan.nextLine().trim();
                if (new File(inputFilePath).canRead()) break;
            }
            while (true) {
                System.out.println("Please provide a valid output file path: ");
                outputFilePath = scan.nextLine().trim();
                if (new File(inputFilePath).canRead()) break;
            }
        }

        // read input
        init(inputFilePath);

        while(quantum < 1) {
            try {
                System.out.println("Please provide a quantum (>= 1)");
                quantum = scan.nextInt();
            }
            catch (Exception e) {
                System.out.println("Problem reading quantum value.");
                scan.nextLine();
            }
        }


        try (PrintWriter pw = new PrintWriter(outputFilePath)) {

            System.out.println("Quantum value : " + quantum);
            pw.println("Quantum value : " + quantum);

            System.out.println("\n***** FCFS *****\n");
            pw.println("\n**** FCFS ****\n");
            SimulationResult sim_result = new FCFS(nCPUs, build_process_list()).runProcesses();
            print_results(sim_result, pw);

            System.out.println("\n\n***** SJF *****\n");
            pw.println("\n\n**** SJF ****\n");
            sim_result = new SJF(nCPUs, build_process_list()).runProcesses();
            print_results(sim_result, pw);

            System.out.println("\n\n***** SRTF *****\n");
            pw.println("\n\n***** SRTF *****\n");
            sim_result = new SRTF(nCPUs, build_process_list()).runProcesses();
            print_results(sim_result, pw);

            System.out.println("\n\n***** RR with quantum: " + quantum +" *****\n");
            pw.println("\n\n**** RR with quantum: " + quantum +" ****\n");
            sim_result = new RR(nCPUs, build_process_list()).runProcesses();
            print_results(sim_result, pw);
        }
        catch (FileNotFoundException e) {
            System.out.println("Problem writing to output file.");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Problem processing input. please check input format.");
            e.printStackTrace();
        }




    }

    static void print_results(SimulationResult results, PrintWriter pw) {

        // print the timeline
        StringBuilder sb = new StringBuilder();
        sb.append("\ntime              |");
        int n_time_units = results.cpuTimelines.get(0).size();
        for (int i = 0; i < n_time_units; ++i) {
            sb.append(String.format("%03d", i) + "|");
        }
        sb.append("\n");
        for (int i = 0; i < (19 + n_time_units*4); ++i) sb.append("-");
        sb.append('\n');
        for (int i = 0; i < results.cpuTimelines.size(); ++i) {
            sb.append("CPU" + String.format("%03d", i) + "             ");
            var cpu_timeline = results.cpuTimelines.get(i);
            for (String str : cpu_timeline) {
                sb.append(str + (str.length() == 2? "  ": " "));
            }
            sb.append('\n');
        }
        sb.append('\n');

        // check the max length of all ready-queue states
        int max_length = 0;
        for (ArrayList<String> queue : results.readyqueueTimeline) {
            if (queue.size() > max_length) {
                max_length = queue.size();
            }
        }
        // print the ready-queue, level by level
        sb.append("Ready-queue        ");
        for (int i = 0; i < max_length; ++i) {
            for (var queue_state : results.readyqueueTimeline) {
                if (queue_state.size() > i) {
                    String s = queue_state.get(i);
                    sb.append(s + (s.length() == 2? "  ": " "));
                }
                else sb.append("    ");
            }
            sb.append("\n                   ");
        }
        sb.append("\n");

        //print the IO processing queue
        sb.append("IO processing      ");
        for (String s : results.ioTimeline) {
            sb.append(s + (s.length() == 2? "  ": " "));
        }
        sb.append("\n\n");

        // check the max length of all IO wait-queue states
        max_length = 0;
        for (ArrayList<String> queue : results.ioWaitqueueTimeline) {
            if (queue.size() > max_length) {
                max_length = queue.size();
            }
        }
        //print the IO wait queue

        sb.append("IO wait queue      ");
        for (int i = 0; i < max_length; ++i) {
            for (var queue_state : results.ioWaitqueueTimeline) {
                if (queue_state.size() > i) {
                    String s = queue_state.get(i);
                    sb.append(s + (s.length() == 2? "  ": " "));
                }
                else sb.append("    ");
            }
            sb.append("\n                   ");
        }
        sb.append("\n");


        System.out.println(sb.toString());
        pw.println(sb.toString());

        // General stats

        sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.00");
        sb.append("\n** General statistics **\n\n");

        sb.append("CPU utilization:\n\n");

        int total_time_units = results.cpuTimelines.get(0).size();
        int total_idle_time = 0;

        for (int i = 0; i < nCPUs; ++i) {
            sb.append("CPU" + String.format("%03d", i) + ": ");
            // count idle time units
            int idle_units = 0;
            var cpu_timeline = results.cpuTimelines.get(i);
            for (String str : cpu_timeline) {
                if (str.equals(Main.IDLE)) ++idle_units;
            }
            sb.append("" + (total_time_units - idle_units) + "/" + total_time_units + "\n");
            total_idle_time += idle_units;
        }


        sb.append("\nTOTAL CPU USAGE: " + (total_time_units * nCPUs - total_idle_time) + "/" + total_time_units * nCPUs);
        sb.append(" (" +df.format(
                100.0 *(total_time_units* nCPUs - total_idle_time)/(total_time_units* nCPUs)) + "%)");


        sb.append("\n\nAverage wait time and wait time per process: \n");
        int total_wait_time = 0;
        for (SimProcess p : results.processes) {
            sb.append(p.pid + ": " + p.timeWaiting + "\n");
            total_wait_time += p.timeWaiting;
        }
        sb.append("Average wait time: " + df.format((double)total_wait_time/results.processes.size()));

        sb.append("\n\nTurnaround time for each process\n\n");
        int total = 0;
        for (SimProcess p : results.processes) {
            sb.append(p.pid + ": " + p.turnaround + "\n");
            total += p.turnaround;
        }
        sb.append("Average turnaround: " + df.format((double)total/results.processes.size()));


        // print CPU response time
        sb.append("\n\nCPU response time for each process\n\n");
        total = 0;
        for (SimProcess p : results.processes) {
            sb.append(p.pid + ": " + p.responseTime + "\n");
            total += p.responseTime;
        }
        sb.append("Average response time: " + df.format((double)total/results.processes.size()));

        System.out.println(sb.toString());
        pw.println(sb.toString());

    }

    static void init(String file_path) {
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            boolean found_n_cpus = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;
                else if (!found_n_cpus){
                    nCPUs = Integer.parseInt(line.split("[ \t$]+")[1]);
                    if (nCPUs < 1) {
                        System.out.println("At least 1 CPU is needed. Value provided: " + nCPUs + ". Please modify the number of CPUs.\nExiting...");
                        System.exit(1);
                    }
                    found_n_cpus = true;
                }
                else {
                    inputLines.add(line);
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("Couldn't open file: " + file_path);
            ioe.printStackTrace();
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Problem with input format");
            e.printStackTrace();
            System.exit(1);
        }
    }

    static ArrayList<SimProcess> build_process_list() throws Exception {
        ArrayList<SimProcess> process_list = new ArrayList<>();
        for (String entry_line : inputLines) {
            String[] fields = entry_line.split("[ \t$]+");
            LinkedList<Integer> io_requests = new LinkedList<>();
            if (fields.length > 3) { // there are IO requests
                for (int i = 3; i < fields.length; ++i) {
                    io_requests.add(Integer.parseInt(fields[i]));
                }
            }

            process_list.add(
                    new SimProcess(
                            fields[0],
                            Integer.parseInt(fields[1]),
                            Integer.parseInt((fields[2])),
                            io_requests)
            );

        }

        if (process_list.isEmpty()) {
            System.out.println("No processes were specified. At least one process is needed.\nExiting...");
            System.exit(1);
        }
        return process_list;
    }
}
