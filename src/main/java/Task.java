public class Task{
    private String task;
    private boolean isDone = false;
    public Task(String task){
        this.task = task;
    }
    public void markDone(){
        isDone = true;
    }
    public void markNotDone(){
        isDone = false;
    }
    public String getTask(){
        return "[" + (isDone? "X":" ") + "] " + task;
    }
}