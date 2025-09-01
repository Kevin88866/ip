abstract class Task{
    protected String task;
    private boolean isDone;
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
    public abstract String toString();
}