package schedsim;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Base simulator class, does not function as a working scheduling algorithm
 * simulator itself instead this class is to contain shared functionality for
 * its child classes that are the functioning scheduling algorithm simulator
 */
public class Simulator {
    protected final int UNCHANGED = -1;
    protected final int NO_RUNNING_PROCESS = -1;

    protected LinkedList<Integer> ready_queue;
    protected LinkedList<Integer> wait_queue;
    protected LinkedList<Event> event_queue;
    protected Process[] process_table;
    protected int running_process = NO_RUNNING_PROCESS;
    protected int process_count;
    protected int time = 0;
    protected int end_time;
    protected int total_idle_time = 0;
    protected int idle_instance_start = 0;
    protected boolean is_idle = true;

    /**
     * Constructor for Simulator
     *
     * @param process_table list of all processes that will be used in the
     *                      simulation
     * @param event_queue   linked list that will contain all events, must have all
     *                      process arrival events contained in it already and no
     *                      other events. Will not check to verify this.
     */
    Simulator(Process[] process_table, LinkedList<Event> event_queue) {
        this.ready_queue = new LinkedList<Integer>();
        this.wait_queue = new LinkedList<Integer>();

        this.process_table = process_table;
        this.event_queue = event_queue;

        int i = 0;
        while (i < process_table.length && process_table[i] != null) {
            i += 1;
        }
        this.process_count = i;
    }

    /**
     * Runs the simulator until there are no more events in the queue
     */
    public void runSimulator() {
        while (!event_queue.isEmpty()) {
            Event event = event_queue.removeFirst();

            if (!event_queue.isEmpty()) {
                Event next_event = event_queue.peekFirst();

                if (event.getTime() == next_event.getTime()) {
                    handleEvent(event);
                } else {
                    handleEvent(event);
                    invokeScheduler();
                }
            } else {
                handleEvent(event);
                invokeScheduler();
            }

        }

        end_time = time;
    }

    /**
     * handles events by calling the corresponding method
     *
     * @param event event to handle
     */
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
    }

    /**
     * handles process arriving event
     *
     * @param event event to handle
     */
    protected void handleProcessArrives(Event event) {
        addToReadyQueue(event.getProcess());
    }

    /**
     * handles process' CPU burst ending event
     *
     * @param event event to handle
     */
    private void handleCpuBurstFinishes(Event event) {
        int process_number = event.getProcess();

        startIdle();

        if (process_table[process_number].hasMoreIoBursts()) {
            addToWaitQueue(process_number);
        } else {
            process_table[process_number].setEnd_time(time);
        }

    }

    /**
     * handles process' IO burst ending event
     *
     * @param event event to handle
     */
    private void handleIoBurstFinishes(Event event) {
        int process_number = event.getProcess();

        removeFromWaitQueue(process_number);

        if (process_table[process_number].hasMoreCpuBursts()) {
            addToReadyQueue(process_number);
        } else {
            process_table[process_number].setEnd_time(time);
        }

    }

    /**
     * adds given process to the wait queue
     *
     * @param process_number process to add to wait queue
     */
    private void addToWaitQueue(int process_number) {
        wait_queue.addLast(process_number);
        scheduleIoBurstCompletion(process_number);
    }

    /**
     * removes given process to the wait queue
     *
     * @param process_number process to remove from wait queue
     */
    private void removeFromWaitQueue(int process_number) {
        // Use Integer.valueOf to force remove(Object) instead of remove(int)
        wait_queue.remove(Integer.valueOf(process_number));
        // increment shared index for both arrays of burst lengths
        process_table[process_number].incrementBurstNumber();
    }

    /**
     * adds given process to the ready queue
     *
     * @param process_number process to add to ready queue
     */
    protected void addToReadyQueue(int process_number) {
        ready_queue.addLast(process_number);
        process_table[process_number].startWaiting(time);
    }

    /**
     * removes given process to the ready queue
     *
     * @param process_number process to remove from ready queue
     */
    private void removeFromReadyQueue(int process_number) {
        // Use Integer.valueOf to force remove(Object) instead of remove(int)
        ready_queue.remove(Integer.valueOf(process_number));
        process_table[process_number].stopWaiting(time);
    }

    /**
     * invokes scheduler to make a scheduling decision
     */
    private void invokeScheduler() {
        int picked_process = decideNextRunningProcess();

        if (picked_process == UNCHANGED) {
            return;
        }
        runProcess(picked_process);
    }

    /**
     * returns the next process to run, can also return UNCHANGED if decision is
     * made to not change currently running process
     *
     * @return next process to run or UNCHANGED
     */
    protected int decideNextRunningProcess() {
        return UNCHANGED;
    }

    /**
     * runs given process
     *
     * @param process process to run
     */
    protected void runProcess(int process) {
        running_process = process;
        removeFromReadyQueue(running_process);

        if (is_idle) {
            stopIdle();
        }

        process_table[running_process].setBurst_start_time(time);
        scheduleCpuBurstCompletion(running_process);
    }

    /**
     * add event into event queue for when the CPU burst of the given process will
     * complete
     *
     * @param running_process the process for which to schedule event
     */
    private void scheduleCpuBurstCompletion(int running_process) {
        int cpu_burst_length = process_table[running_process].getCpuBurstLength();
        int cpu_burst_end_time = cpu_burst_length + time;
        Event cpu_burst_end_event = new Event("cpu_burst_finishes", running_process, cpu_burst_end_time);

        addEvent(cpu_burst_end_event);
    }

    /**
     * add event into event queue for when the IO burst of the given process will
     * complete
     *
     * @param running_process the process for which to schedule event
     */
    private void scheduleIoBurstCompletion(int process) {
        int io_burst_length = process_table[process].getIoBurstLength();
        int io_burst_end_time = io_burst_length + time;
        Event io_burst_end_event = new Event("io_burst_finishes", process, io_burst_end_time);

        addEvent(io_burst_end_event);
    }

    /**
     * start idle state
     */
    private void startIdle() {
        running_process = NO_RUNNING_PROCESS;
        is_idle = true;
        idle_instance_start = time;
    }

    /**
     * stop idle state
     */
    private void stopIdle() {
        is_idle = false;
        total_idle_time += time - idle_instance_start;
    }

    /**
     * add an event to the event queue and sort it
     *
     * @param event event to add
     */
    private void addEvent(Event event) {
        event_queue.add(event);
        event_queue.sort(Comparator.comparingInt(Event::getTime));
    }

    /**
     * outputs statistics calculated during running of simulator
     */
    public void outputStatistics() {
        int total_wait_time = 0;

        System.out.printf("CPU Utilization: (Idle Time/Total Time)\n%f%% (%d/%d)\n",
                ((float) total_idle_time / (time)), time, total_idle_time);

        System.out.printf("# of Scheduling Decisions:\n%d\n", 0); // 0 is a placeholder right now

        System.out.println("Process #: Wait Time");
        for (int i = 0; i < process_count; i += 1) {
            System.out.printf("Process %d: %d\n", i, process_table[i].getTotal_waiting_time());
            total_wait_time += process_table[i].getTotal_waiting_time();
        }
        System.out.printf("Avg Wait Time:\n%f\n", ((float) total_wait_time / process_count));
    }

}