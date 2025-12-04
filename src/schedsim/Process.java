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

    Process(int[] times, int priority) {
        this.io_burst_times = new int[times.length / 2];
        this.cpu_burst_times = new int[times.length / 2];

        for (int i = 0; i < times.length; i += 2) {
            io_burst_times[i] = times[i];
        }

        for (int i = 1; i < times.length; i += 2) {
            cpu_burst_times[i - 1] = times[i];
        }

        this.priority = priority;
    }

    int getWait() {
        return io_burst_times[burst_number];
    }

    int getBurst() {
        return cpu_burst_times[burst_number];
    }

    boolean hasMoreIoBursts() {
        return burst_number < io_burst_times.length;
    }

    boolean hasMoreCpuBursts() {
        return burst_number < cpu_burst_times.length;
    }

}
