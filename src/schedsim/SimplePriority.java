package schedsim;

import java.util.Comparator;
import java.util.LinkedList;

public class SimplePriority extends Simulator {

    SimplePriority(Process[] process_table, LinkedList<Event> event_queue) {
        super(process_table, event_queue);
    }

    @Override
    protected void addToReadyQueue(int process_number) {
        super.addToReadyQueue(process_number);
        sortReadyQueue();

    }

    protected void sortReadyQueue() {
        ready_queue.sort(Comparator.comparingInt(process_number -> process_table[process_number].getPriority()));
    }

    @Override
    protected int decideNextRunningProcess() {
        if (running_process == NO_RUNNING_PROCESS & !ready_queue.isEmpty()) {
            return ready_queue.peekFirst(); 
        }

        return UNCHANGED;
    }
}
