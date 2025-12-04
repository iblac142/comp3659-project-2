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

    Simulator(Process[] process_table, LinkedList<Event> event_queue) {
        this.process_table = process_table;
        this.event_queue = event_queue;
    }

    public void runSimulator() {
        while (!event_queue.isEmpty()) {
            Event event = event_queue.removeFirst();
            handleEvent(event);
        }

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

    protected void handleCpuBurstFinishes(Event event) {
        System.out.println("Undefined event for this scheduling algorithm");
    }

    protected void handleIoBurstFinishes(Event event) {
        System.out.println("Undefined event for this scheduling algorithm");
    }

    private void invokeScheduler() {
        int picked_process = decideNextRunningProcess();

        if (picked_process == -1) {
            return;
        }
        running_process = picked_process;

        scheduleCpuBurstCompletion(running_process);
    }

    // returns process ID, -1 if not changed
    protected int decideNextRunningProcess() {
        return -1;
    }

    private void scheduleCpuBurstCompletion(int running_process) {
        int cpu_burst_length = process_table[running_process].getBurst();
        int cpu_burst_end_time = cpu_burst_length + time;
        Event cpu_burst_end_event = new Event("burst_finished", running_process, cpu_burst_end_time);

        addEvent(cpu_burst_end_event);
    }

    protected void scheduleIoBurstCompletion(int process) {
        int io_burst_length = process_table[process].getWait();
        int io_burst_end_time = io_burst_length + time;
        Event io_burst_end_event = new Event("io_burst_finishes", process, io_burst_end_time);

        addEvent(io_burst_end_event);
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