package schedsim;

public class Process {
    // We don't want anyone modifiying wait or burst times after the process is
    // created
    private int[] io_burst_times;
    private int[] cpu_burst_times;
    int burst_number = 0;
    int time_spent = 0;
    int total_time = 0;
    int priority;
    int end_time;
    int total_waiting_time = 0;
    int waiting_instance_start = 0;
    boolean is_waiting = true;

    Process(int[] times, int priority) {
        this.io_burst_times = new int[times.length / 2];
        this.cpu_burst_times = new int[times.length / 2];

        for (int i = 0; i < times.length; i += 2) {
            io_burst_times[i / 2] = times[i];
            System.out.println(times[i]);
        }

        for (int i = 1; i < times.length; i += 2) {
            cpu_burst_times[(i - 1) / 2] = times[i];
            System.out.println(times[i]);
        }

        this.priority = priority;
    }

    public int getWait() {
        return io_burst_times[burst_number];
    }

    public int getBurst() {
        return cpu_burst_times[burst_number];
    }

    public boolean hasMoreIoBursts() {
        return burst_number < io_burst_times.length;
    }

    public boolean hasMoreCpuBursts() {
        return burst_number < cpu_burst_times.length;
    }
    
    // for debugging purposes
    void print() {
    	System.out.println("Priority: " + priority);
    	System.out.print("IO times: ");
    	for (int i = 0; i < io_burst_times.length; i += 1) {
    		System.out.print(io_burst_times[i] + " ");
    	}
    	System.out.print("CPU times: ");
    	for (int i = 0; i < cpu_burst_times.length; i += 1) {
    		System.out.print(cpu_burst_times[i] + " ");
    	}
    	System.out.print("\n");
    }

    public void startWaiting(int start_time) {
        is_waiting = true;
        waiting_instance_start = start_time;
    }

    public void stopWaiting(int end_time) {
        is_waiting = false;
        total_waiting_time += end_time - waiting_instance_start;
    }
}
