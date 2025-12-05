package schedsim;

public class Process {
    // We don't want anyone modifying wait or burst times after the process is
    // created
    private int[] io_burst_lengths;
    private int[] cpu_burst_lengths;
    private int burst_number = 0;

    // do we even need these two?
    private int time_spent = 0;
    private int total_time = 0;

    private int priority;
    private int end_time;
    private int total_waiting_time = 0;
    private int waiting_instance_start = 0;

    Process(int[] times, int priority) {
        this.io_burst_lengths = new int[times.length / 2];
        this.cpu_burst_lengths = new int[times.length / 2];

        for (int i = 0; i < times.length; i += 2) {
            io_burst_lengths[i / 2] = times[i];
            System.out.println(times[i]);
        }

        for (int i = 1; i < times.length; i += 2) {
            cpu_burst_lengths[(i - 1) / 2] = times[i];
            System.out.println(times[i]);
        }

        this.priority = priority;
    }

    // some of these get and sets might not be used remove before submitting
    public int getNextIoBurstLength() {
        return io_burst_lengths[burst_number];
    }

    public int getNextCpuBurstLength() {
        return cpu_burst_lengths[burst_number];
    }

    public void incrementBurstNumber() {
        burst_number++;
    }

    public int getTime_spent() {
        return time_spent;
    }

    public void setTime_spent(int time_spent) {
        this.time_spent = time_spent;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public int getTotal_waiting_time() {
        return total_waiting_time;
    }

    public boolean hasMoreIoBursts() {
        return burst_number < cpu_burst_lengths.length;
    }

    public boolean hasMoreCpuBursts() {
        return burst_number < cpu_burst_lengths.length;
    }

    // for debugging purposes
    void print() {
        System.out.println("Priority: " + priority);
        System.out.print("IO times: ");
        for (int i = 0; i < cpu_burst_lengths.length; i += 1) {
            System.out.print(cpu_burst_lengths[i] + " ");
        }
        System.out.print("CPU times: ");
        for (int i = 0; i < cpu_burst_lengths.length; i += 1) {
            System.out.print(cpu_burst_lengths[i] + " ");
        }
        System.out.print("\n");
    }

    public void startWaiting(int start_time) {
        waiting_instance_start = start_time;
    }

    public void stopWaiting(int end_time) {
        total_waiting_time += end_time - waiting_instance_start;
    }

}
