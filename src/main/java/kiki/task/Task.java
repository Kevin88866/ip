package kiki.task;
//class task
//abstract class, superclass of deadline, event, todo

public abstract class Task{
    protected String task;
    protected boolean isDone;

    public Task(String task){
        this.task = task;
        this.isDone = false;
    }

    public void markDone(){
        isDone = true;
    }

    public void markNotDone(){
        isDone = false;
    }

    public String getStatusIcon(){
        return isDone? "[X]" : "[ ]";
    }

    //evey subclasses have different toString function
    public abstract String toString();
    public abstract String toSaveString();
}