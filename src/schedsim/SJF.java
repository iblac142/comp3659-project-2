package schedsim;

import java.util.LinkedList;

public class SJF extends SimplePriority {
    protected int weighting_coefficient;
    protected int starting_guess;

    SJF(Process[] process_table, LinkedList<Event> event_queue, int weighting_coefficient, int starting_guess) {
        super(process_table, event_queue);
        this.weighting_coefficient = weighting_coefficient;
        this.starting_guess = starting_guess;
    }

    @Override
    protected void handleProcessArrives(Event event) {
        process_table[event.getProcess()].setPerdicted_cpu_burst_lenght(starting_guess);
        addToReadyQueueInitial(event.getProcess());
    }

    private void addToReadyQueueInitial(int process_number) {
        super.addToReadyQueue(process_number);
    }

    @Override
    protected void addToReadyQueue(int process_number) {
        process_table[process_number].calculateNewPerdictedCpuBurstLength(weighting_coefficient);
        super.addToReadyQueue(process_number);

    }
}
