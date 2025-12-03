package schedsim;

public class Process {
	// We don't want anyone modifiying wait or burst times after the process is created
	private int[] wait_times;
	private int[] burst_times;
	int burst_number = 0;
	int time_spent = 0;
	int total_time = 0;
	int priority;
	
	Process(int[] times, int priority) {
		this.wait_times = new int[times.length / 2];
		this.burst_times = new int[times.length / 2];
		
		for (int i = 0; i < times.length; i += 2) {
			wait_times[i] = times[i];
		}
		
		for (int i = 1; i < times.length; i += 2) {
			burst_times[i - 1] = times[i];
		}
		
		this.priority = priority;
	}
	
	int get_wait() {
		return wait_times[burst_number];
	}
	
	int get_burst() {
		return burst_times[burst_number];
	}
	
}
