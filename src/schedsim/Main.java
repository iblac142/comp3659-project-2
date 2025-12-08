package schedsim;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Process[] process_table = new Process[128];
        LinkedList<Event> event_queue = new LinkedList<Event>();

        Inputs inputs = new Inputs();
        Scanner in = new Scanner(System.in);
        int selected_algorithm = inputs.intInput(in,
                "Select an algorithm.\n"
                + "0 - First Come First Serve\n"
                + "1 - Simple Priority\n"
                + "2 - Shortest Job First\n"
                + "3 - Pre-Emptive Shortest Job First\n"
                + "4 - Round Robin",
                0, 4);

        if (inputs.getProcesses(in, process_table) >= 0) {
	        if (selected_algorithm == 0) {
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

	            SJF sjf = new SJF(process_table, event_queue, inputs.intInput(in, 
	            		"Input an \"a\" % value: (0-100)", 0, -1),
	            		inputs.intInput(in, "Input the initial prediction value: (0+)", 0, -1));
	
	            sjf.runSimulator();
	            sjf.outputStatistics();
	        }
	
	        if (selected_algorithm == 3) {
	            SRJF srjf = new SRJF(process_table, event_queue,
	            		inputs.intInput(in, "Input an \"a\" % value: (0-100)", 0, -1),
	            		inputs.intInput(in, "Input the initial prediction value: (0+)", 0, -1));
	
	            srjf.runSimulator();
	            srjf.outputStatistics();
	        }
	
	        if (selected_algorithm == 4) {
	            //q = inputs.intInput(in, "Input a \"q\" value: (1+)", 1, -1);
	        }
        }

    }
}
