package schedsim;

import java.util.LinkedList;

public class FIFO extends Simulator {

    FIFO(Process[] process_table, LinkedList<Event> event_queue) {
        super(process_table, event_queue);
    }

    @Override
    protected void handleProcessArrives(Event event) {
        addToReadyQueue(event.getProcess());
    }

    @Override
    protected int decideNextRunningProcess() {
        if (running_process == NO_RUNNING_PROCESS & !ready_queue.isEmpty()) {
            return ready_queue.removeFirst();
        }

        return UNCHANGED;
    }

}
