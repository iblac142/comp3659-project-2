package schedsim;

public class Event {
    private String type;
    private int process;
    private int time;

    Event(String type) {
        this.type = type;
    }

    Event(String type, int process) {
        this.type = type;
        this.setProcess(process);
    }

    public Event(String type, int process, int time) {
        this.type = type;
        this.setProcess(process);
        this.setTime(time);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }
}
