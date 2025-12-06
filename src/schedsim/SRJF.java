package schedsim;

import java.util.LinkedList;

public class SRJF extends SJF {

    SRJF(Process[] process_table, LinkedList<Event> event_queue, int weighting_coefficient, int starting_guess) {
        super(process_table, event_queue, weighting_coefficient, starting_guess);
    }

    @Override
    protected int decideNextRunningProcess() {
        if (!ready_queue.isEmpty()) {
            if (isNextJobShorter()) {
                return ready_queue.removeFirst();
            }
        }

        return UNCHANGED;
    }

    private boolean isNextJobShorter() {
        Process current_process = process_table[running_process];
        Process next_process = process_table[ready_queue.peekFirst()];

        int current_burst_completed = time - current_process.getBurst_start_time();
        int current_process_burst_remaining = current_process.getCurrentCpuBurstLength() - current_burst_completed;
        int next_process_burst_length = next_process.getNextCpuBurstLength();

        if (next_process_burst_length < current_process_burst_remaining) {
            return true;
        } else {
            return false;
        }

    }
}
