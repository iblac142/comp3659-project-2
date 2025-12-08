package schedsim;

public class Process {
    private int[] io_burst_lengths;
    private int[] cpu_burst_lengths;
    private int burst_number = 0;
    private int burst_start_time;

    private int arrival_time;
    private int priority; // in SJF and SRJF this is also the estimated CPU burst length
    private int end_time;
    private int total_waiting_time = 0;
    private int waiting_instance_start = 0;

    private boolean been_preempted = false;

    Process(int[] times, int priority) {
        this.io_burst_lengths = new int[times.length / 2];
        this.cpu_burst_lengths = new int[times.length / 2];
        this.arrival_time = times[0];
        
        // populate io_burst_lengths table
        for (int i = 2; i < times.length; i += 2) {
            io_burst_lengths[i / 2] = times[i];
        }

        // populate cpu_burst_lengths table
        for (int i = 1; i < times.length; i += 2) {
            cpu_burst_lengths[(i - 1) / 2] = times[i];
        }

        // set listed priority
        // this is overwritten later by sfj and pesfj
        this.priority = priority;
    }

    public int getArrival_time() {
        return arrival_time;
    }

    public int getIoBurstLength() {
        return io_burst_lengths[burst_number];
    }

    public int getCpuBurstLength() {
        return cpu_burst_lengths[burst_number];
    }

    public void setCpuBurstLength(int cpu_burst_lenth) {
        this.cpu_burst_lengths[burst_number] = cpu_burst_lenth;
    }

    public void incrementBurstNumber() {
        burst_number++;
    }

    public void setBurst_start_time(int burst_start_time) {
        this.burst_start_time = burst_start_time;
    }

    public int getBurst_start_time() {
        return burst_start_time;
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

    /**
     * check if there are any more IO bursts for this process
     *
     * @return true if there are, false if not
     */
    public boolean hasMoreIoBursts() {
        return burst_number < cpu_burst_lengths.length;
    }

    /**
     * check if there are any more CPU bursts for this process
     *
     * @return true if there are, false if not
     */
    public boolean hasMoreCpuBursts() {
        return burst_number < cpu_burst_lengths.length;
    }

    public void startWaiting(int start_time) {
        waiting_instance_start = start_time;
    }

    public void stopWaiting(int end_time) {
        total_waiting_time += end_time - waiting_instance_start;
    }

    public void setPerdicted_cpu_burst_lenght(int perdicted_cpu_burst_length) {
        this.priority = perdicted_cpu_burst_length;
    }

    public void setBeen_preempted(boolean been_preempted) {
        this.been_preempted = been_preempted;
    }

    /**
     * calculates the new predicted CPU burst length using exponential averaging
     *
     * @param weighting_coefficient the weighting coefficient for the exponential
     *                              averaging equation
     */
    public void calculateNewPerdictedCpuBurstLength(float weighting_coefficient) {
        float float_priority;

        if (!been_preempted) {
            float_priority = (int) (weighting_coefficient * cpu_burst_lengths[burst_number - 1]
                    + (1 - weighting_coefficient) * priority);
        } else {
            float_priority = (int) (weighting_coefficient * cpu_burst_lengths[burst_number]
                    + (1 - weighting_coefficient) * priority);
        }

        priority = Math.round(float_priority);
    }

}
