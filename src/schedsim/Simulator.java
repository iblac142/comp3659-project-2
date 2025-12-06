package schedsim;

import java.util.Comparator;
import java.util.LinkedList;

//before submitting look at all access modifiers again
public class Simulator {
    protected final int UNCHANGED = -1;
    protected final int NO_RUNNING_PROCESS = -1;

    protected LinkedList<Integer> ready_queue;
    protected LinkedList<Integer> wait_queue;
    protected LinkedList<Event> event_queue;
    protected Process[] process_table;
    protected int running_process = NO_RUNNING_PROCESS;
    protected int time = 0;
    protected int end_time;
    protected int total_idle_time = 0;
    protected int idle_instance_start = 0;
    protected boolean is_idle = true;

    Simulator(Process[] process_table, LinkedList<Event> event_queue) {
        this.process_table = process_table;
        this.event_queue = event_queue;
    }

    public void runSimulator() {
        while (!event_queue.isEmpty()) {
            Event event = event_queue.removeFirst();
            handleEvent(event);
        }

        end_time = time;

        outputResults();
    }

    // for all other event types added follow same structure
    private void handleEvent(Event event) {
        time = event.getTime();
        String type = event.getType();

        if (type == "process_arrives") {
            handleProcessArrives(event);
        }

        else if (type == "cpu_burst_finishes") {
            handleCpuBurstFinishes(event);
        }

        else if (type == "io_burst_finishes") {
            handleIoBurstFinishes(event);
        }
        // may make sense to move this call to handlers depending on how many event
        // handlers would need to call it (all FIFO ones do)
        invokeScheduler();
    }

    protected void handleProcessArrives(Event event) {
        addToReadyQueue(event.getProcess());
    }

    private void handleCpuBurstFinishes(Event event) {
        int process_number = event.getProcess();

        startIdle();

        if (process_table[process_number].hasMoreIoBursts()) {
            addToWaitQueue(process_number);
        } else {
            // If no more IO bursts, process is complete
            process_table[process_number].setEnd_time(time);
        }

    }

    private void addToWaitQueue(int process_number) {
        wait_queue.addLast(process_number);
        scheduleIoBurstCompletion(process_number);
    }

    private void handleIoBurstFinishes(Event event) {
        int process_number = event.getProcess();

        removeFromWaitQueue(process_number);

        if (process_table[process_number].hasMoreCpuBursts()) {
            addToReadyQueue(process_number);
        } else {
            // If no more bursts, process is complete
            process_table[process_number].setEnd_time(time);
        }

    }

    private void removeFromWaitQueue(int process_number) {
        wait_queue.remove(process_number);
        // increment shared index for both arrays of burst lengths
        process_table[process_number].incrementBurstNumber(); // might want to rename burst_number to burst_cycle
    }

    protected void addToReadyQueue(int process_number) {
        ready_queue.addLast(process_number);
        process_table[process_number].startWaiting(time);
    }

    private void invokeScheduler() {
        int picked_process = decideNextRunningProcess();

        if (picked_process == UNCHANGED) {
            return;
        }
        runProcess(picked_process);
    }

    // returns process ID, UNCHANGED if not changed
    protected int decideNextRunningProcess() {
        return UNCHANGED;
    }

    private void runProcess(int process) {
        running_process = process;
        removeFromReadyQueue(running_process);

        if (is_idle) {
            stopIdle();
        }

        process_table[running_process].setBurst_start_time(time);
        scheduleCpuBurstCompletion(running_process);
    }

    private void removeFromReadyQueue(int process_number) {
        ready_queue.remove(process_number);
        process_table[process_number].stopWaiting(time);
    }

    private void scheduleCpuBurstCompletion(int running_process) {
        int cpu_burst_length = process_table[running_process].getNextCpuBurstLength();
        int cpu_burst_end_time = cpu_burst_length + time;
        Event cpu_burst_end_event = new Event("burst_finished", running_process, cpu_burst_end_time);

        addEvent(cpu_burst_end_event);
    }

    private void scheduleIoBurstCompletion(int process) {
        int io_burst_length = process_table[process].getNextIoBurstLength();
        int io_burst_end_time = io_burst_length + time;
        Event io_burst_end_event = new Event("io_burst_finishes", process, io_burst_end_time);

        addEvent(io_burst_end_event);
    }

    private void startIdle() {
        running_process = NO_RUNNING_PROCESS;
        is_idle = true;
        idle_instance_start = time;
    }

    private void stopIdle() {
        is_idle = false;
        total_idle_time += time - idle_instance_start;
    }

    private void addEvent(Event event) {
        event_queue.add(event);
        event_queue.sort(Comparator.comparingInt(Event::getTime));
        // or comparingInt(e -> e.getTime()) if this does not work
    }

    @SuppressWarnings("unused")
    private void removeEvent(Event event) {
        event_queue.remove(event);
    }

    private void outputResults() {
        // TODO
    }
}