package schedsim;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int time = 0;
        int current_process = -1;

        int a; // for sjf
        int f; // for sjf
        int q; // for rr

        Process[] process_table = new Process[128];
        LinkedList<Integer> process_queue;
        LinkedList<Event> event_queue = new LinkedList<Event>();

        Inputs inputs = new Inputs();
        Scanner in = new Scanner(System.in);
        int selected_algorithm = inputs.intInput(in,
                "Select an algorithm.\n0 - First Come First Serve\n1 - Simple Priority\n2 - Shortest Job First\n3 - Pre-Emptive Shortest Job First\n4 - Round Robin",
                0, 4);

        inputs.getProcesses(in, process_table);

        if (selected_algorithm == 0) {
            // TODO
            FIFO fifo = new FIFO(process_table, event_queue);

            fifo.runSimulator();
            fifo.outputStatistics();
        }

        if (selected_algorithm == 1) {
            SimplePriority prio = new SimplePriority(process_table, event_queue);

            prio.runSimulator();
            prio.outputStatistics();
        }

        if (selected_algorithm == 2) {
            a = inputs.intInput(in, "Input an \"a\" % value: (0-100)", 0, -1);
            f = inputs.intInput(in, "Input the initial prediction value: (0+)", 0, -1);

            SJF sjf = new SJF(process_table, event_queue, a, f);

            sjf.runSimulator();
            sjf.outputStatistics();
        }

        if (selected_algorithm == 3) {
            a = inputs.intInput(in, "Input an \"a\" % value: (0-100)", 0, -1);
            f = inputs.intInput(in, "Input the initial prediction value: (0+)", 0, -1);

            SRJF srjf = new SRJF(process_table, event_queue, a, f);

            srjf.runSimulator();
            srjf.outputStatistics();
        }

        if (selected_algorithm == 4) {
            q = inputs.intInput(in, "Input a \"q\" value: (1+)", 1, -1);
        }

    }
}
