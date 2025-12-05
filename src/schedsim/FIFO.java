package schedsim;

import java.util.LinkedList;

public class FIFO extends Simulator {

    FIFO(Process[] process_table, LinkedList<Event> event_queue) {
        super(process_table, event_queue);
    }

    @Override
    protected void handleProcessArrives(Event event) {
        ready_queue.addLast(event.process);
        process_table[event.process].startWaiting(time);
    }

    @Override
    protected int decideNextRunningProcess() {
        if (running_process == -1 & !ready_queue.isEmpty()) {
            return ready_queue.removeFirst();
        }

        return -1;
    }

}
