package schedsim;

public class Event {
	String type;
	int process;
	
	Event(String type) {
		this.type = type;
	}
	
	Event(String type, int process) {
		this.type = type;
		this.process = process;
	}
}
