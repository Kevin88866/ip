package kiki.task;
//class todo

public class Todo extends Task{
    public Todo(String task){
        super(task);
    }

    @Override
    public String toString(){
        return "[T]" + getStatusIcon() + " " + task;
    }

    @Override
    public String toSaveString() {
        return String.join(" | ", "T", isDone ? "1" : "0", task);
    }
}