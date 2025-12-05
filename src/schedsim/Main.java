package schedsim;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        int time = 0;
        int current_process = -1;
        Process[] process_table = new Process[128];
        LinkedList<Integer> process_queue;
        LinkedList<Event> event_queue;
        
        Inputs inputs = new Inputs();
        inputs.getProcesses(process_table);
        
        // just a test to see if the input works
        /* for (int i = 0; i < process_table.length && process_table[i] != null; i += 1) {
        	System.out.println("Process " + i + ":");
        	process_table[i].print();
        } */
        
    }
}
