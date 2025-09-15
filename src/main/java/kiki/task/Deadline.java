package kiki.task;
// class Deadline
// format: deadline ___ /by ___

public class Deadline extends Task{
    private String deadline;

    public Deadline(String task, String by){
        super(task);
        deadline = by;
    }

    @Override
    public String toString(){
        return "[D]" + getStatusIcon() + " " + task + " (by: " + deadline + ")";
    }

    @Override
    public String toSaveString() {
        return String.join(" | ", "D", isDone ? "1" : "0", task, deadline);
    }
}