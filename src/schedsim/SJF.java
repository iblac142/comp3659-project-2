package schedsim;

import java.util.LinkedList;

/**
 * Simulates a shortest job first CPU scheduling algorithm
 */
public class SJF extends SimplePriority {
    protected float weighting_coefficient;
    protected int starting_guess;

    SJF(Process[] process_table, LinkedList<Event> event_queue, float weighting_coefficient, int starting_guess) {
        super(process_table, event_queue);
        this.weighting_coefficient = weighting_coefficient;
        this.starting_guess = starting_guess;
    }

    @Override
    protected void handleProcessArrives(Event event) {
        process_table[event.getProcess()].setPerdicted_cpu_burst_lenght(starting_guess);
        addToReadyQueueInitial(event.getProcess());
    }

    /**
     * add given process to ready queue for use only when it first arrives
     *
     * @param process_number process to add to ready queue
     */
    private void addToReadyQueueInitial(int process_number) {
        super.addToReadyQueue(process_number);
    }

    @Override
    protected void addToReadyQueue(int process_number) {
        process_table[process_number].calculateNewPerdictedCpuBurstLength(weighting_coefficient);
        super.addToReadyQueue(process_number);

    }
}
