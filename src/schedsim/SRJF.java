package schedsim;

import java.util.LinkedList;

/**
 * Simulates a shortest remaining job first (also know as preemptive shortest
 * job first) CPU scheduling algorithm
 */
public class SRJF extends SJF {

    SRJF(Process[] process_table, LinkedList<Event> event_queue, int weighting_coefficient, int starting_guess) {
        super(process_table, event_queue, weighting_coefficient, starting_guess);
    }

    @Override
    protected int decideNextRunningProcess() {
        if (!ready_queue.isEmpty()) {
            if (running_process == NO_RUNNING_PROCESS) {
                return ready_queue.peekFirst();
            }

            if (isNextJobShorter()) {
                return ready_queue.peekFirst();
            }
        }

        return UNCHANGED;
    }

    /**
     * Determines if the next job has a shorter CPU burst then the current process
     * had remaining. Also handles case when no process currently running
     *
     * @return true if the next job is shorter, false otherwise
     */
    private boolean isNextJobShorter() {
        if (running_process == NO_RUNNING_PROCESS) {
            return false;
        }

        Process current_process = process_table[running_process];
        Process next_process = process_table[ready_queue.peekFirst()];

        int current_burst_completed = time - current_process.getBurst_start_time();
        int current_process_burst_total = current_process.getCpuBurstLength();
        int current_process_burst_remaining = current_process_burst_total - current_burst_completed;
        int next_process_burst_length = next_process.getCpuBurstLength();

        if (next_process_burst_length < current_process_burst_remaining) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void runProcess(int process) {
        if (running_process != NO_RUNNING_PROCESS && running_process != process) {
            stopRunningPreemptedProcess();
            updateCpuBurstLength(process_table[running_process]);
        }

        super.runProcess(process);

    }

    /**
     * Stops running the current process if it has been preempted
     */
    private void stopRunningPreemptedProcess() {
        process_table[running_process].setBeen_preempted(true);
        event_queue.removeIf(
                event -> "cpu_burst_finishes".equals(event.getType()) && event.getProcess() == running_process);
        addToReadyQueue(running_process);
    }

    /**
     * Updates the CPU burst length for a preempted process
     *
     * @param process the process to update the burst length for
     */
    private void updateCpuBurstLength(Process process) {
        int cpu_burst_total = process.getCpuBurstLength();
        int cpu_burst_completed = time - process.getBurst_start_time();
        int cpu_burst_remaining = cpu_burst_total - cpu_burst_completed;

        process.setCpuBurstLength(cpu_burst_remaining);
    }

    @Override
    protected void addToReadyQueue(int process_number) {
        super.addToReadyQueue(process_number);
        process_table[process_number].setBeen_preempted(false);

    }

}
