package schedsim;

import java.util.Comparator;
import java.util.LinkedList;

//before submitting look at all access modifiers again
public class Simulator {
    protected LinkedList<Integer> ready_queue;
    protected LinkedList<Integer> wait_queue;
    protected LinkedList<Event> event_queue;
    protected Process[] process_table;
    protected int running_process = -1;
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
        time = event.time;

        if (event.type == "process_arrives") {
            handleProcessArrives(event);
        }

        else if (event.type == "cpu_burst_finishes") {
            handleCpuBurstFinishes(event);
        }

        else if (event.type == "io_burst_finishes") {
            handleIoBurstFinishes(event);
        }

        invokeScheduler(); // may make sense to move this call to children depending on how many event
                           // handlers would need to call it (all FIFO ones do) if do make method protected
    }

    protected void handleProcessArrives(Event event) {
        System.out.println("Undefined event for this scheduling algorithm");
    }

    private void handleCpuBurstFinishes(Event event) {
        startIdle();
        if (process_table[event.process].hasMoreIoBursts()) {
            wait_queue.addLast(event.process); // maybe make this function addToWaitQueue
            scheduleIoBurstCompletion(event.process);
        }
        // If no more IO bursts, process is complete
        process_table[event.process].end_time = time;
    }

    private void handleIoBurstFinishes(Event event) {
        wait_queue.remove(event.process);
        process_table[event.process].burst_number++; // might want to rename burst_number to burst_cycle

        if (process_table[event.process].hasMoreCpuBursts()) {
            ready_queue.addLast(event.process); // maybe make this (and duplicate in FIFO) a function addToReadyQueue
            process_table[event.process].startWaiting(time);
        }
        // If no more bursts, process is complete
        process_table[event.process].end_time = time;
    }

    private void invokeScheduler() {
        int picked_process = decideNextRunningProcess();

        if (picked_process == -1) {
            return;
        }
        runProcess(picked_process);
    }

    // returns process ID, -1 if not changed
    protected int decideNextRunningProcess() {
        return -1;
    }

    private void runProcess(int process) {
        running_process = process;
        process_table[running_process].stopWaiting(time);

        if (is_idle) {
            stopIdle();
        }

        scheduleCpuBurstCompletion(running_process);
    }

    private void scheduleCpuBurstCompletion(int running_process) {
        int cpu_burst_length = process_table[running_process].getBurst();
        int cpu_burst_end_time = cpu_burst_length + time;
        Event cpu_burst_end_event = new Event("burst_finished", running_process, cpu_burst_end_time);

        addEvent(cpu_burst_end_event);
    }

    private void scheduleIoBurstCompletion(int process) {
        int io_burst_length = process_table[process].getWait();
        int io_burst_end_time = io_burst_length + time;
        Event io_burst_end_event = new Event("io_burst_finishes", process, io_burst_end_time);

        addEvent(io_burst_end_event);
    }

    private void startIdle() {
        running_process = -1;
        is_idle = true;
        idle_instance_start = time;
    }

    private void stopIdle() {
        is_idle = false;
        total_idle_time += time - idle_instance_start;
    }

    protected void addEvent(Event event) {
        event_queue.add(event);
        event_queue.sort(Comparator.comparingInt(e -> e.time));
        // or comparingInt(Event::getInt) if make getter for time
    }

    protected void removeEvent(Event event) {
        event_queue.remove(event);
    }

    private void outputResults() {
        // TODO
    }
}