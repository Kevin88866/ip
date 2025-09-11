package kiki.task;
//class event
//format: event ___ /from ___ /to ___

public class Event extends Task{
    private String from,to;

    public Event(String task,String from,String to){
        super(task);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + getStatusIcon() + " " + task + " (from: " + from + " to: " + to + ")";
    }
}