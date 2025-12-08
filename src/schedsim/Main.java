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
        LinkedList<Event> event_queue;
        
        Inputs inputs = new Inputs();
        Scanner in = new Scanner(System.in);
        int selected_algorithm = inputs.intInput(in, "Select an algorithm.\n0 - First Come First Serve\n1 - Simple Priority\n2 - Shortest Job First\n3 - Pre-Emptive Shortest Job First\n4 - Round Robin", 0, 4);
        
        if (selected_algorithm == 2 || selected_algorithm == 3) {
        	a = Inputs.intInput(in, "Input an \"a\" % value: (0-100)", 0, -1);
        	f = Inputs.intInput(in, "Input the initial prediction value: (0+)", 0, -1);
        }
        
        if (selected_algorithm == 4) {
        	q = Inputs.intInput(in, "Input a \"q\" value: (1+)", 1, -1);
        }
        
        if (inputs.getProcesses(in, process_table) >= 0) {
        	in.close();
        	// TODO
        	// run the simulation
        };
        
        
        // just a test to see if the input works
        /* for (int i = 0; i < process_table.length && process_table[i] != null; i += 1) {
        	System.out.println("Process " + i + ":");
        	process_table[i].print();
        } */
        
    }
}
