package schedsim;

public class Event {
    String type;
    int process;
    int time;

    Event(String type) {
        this.type = type;
    }

    Event(String type, int process) {
        this.type = type;
        this.process = process;
    }

    public Event(String type, int process, int time) {
        this.type = type;
        this.process = process;
        this.time = time;
    }
}
