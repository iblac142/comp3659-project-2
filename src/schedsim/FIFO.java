package schedsim;

import java.util.LinkedList;

public class FIFO extends Simulator {

    FIFO(Process[] process_table, LinkedList<Event> event_queue) {
        super(process_table, event_queue);
    }

    @Override
    protected void handleProcessArrives(Event event) {
        ready_queue.addLast(event.process);
    }

    @Override
    protected void handleCpuBurstFinishes(Event event) {
        running_process = -1;
        if (process_table[event.process].hasMoreIoBursts()) {
            wait_queue.addLast(event.process);
            scheduleIoBurstCompletion(event.process);
        }
        // If no more IO bursts, process is complete
    }

    @Override
    protected void handleIoBurstFinishes(Event event) {
        wait_queue.remove(event.process);
        process_table[event.process].burst_number++; // might want to rename burst_number to burst_cycle

        if (process_table[event.process].hasMoreCpuBursts()) {
            ready_queue.addLast(event.process);
        }
        // If no more bursts, process is complete
    }

    @Override
    protected int decideNextRunningProcess() {

        if (running_process == -1 & !ready_queue.isEmpty()) {
            return ready_queue.removeFirst();
        }

        return -1;
    }

}
